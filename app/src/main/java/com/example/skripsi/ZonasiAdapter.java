package com.example.skripsi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ZonasiAdapter extends ArrayAdapter<Zonasi> {

    private Context mContext;
    private List<Zonasi> zonasiList = new ArrayList<>();

    public ZonasiAdapter(@NonNull Context context, ArrayList<Zonasi> list) {
        super(context, 0, list);
        mContext = context;
        zonasiList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.desain,parent,false);

        Zonasi currentZonasi = zonasiList.get(position);

        TextView namaSekolah = listItem.findViewById(R.id.namaSMA);
        namaSekolah.setText(currentZonasi.getNamaSekolah());

        TextView hasilJarak = listItem.findViewById(R.id.hasilSkorJarak);
        hasilJarak.setText(currentZonasi.getHasilJarak());

        TextView hasil = listItem.findViewById(R.id.hasilSkorAkhir);
        hasil.setText(currentZonasi.getHasil());

        return listItem;
    }

}
