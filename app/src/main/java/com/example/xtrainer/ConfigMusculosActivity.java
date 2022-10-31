package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.xtrainer.databinding.ActivityConfigMusculosBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConfigMusculosActivity extends AppCompatActivity {

    private FloatingActionButton fabAddMusculo;
    private ListView lvMusculos;

    private ArrayList<Musculo> musculos = new ArrayList<>();
    ArrayAdapter listAdapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference musculosRef = database.getReference().child("musculos");

    ActivityConfigMusculosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigMusculosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fabAddMusculo = findViewById(R.id.fabAddMusculo);
        lvMusculos = findViewById(R.id.lvMusculos);

        setOnClickListeners();
        setListViewMusculos();
        getMusculosDB();
    }

    private void getMusculosDB(){
        musculosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                musculos.add(snapshot.getValue(Musculo.class));
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Musculo changedMusculo = snapshot.getValue(Musculo.class);
                if(changedMusculo !=null) {
                    for (int i = 0; i < musculos.size(); i++) {
                        if (musculos.get(i).getId().equals(changedMusculo.getId())) {
                            musculos.remove(i);
                            musculos.add(i, changedMusculo);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Musculo removedMusculo = snapshot.getValue(Musculo.class);
                if(removedMusculo != null) {
                    for (int i = 0; i < musculos.size(); i++) {
                        if (musculos.get(i).getId().equals(removedMusculo.getId())) {
                            musculos.remove(i);
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListViewMusculos(){
        listAdapter = new MusculosListAdapter(ConfigMusculosActivity.this, musculos);
        binding.lvMusculos.setAdapter(listAdapter);
    }

    private void setOnClickListeners(){
        fabAddMusculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigMusculosActivity.this, ConfigMusculosAddMusculosActivity.class));
            }
        });
    }
}