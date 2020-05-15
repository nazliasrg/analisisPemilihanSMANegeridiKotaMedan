package com.example.skripsi;

import java.util.ArrayList;

public class Zonasi {
    private String namaSekolah;
    private String hasilJarak;
    private String hasil;

    public Zonasi(String namaSekolah, String hasilJarak, String hasil) {
        this.namaSekolah = namaSekolah;
        this.hasilJarak = hasilJarak;
        this.hasil = hasil;
    }
    public String getNamaSekolah() {
        return namaSekolah;
    }

    public void setNamaSekolah(String namaSekolah) {
        this.namaSekolah = namaSekolah;
    }

    public String getHasilJarak() {
        return hasilJarak;
    }

    public void setHasilJarak(String hasilJarak) {
        this.hasilJarak = hasilJarak;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }
}

