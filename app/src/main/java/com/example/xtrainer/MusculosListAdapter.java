package com.example.xtrainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusculosListAdapter extends ArrayAdapter<Musculo> {
    public MusculosListAdapter(Context context, ArrayList<Musculo> musculos){

        super(context, R.layout.list_item_musculo, musculos);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Musculo musculo = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_musculo, parent, false);
        }

        TextView tvNomeMusculo = convertView.findViewById(R.id.tvNomeMusculo);

        tvNomeMusculo.setText(musculo.getNome());

        return convertView;
    }
}
