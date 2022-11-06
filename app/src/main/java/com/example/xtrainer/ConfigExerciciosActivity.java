package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

        setButtons();
        setSpinner();
        setListViewExercicios();
        getMusculosDB();
    }

    private void setSpinner(){
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, musculosNomes); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMusculos.setAdapter(spinnerArrayAdapter);

        spnMusculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exercicios.clear();
                listAdapter.notifyDataSetChanged();
                exerciciosRef.child(musculos.get(position).getId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Exercicio novoExercicio = snapshot.getValue(Exercicio.class);
                        if(novoExercicio != null) {
                            exercicios.add(novoExercicio);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListViewExercicios(){
        listAdapter = new ExerciciosListAdapter(ConfigExerciciosActivity.this, exercicios);
        binding.lvExercicios.setAdapter(listAdapter);
        binding.lvExercicios.setClickable(true);
        binding.lvExercicios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfigExerciciosActivity.this);
                builder.setTitle("Excluir exercício!")
                        .setMessage("Deseja realmente excluir o exercício " + exercicios.get(position).getNome() + "?")
                        .setNegativeButton("Não", null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exerciciosRef.child(musculos.get(spnMusculos.getSelectedItemPosition()).getId()).child(exercicios.get(position).getId()).removeValue();
                                exercicios.remove(position);
                                listAdapter.notifyDataSetChanged();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });
    }

    private void getMusculosDB(){
        musculosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Musculo novoMusculo = snapshot.getValue(Musculo.class);
                if(novoMusculo != null) {
                    musculos.add(novoMusculo);
                    musculosNomes.add(novoMusculo.getNome());
                    spinnerArrayAdapter.notifyDataSetChanged();
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
    }

    private void setButtons(){
        fabAddExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigExerciciosActivity.this, ConfigExerciciosAddExerciciosActivity.class));
            }
        });
    }
}
