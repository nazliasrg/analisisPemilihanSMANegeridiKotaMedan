package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skripsi.network.ApiServices;
import com.example.skripsi.network.InitLibrary;
import com.example.skripsi.response.Distance;
import com.example.skripsi.response.Duration;
import com.example.skripsi.response.LegsItem;
import com.example.skripsi.response.ResponseRoute;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class sistemZonasi extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private TextView alamatSiswa;
    private Button hasil;
    EditText bindo, bing, matematika, ipa;
    String nilaiBindo, nilaiBing, nilaiMate, nilaiIPA, jarak1, nilaiJarak;
    double bahasa, inggris, mate, sains, hasilNilai, angkaJarak;

    String alamatTempatTinggal, LatlngTempatTinggal;

    private String API_KEY = "AIzaSyBCITS4OOelRvvFGj8yQAZ0Knz6m8Mvsz8";

    private LatLng SMAN1 = new LatLng(3.581612, 98.669566);

    public static final int PICK_UP = 0;
    public static final int DEST_LOC = 1;
    private static int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sistem_zonasi);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY);
        }

        PlacesClient placesClient = Places.createClient(this);

        wigetInit();

        hasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionDistance(LatlngTempatTinggal, REQUEST_CODE);
            }
        });
    }

    private void wigetInit() {

        alamatSiswa = findViewById(R.id.alamatSiswa);

        alamatTempatTinggal = getIntent().getStringExtra("alamatlokasi");
        alamatSiswa.setText(alamatTempatTinggal);

        LatlngTempatTinggal = getIntent().getStringExtra("latlnglokasi");

        hasil = findViewById(R.id.hasil);

        bindo = findViewById(R.id.bahasa);
        bing = findViewById(R.id.inggris);
        matematika = findViewById(R.id.matematika);
        ipa = findViewById(R.id.ipa);

    }

    private void actionDistance(String latlngTempatTinggal, int requestCode) {
        String TempatTinggal = latlngTempatTinggal;
        String SMAN1Medan = SMAN1.latitude + "," + SMAN1.longitude;

        ApiServices api = InitLibrary.getInstance();
        // Siapkan request
        Call<ResponseRoute> routeRequest = api.request_route(TempatTinggal, SMAN1Medan, API_KEY);
        // kirim request
        routeRequest.enqueue(new Callback<ResponseRoute>() {
            @Override
            public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {

                if (response.isSuccessful()){
                    // tampung response ke variable
                    ResponseRoute dataDirection = response.body();

                    LegsItem dataLegs = dataDirection.getRoutes().get(0).getLegs().get(0);

                    Distance dataDistance = dataLegs.getDistance();
                    Duration dataDuration = dataLegs.getDuration();

                    jarak1 = dataDistance.getText().toString();
                    angkaJarak = dataDistance.getValue();
                    nilaiJarak = Double.toString(angkaJarak);

                    hasil();
                }
            }

            @Override
            public void onFailure(Call<ResponseRoute> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void hasil() {

        nilaiBindo = bindo.getText().toString();
        nilaiBing = bing.getText().toString();
        nilaiMate = matematika.getText().toString();
        nilaiIPA = ipa.getText().toString();

        Intent intentHasil = new Intent(sistemZonasi.this, hasilSistemZonasi.class);
        intentHasil.putExtra("skorJarak", nilaiJarak);
        intentHasil.putExtra("nilaiBindo", nilaiBindo);
        intentHasil.putExtra("nilaiBing", nilaiBing);
        intentHasil.putExtra("nilaiMate", nilaiMate);
        intentHasil.putExtra("nilaiIPA", nilaiIPA);
        intentHasil.putExtra("LatlngTempatTinggal", LatlngTempatTinggal);
        intentHasil.putExtra("alamatTempatTinggal", alamatTempatTinggal);

        startActivity(intentHasil);
        finish();
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

    public static class Zonasi {

    }
}
