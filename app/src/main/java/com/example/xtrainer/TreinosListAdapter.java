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

public class TreinosListAdapter extends ArrayAdapter<Treino> {

    public TreinosListAdapter(Context context, ArrayList<Treino> treinos){

        super(context, R.layout.list_item_treino, treinos);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Treino treino = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_treino, parent, false);
        }

        TextView tvNomeTreino = convertView.findViewById(R.id.tvNomeTreinoListItem);

        tvNomeTreino.setText(treino.getNome());

        return convertView;
    }
}
