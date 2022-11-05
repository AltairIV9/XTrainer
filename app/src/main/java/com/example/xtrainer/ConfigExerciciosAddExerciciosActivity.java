package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConfigExerciciosAddExerciciosActivity extends AppCompatActivity {

    private EditText edtNomeExercicio, edtPeso, edtRepeticoes;
    private Button btnAddExercicio;
    private Spinner spnMusculosAddExercicio;

    private ArrayList<Musculo> musculos = new ArrayList<>();
    private ArrayList<String> musculosNomes = new ArrayList<>();

    private ArrayAdapter<String> spinnerArrayAdapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference musculosRef = database.getReference().child("musculos");
    private DatabaseReference exerciciosRef = database.getReference().child("exercicios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_exercicios_add_exercicios);

        edtNomeExercicio = findViewById(R.id.edtNomeExercicio);
        edtPeso = findViewById(R.id.edtPeso);
        edtRepeticoes = findViewById(R.id.edtRepeticoes);
        btnAddExercicio = findViewById(R.id.btnAddExercicio);
        spnMusculosAddExercicio = findViewById(R.id.spnMusculosAddExercicio);

        edtNomeExercicio.requestFocus();

        setSpinner();
        setOnClickListener();
        getMusculosDB();
    }

    private void setOnClickListener(){
        btnAddExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int peso = 1;
                int repeticoes = 8;
                if(!edtPeso.getText().toString().isEmpty()){
                    peso = Integer.parseInt(edtPeso.getText().toString());
                }
                if(!edtRepeticoes.getText().toString().isEmpty()){
                    repeticoes = Integer.parseInt(edtRepeticoes.getText().toString());
                }
                if(musculosNomes.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfigExerciciosAddExerciciosActivity.this);

                    builder.setTitle("Músculo inválido!")
                            .setMessage("Adicione um músculo antes.")
                            .setPositiveButton("Ok", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if(edtNomeExercicio.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfigExerciciosAddExerciciosActivity.this);

                    builder.setTitle("Nome de exercício inválido!")
                            .setMessage("Dê um nome ao exercício.")
                            .setPositiveButton("Ok", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                String key = exerciciosRef.child(musculos.get(spnMusculosAddExercicio.getSelectedItemPosition()).getId()).push().getKey();
                Exercicio novoExercicio = new Exercicio(edtNomeExercicio.getText().toString(), key, musculos.get(spnMusculosAddExercicio.getSelectedItemPosition()).getId(), peso, repeticoes);
                exerciciosRef.child(musculos.get(spnMusculosAddExercicio.getSelectedItemPosition()).getId()).child(key).setValue(novoExercicio);
                finish();
            }
        });
    }

    private void setSpinner(){
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, musculosNomes); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMusculosAddExercicio.setAdapter(spinnerArrayAdapter);
    }

    private void getMusculosDB() {
        musculosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                musculos.add(snapshot.getValue(Musculo.class));
                musculosNomes.add(musculos.get(musculos.size() - 1).getNome());
                spinnerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Musculo changedMusculo = snapshot.getValue(Musculo.class);
                if (changedMusculo != null) {
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
                if (removedMusculo != null) {
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