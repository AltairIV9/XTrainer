package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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

public class MainAddTreinoActivity extends AppCompatActivity {

    private EditText edtNomeTreino;
    private Spinner spnMusculo1, spnMusculo2;
    private Button btnAddTreino;

    private ArrayList<Musculo> musculos = new ArrayList<>();
    private ArrayList<String> musculosNomes = new ArrayList<>();
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ArrayList<Treino> treinos = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference musculosRef = database.getReference().child("musculos");
    private DatabaseReference treinosRef = database.getReference().child("treinos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_treino);

        edtNomeTreino = findViewById(R.id.edtNomeTreino);
        spnMusculo1 = findViewById(R.id.spnMusculo1);
        spnMusculo2 = findViewById(R.id.spnMusculo2);
        btnAddTreino = findViewById(R.id.btnAddTreino);

        setSpinnerMusculos();
        setOnClickListener();
        getMusculosDB();
        getTreinosDB();
    }

    private void setSpinnerMusculos() {
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, musculosNomes);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMusculo1.setAdapter(spinnerArrayAdapter);
        spnMusculo2.setAdapter(spinnerArrayAdapter);
    }

    private void setOnClickListener() {
        btnAddTreino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNomeTreino.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainAddTreinoActivity.this);

                    builder.setTitle("Nome inválido!")
                            .setMessage("Escolha um nome para o treino.")
                            .setPositiveButton("Ok", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }else{
                    for(Treino treino : treinos){
                        if(treino.getNome().equals(edtNomeTreino.getText().toString())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainAddTreinoActivity.this);

                            builder.setTitle("Treino já existe!")
                                    .setMessage("Escolha outro nome.")
                                    .setPositiveButton("Ok", null);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return;
                        }
                    }
                }
                if(musculos.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainAddTreinoActivity.this);

                    builder.setTitle("Não há músculos!")
                            .setMessage("Adicione músculos nas configurações.")
                            .setPositiveButton("ok", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                if(spnMusculo1.getSelectedItemPosition() == spnMusculo2.getSelectedItemPosition()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainAddTreinoActivity.this);

                    builder.setTitle("Músculos são iguais!")
                            .setMessage("Deseja continuar?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String key = treinosRef.push().getKey();
                                                    Treino novoTreino = new Treino(edtNomeTreino.getText().toString(), key, musculos.get(spnMusculo1.getSelectedItemPosition()).getId(), musculos.get(spnMusculo2.getSelectedItemPosition()).getId());
                                                    treinosRef.child(key).setValue(novoTreino);
                                                    finish();
                                                }
                                            })
                            .setNegativeButton("Não", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    String key = treinosRef.push().getKey();
                    Treino novoTreino = new Treino(edtNomeTreino.getText().toString(), key, musculos.get(spnMusculo1.getSelectedItemPosition()).getId(), musculos.get(spnMusculo2.getSelectedItemPosition()).getId());
                    treinosRef.child(key).setValue(novoTreino);
                    finish();
                }
            }
        });
    }

    private void getMusculosDB(){
        musculosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Musculo novoMusculo = snapshot.getValue(Musculo.class);
                if(novoMusculo != null){
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

    private void getTreinosDB(){
        treinosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Treino novoTreino = snapshot.getValue(Treino.class);
                if(novoTreino != null){
                    treinos.add(novoTreino);
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
}