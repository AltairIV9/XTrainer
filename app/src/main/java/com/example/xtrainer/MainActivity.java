package com.example.xtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.xtrainer.databinding.ActivityConfigMusculosBinding;
import com.example.xtrainer.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnConfigActivity;
    private FloatingActionButton fabAddTreino;

    ArrayList<Treino> treinos = new ArrayList<>();
    ArrayAdapter listAdapter;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnConfigActivity = findViewById(R.id.btnConfigActivity);
        fabAddTreino = findViewById(R.id.fabAddTreino);

        setListViewTreinos();
        setClickListener();
    }

    private void setListViewTreinos(){
        listAdapter = new TreinosListAdapter(MainActivity.this, treinos);
        binding.lvTreinos.setAdapter(listAdapter);
    }

    private void setClickListener(){
        btnConfigActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConfigActivity.class));
            }
        });

        fabAddTreino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainAddTreinoActivity.class));
            }
        });
    }
}