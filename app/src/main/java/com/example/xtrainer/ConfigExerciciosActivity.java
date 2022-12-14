package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.xtrainer.databinding.ActivityConfigExerciciosBinding;
import com.example.xtrainer.databinding.ActivityConfigMusculosBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConfigExerciciosActivity extends AppCompatActivity {

    private Spinner spnMusculos;
    private ListView lvExercicios;
    private FloatingActionButton fabAddExercicio;

    private ArrayList<Musculo> musculos = new ArrayList<>();
    private ArrayList<String> musculosNomes = new ArrayList<>();
    private ArrayList<Exercicio> exercicios = new ArrayList<>();

    private ArrayAdapter<String> spinnerArrayAdapter;
    private ArrayAdapter listAdapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference musculosRef = database.getReference().child("musculos");
    private DatabaseReference exerciciosRef = database.getReference().child("exercicios");

    ActivityConfigExerciciosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigExerciciosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fabAddExercicio = findViewById(R.id.fabAddExercicio);
        spnMusculos = findViewById(R.id.spnMusculos);
        lvExercicios = findViewById(R.id.lvExercicios);

        setClickListeners();
        setSpinner();
        setListViewExercicios();
        getMusculosDB();
    }

    private void setClickListeners(){
        fabAddExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigExerciciosActivity.this, ConfigExerciciosAddExerciciosActivity.class));
            }
        });
    }

    private void setSpinner(){
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, musculosNomes); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMusculos.setAdapter(spinnerArrayAdapter);

        spnMusculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercicios.clear();
                exerciciosRef.child(musculos.get(position).getId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        exercicios.add(snapshot.getValue(Exercicio.class));
                        listAdapter.notifyDataSetChanged();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListViewExercicios(){
        listAdapter = new ExerciciosListAdapter(ConfigExerciciosActivity.this, exercicios);
        binding.lvExercicios.setAdapter(listAdapter);
    }

    private void getMusculosDB(){
        musculosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                musculos.add(snapshot.getValue(Musculo.class));
                musculosNomes.add(musculos.get(musculos.size()-1).getNome());
                spinnerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Musculo changedMusculo = snapshot.getValue(Musculo.class);
                if(changedMusculo !=null) {
                    for (int i = 0; i < musculos.size(); i++) {
                        if (musculos.get(i).getId().equals(changedMusculo.getId())) {
                            musculos.remove(i);
                            musculosNomes.remove(i);
                            musculos.add(i, changedMusculo);
                            musculosNomes.add(i, changedMusculo.getNome());
                            spinnerArrayAdapter.notifyDataSetChanged();
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
                            musculosNomes.remove(i);
                            spinnerArrayAdapter.notifyDataSetChanged();
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
}
