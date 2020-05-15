package com.example.skripsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String API_KEY = "AIzaSyBCITS4OOelRvvFGj8yQAZ0Knz6m8Mvsz8";

    public LatLng pickUpLatLng = null;

    private TextView tvStartAddress, tvEndAddress;
    private Button btnNext;
    private LinearLayout infoPanel;
    private TextView tvPickUpFrom;
    private String lokasiAwal, placeAdress;

    public static final int PICK_UP = 0;
    public static final int DEST_LOC = 1;
    private static int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY);
        }

        PlacesClient placesClient = Places.createClient(this);

        // Inisialisasi Widget
        wigetInit();

        tvPickUpFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceAutoComplete(PICK_UP);
            }
        });
    }

    private void wigetInit() {
        // Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        infoPanel = findViewById(R.id.infoPanel);
        // Widget
        tvPickUpFrom = findViewById(R.id.tvPickUpFrom);

        btnNext = findViewById(R.id.btnNext);
    }

    public class PlacesFieldSelector {

        private final List<PlaceField> placeFields;

        public PlacesFieldSelector() {
            this(Arrays.asList(Place.Field.values()));
        }

        public PlacesFieldSelector(List<Place.Field> validFields) {
            placeFields = new ArrayList<>();
            for (Place.Field field : validFields) {
                placeFields.add(new PlaceField(field));
            }
        }

        /**
         * Returns all {@link Place.Field} that are selectable.
         */
        public List<Place.Field> getAllFields() {
            List<Place.Field> list = new ArrayList<>();
            for (PlaceField placeField : placeFields) {
                list.add(placeField.field);
            }

            return list;
        }

        /**
         * Returns all {@link Place.Field} values the user selected.
         */
        public List<Place.Field> getSelectedFields() {
            List<Place.Field> selectedList = new ArrayList<>();
            for (PlaceField placeField : placeFields) {
                if (placeField.checked) {
                    selectedList.add(placeField.field);
                }
            }

            return selectedList;
        }

        /**
         * Returns a String representation of all selected {@link Place.Field} values. See {@link
         * #getSelectedFields()}.
         */
        public String getSelectedString() {
            StringBuilder builder = new StringBuilder();
            for (Place.Field field : getSelectedFields()) {
                builder.append(field).append("\n");
            }

            return builder.toString();
        }

        //////////////////////////
        // Helper methods below //
        //////////////////////////

        private class PlaceField {
            final Place.Field field;
            boolean checked;

            public PlaceField(Place.Field field) {
                this.field = field;
            }
        }

        private class PlaceFieldArrayAdapter extends ArrayAdapter<PlaceField>
                implements AdapterView.OnItemClickListener {

            public PlaceFieldArrayAdapter(Context context, List<PlaceField> placeFields) {
                super(context, android.R.layout.simple_list_item_multiple_choice, placeFields);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                PlaceField placeField = getItem(position);
                updateView(view, placeField);

                return view;
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceField placeField = getItem(position);
                placeField.checked = !placeField.checked;
                updateView(view, placeField);
            }

            private void updateView(View view, PlaceField placeField) {
                if (view instanceof CheckedTextView) {
                    CheckedTextView checkedTextView = (CheckedTextView) view;
                    checkedTextView.setText(placeField.field.toString());
                    checkedTextView.setChecked(placeField.checked);
                }
            }
        }
    }

    public void showPlaceAutoComplete(int typeLocation){
        // isi RESUT_CODE tergantung tipe lokasi yg dipilih.
        // titik jemput atau tujuan
        REQUEST_CODE = typeLocation;

        //List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        PlacesFieldSelector fieldSelector = new PlacesFieldSelector();

        Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                fieldSelector.getAllFields())
                .build(HomeActivity.this);

        // jalankan intent impilist
        startActivityForResult(autocompleteIntent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == AutocompleteActivity.RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(intent);
            //   place.getName();
            //   place.getAddress();
            placeAdress = place.getAddress().toString();
            LatLng placeLatLng = place.getLatLng();
            String placeName = place.getName().toString();

            // Cek user milih titik jemput atau titik tujuan
            switch (REQUEST_CODE) {
                case PICK_UP:
                    // Set ke widget lokasi asal
                    tvPickUpFrom.setText(placeAdress);
                    pickUpLatLng = place.getLatLng();
                    break;
            }

            if (pickUpLatLng != null) {
                actionRoute(placeLatLng, REQUEST_CODE);
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(intent);
        } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    private void actionRoute(LatLng placeLatLng, int requestCode) {
        lokasiAwal = pickUpLatLng.latitude + "," + pickUpLatLng.longitude;

        mMap.addMarker(new MarkerOptions().position(pickUpLatLng).title("Lokasi Awal"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickUpLatLng,18));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMenu = new Intent(HomeActivity.this, MenuActivity.class);
                intentMenu.putExtra("latlnglokasi", lokasiAwal);
                intentMenu.putExtra("alamatlokasi", placeAdress);
                startActivity(intentMenu);

            }
        }
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ilkom = new LatLng(3.562870, 98.659770);
        mMap.addMarker(new MarkerOptions().position(ilkom).title("S1 Ilmu Komputer"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ilkom,18));
        mMap.setPadding(10, 180, 10, 10);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

         mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
