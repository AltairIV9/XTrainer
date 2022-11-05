package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.xtrainer.databinding.ActivityConfigMusculosBinding;
import com.example.xtrainer.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnConfigActivity;
    private FloatingActionButton fabAddTreino;

    private ArrayList<Treino> treinos = new ArrayList<>();
    private ArrayAdapter listAdapter;

    private Treino treinoOnline;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference treinosRef = database.getReference().child("treinos");
    private DatabaseReference treinoAtualRef = database.getReference().child("treinoAtual");
    private DatabaseReference treinoOnlineRef = database.getReference().child("treinoOnline").child("treino");

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
        getTreinosDB();
    }

    private void setListViewTreinos(){
        listAdapter = new TreinosListAdapter(MainActivity.this, treinos);
        binding.lvTreinos.setAdapter(listAdapter);
        binding.lvTreinos.setClickable(true);
        binding.lvTreinos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(treinoOnline != null && treinos.get(position).getId().equals(treinoOnline.getId())){
                    startActivity(new Intent(MainActivity.this, MainTreinoOnlineActivity.class));
                }else {
                    treinoAtualRef.setValue(treinos.get(position));
                    startActivity(new Intent(MainActivity.this, MainTreinoActivity.class));
                }
            }
        });
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

    private void getTreinosDB(){
        treinosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Treino novoTreino = snapshot.getValue(Treino.class);
                if(novoTreino != null){
                    treinos.add(novoTreino);
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        treinoOnlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                treinoOnline = snapshot.getValue(Treino.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}