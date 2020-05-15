package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.skripsi.network.ApiServices;
import com.example.skripsi.network.InitLibrary;
import com.example.skripsi.response.Distance;
import com.example.skripsi.response.LegsItem;
import com.example.skripsi.response.ResponseRoute;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class hasilSistemZonasi extends AppCompatActivity {

    TextView hasilNilai, alamatSiswa;
    String skorJarak, nilaiBindo, nilaiBing, nilaiMate, nilaiIPA, nilaiHasil, skorJarakAkhir, hasilStr, jarakStr, LatlngTempatTinggal, sma, alamatTempatTinggal, a;
    double bindo, bing, mate, ipa, nilai, nilaiJarak, hasilDoub, jarakDoub;

    int size;

    List<String> SMANegeri = new ArrayList<>();
    List<String> hasilJarak = new ArrayList<>();
    List<String> hasilAkhir = new ArrayList<>();

    String[] namaSekolah = {"SMA Negeri 1 Medan", "SMA Negeri 2 Medan", "SMA Negeri 3 Medan",
                            "SMA Negeri 4 Medan", "SMA Negeri 5 Medan", "SMA Negeri 6 Medan",
                            "SMA Negeri 7 Medan", "SMA Negeri 8 Medan", "SMA Negeri 9 Medan",
                            "SMA Negeri 10 Medan", "SMA Negeri 11 Medan", "SMA Negeri 12 Medan",
                            "SMA Negeri 13 Medan", "SMA Negeri 14 Medan", "SMA Negeri 15 Medan",
                            "SMA Negeri 16 Medan", "SMA Negeri 17 Medan", "SMA Negeri 18 Medan",
                            "SMA Negeri 19 Medan", "SMA Negeri 20 Medan", "SMA Negeri 21 Medan"
    };


    //String[] mHasilJarak;
    //String[] mhasilAkhir;
    //String mHasilJarak[] = new String[21];
    //String hasilAkhir[] = new String[21];

    ArrayList<Zonasi> zonasiList = new ArrayList<>();

    private String API_KEY = "AIzaSyBCITS4OOelRvvFGj8yQAZ0Knz6m8Mvsz8";
    public static final int PICK_UP = 0;
    public static final int DEST_LOC = 1;
    private static int REQUEST_CODE = 0;

    private ListView listView;
    private ZonasiAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_sistem_zonasi);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY);
        }

        PlacesClient placesClient = Places.createClient(this);

        hasilNilai = findViewById(R.id.nilaiUN);
        alamatSiswa = findViewById(R.id.alamatSiswa);

        ListView listView = findViewById(R.id.listview);

        skorJarak = getIntent().getStringExtra("skorJarak");
        nilaiBindo = getIntent().getStringExtra("nilaiBindo");
        nilaiBing = getIntent().getStringExtra("nilaiBing");
        nilaiMate = getIntent().getStringExtra("nilaiMate");
        nilaiIPA = getIntent().getStringExtra("nilaiIPA");
        LatlngTempatTinggal = getIntent().getStringExtra("LatlngTempatTinggal");
        alamatTempatTinggal = getIntent().getStringExtra("alamatTempatTinggal");

        alamatSiswa.setText(alamatTempatTinggal);

        bindo = Double.parseDouble(nilaiBindo);
        bing = Double.parseDouble(nilaiBing);
        mate = Double.parseDouble(nilaiMate);
        ipa = Double.parseDouble(nilaiIPA);
        nilai = 0.1*(bindo+bing+mate+ipa);

        nilaiHasil = Double.toString(nilai);
        hasilNilai.setText(String.format("%.2f", nilai));

        SMANegeri.add(new String("3.581612,98.669566"));
        SMANegeri.add(new String("3.546568,98.682362"));
        SMANegeri.add(new String("3.621412,98.671977"));
        SMANegeri.add(new String("3.597267,98.655111"));
        SMANegeri.add(new String("3.565695,98.698324"));
        SMANegeri.add(new String("3.581412,98.692059"));
        SMANegeri.add(new String("3.601499,98.678906"));
        SMANegeri.add(new String("3.589381,98.698085"));
        SMANegeri.add(new String("3.722349,98.689050"));
        SMANegeri.add(new String("3.582216,98.691475"));
        SMANegeri.add(new String("3.593730,98.732131"));
        SMANegeri.add(new String("3.612068,98.640546"));
        SMANegeri.add(new String("3.529935,98.682744"));
        SMANegeri.add(new String("3.561102,98.716707"));
        SMANegeri.add(new String("3.569777,98.614360"));
        SMANegeri.add(new String("3.711748,98.642710"));
        SMANegeri.add(new String("3.508695,98.615943"));
        SMANegeri.add(new String("3.588039,98.693438"));
        SMANegeri.add(new String("3.732649,98.678032"));
        SMANegeri.add(new String("3.774633,98.705486"));
        SMANegeri.add(new String("3.553006,98.726029"));

        size = SMANegeri.size();

        for (int i=0; i<namaSekolah.length; i++){

            a= namaSekolah[i];
            sma = SMANegeri.get(i);

            ApiServices api = InitLibrary.getInstance();
            // Siapkan request
            Call<ResponseRoute> routeRequest = api.request_route(LatlngTempatTinggal, sma, API_KEY);

            // kirim request
            final int finalI = i;
            routeRequest.enqueue(new Callback<ResponseRoute>() {
                @Override
                public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {

                    if (response.isSuccessful()){
                        // tampung response ke variable
                        ResponseRoute dataDirection = response.body();
                        // mencari jarak
                        LegsItem dataLegs = dataDirection.getRoutes().get(0).getLegs().get(0);
                        Distance dataDistance = dataLegs.getDistance();

                        // perhitungan skor jarak
                        jarakStr = dataDistance.getText();
                        jarakDoub = dataDistance.getValue();
                        nilaiJarak = 60-(jarakDoub/300);
                        skorJarakAkhir = Double.toString(nilaiJarak);

                        // perhitungan skor akhir, skor UN+skor jarak
                        hasilDoub = nilai+nilaiJarak;
                        hasilStr = Double.toString(hasilDoub);


                        zonasiList.add(new Zonasi(a, skorJarakAkhir, hasilStr));
                    }
                }

                @Override
                public void onFailure(Call<ResponseRoute> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        mAdapter = new ZonasiAdapter(this, zonasiList);
        listView.setAdapter(mAdapter);

    }
}
