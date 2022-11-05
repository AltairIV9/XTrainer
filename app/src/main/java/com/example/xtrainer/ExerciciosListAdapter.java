package com.example.xtrainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExerciciosListAdapter extends ArrayAdapter<Exercicio> {

    public ExerciciosListAdapter(Context context, ArrayList<Exercicio> exercicios){

        super(context, R.layout.list_item_exercicio, exercicios);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Exercicio exercicio = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_exercicio, parent, false);
        }

        TextView tvNomeExercicioListItem = convertView.findViewById(R.id.tvNomeExercicioListItem);

        tvNomeExercicioListItem.setText(exercicio.getNome());

        return convertView;
    }
}
