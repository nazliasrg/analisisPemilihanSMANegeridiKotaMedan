package com.example.skripsi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class sistemNonZonasi extends AppCompatActivity {

    private TextView alamatSiswa;

    String alamatTempatTinggal, LatlngTempatTinggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sistem_non_zonasi);

        alamatSiswa = findViewById(R.id.alamatSiswa);

        alamatTempatTinggal = getIntent().getStringExtra("alamatlokasi");
        alamatSiswa.setText(alamatTempatTinggal);

        LatlngTempatTinggal = getIntent().getStringExtra("latlnglokasi");
    }
}
