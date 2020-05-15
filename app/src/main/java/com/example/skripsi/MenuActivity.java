package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.skripsi.network.ApiServices;
import com.example.skripsi.network.InitLibrary;
import com.example.skripsi.response.Distance;
import com.example.skripsi.response.LegsItem;
import com.example.skripsi.response.ResponseRoute;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private CardView sistemZonasi, sistemNonZonasi, bantuan;
    private TextView alamatSiswa;

    String latlnglokasi, alamatTempatTinggal;

    private String API_KEY = "AIzaSyBCITS4OOelRvvFGj8yQAZ0Knz6m8Mvsz8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        alamatSiswa = findViewById(R.id.alamatSiswa);
        sistemZonasi = findViewById(R.id.sistemZonasi);
        sistemNonZonasi = findViewById(R.id.sistemNonZonasi);
        bantuan = findViewById(R.id.bantuan);

        alamatTempatTinggal = getIntent().getStringExtra("alamatlokasi");
        alamatSiswa.setText(alamatTempatTinggal);

        latlnglokasi = getIntent().getStringExtra("latlnglokasi");

        sistemZonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentZonasi = new Intent(MenuActivity.this, com.example.skripsi.sistemZonasi.class);
                intentZonasi.putExtra("alamatlokasi", alamatTempatTinggal);
                intentZonasi.putExtra("latlnglokasi", latlnglokasi);
                startActivity(intentZonasi);


            }
        });

        sistemNonZonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNonZonasi = new Intent(MenuActivity.this, com.example.skripsi.sistemNonZonasi.class);
                intentNonZonasi.putExtra("alamatlokasi", alamatTempatTinggal);
                intentNonZonasi.putExtra("latlnglokasi", latlnglokasi);
                startActivity(intentNonZonasi);

            }
        });

        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBantuan = new Intent(MenuActivity.this, com.example.skripsi.bantuan.class);
                startActivity(intentBantuan);

            }
        });

    }

}
