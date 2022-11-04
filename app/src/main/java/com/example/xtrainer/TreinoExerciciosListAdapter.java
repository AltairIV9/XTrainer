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

public class TreinoExerciciosListAdapter extends ArrayAdapter<Exercicio> {
    public TreinoExerciciosListAdapter(Context context, ArrayList<Exercicio> exercicios){

        super(context, R.layout.list_item_treino_exercicio, exercicios);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Exercicio exercicio = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_treino_exercicio, parent, false);
        }

        TextView tvNomeTreinoExercicioListItem = convertView.findViewById(R.id.tvNomeTreinoExercicioListItem);

        tvNomeTreinoExercicioListItem.setText(exercicio.getNome());

        return convertView;
    }
}
