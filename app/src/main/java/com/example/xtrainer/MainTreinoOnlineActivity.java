package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainTreinoOnlineActivity extends AppCompatActivity {

    private TextView tvNomeExercicio, tvAquecimento, tvPeso, tvRepeticoes;
    private ImageButton btnPesoMenos, btnPesoMais, btnRepMenos, btnRepMais;
    private Button btnFeito;

    private ArrayList<Exercicio> exercicios = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference exerciciosFaltantesRef = database.getReference().child("treinoOnline").child("exerciciosFaltantes");
    private DatabaseReference exerciciosRef = database.getReference().child("exercicios");
    private DatabaseReference treinoOnlineRef = database.getReference().child("treinoOnline").child("treino");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_treino_online);

        tvNomeExercicio = findViewById(R.id.tvNomeExercicio);
        tvAquecimento = findViewById(R.id.tvAquecimento);
        tvPeso = findViewById(R.id.tvPeso);
        tvRepeticoes = findViewById(R.id.tvRepeticoes);
        btnPesoMenos = findViewById(R.id.btnPesoMenos);
        btnPesoMais = findViewById(R.id.btnPesoMais);
        btnRepMenos = findViewById(R.id.btnRepMenos);
        btnRepMais = findViewById(R.id.btnRepMais);
        btnFeito = findViewById(R.id.btnFeito);

        getExerciciosFaltantesDB();
        setOnClick();
    }

    private void setOnClick(){
        btnPesoMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercicios.get(0).setPeso(exercicios.get(0).getPeso() - 1);
                exerciciosRef.child(exercicios.get(0).getIdMusculo()).child(exercicios.get(0).getId()).child("peso").setValue(exercicios.get(0).getPeso());
                tvAquecimento.setText("Aquecimento: " + Math.ceil((float) exercicios.get(0).getPeso()/2) + " kgs");
                tvPeso.setText("Peso: " + (float)exercicios.get(0).getPeso() + " kgs");
            }
        });

        btnPesoMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercicios.get(0).setPeso(exercicios.get(0).getPeso() + 1);
                exerciciosRef.child(exercicios.get(0).getIdMusculo()).child(exercicios.get(0).getId()).child("peso").setValue(exercicios.get(0).getPeso());
                tvAquecimento.setText("Aquecimento: " + Math.ceil((float) exercicios.get(0).getPeso()/2) + " kgs");
                tvPeso.setText("Peso: " + (float)exercicios.get(0).getPeso() + " kgs");
            }
        });

        btnRepMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercicios.get(0).setRepeticoes(exercicios.get(0).getRepeticoes() - 1);
                exerciciosRef.child(exercicios.get(0).getIdMusculo()).child(exercicios.get(0).getId()).child("repeticoes").setValue(exercicios.get(0).getRepeticoes());
                tvRepeticoes.setText("Repetições: " + exercicios.get(0).getRepeticoes());
            }
        });

        btnRepMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercicios.get(0).setRepeticoes(exercicios.get(0).getRepeticoes() + 1);
                exerciciosRef.child(exercicios.get(0).getIdMusculo()).child(exercicios.get(0).getId()).child("repeticoes").setValue(exercicios.get(0).getRepeticoes());
                tvRepeticoes.setText("Repetições: " + exercicios.get(0).getRepeticoes());
            }
        });

        btnFeito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exercicios.size() > 0) {
                    exerciciosFaltantesRef.child(exercicios.get(0).getId()).removeValue();
                    exercicios.remove(0);

                    if (exercicios.size() > 0) {
                        tvNomeExercicio.setText(exercicios.get(0).getNome());
                        tvAquecimento.setText("Aquecimento: " + Math.ceil((float) exercicios.get(0).getPeso() / 2) + " kgs");
                        tvPeso.setText("Peso: " + (float) exercicios.get(0).getPeso() + " kgs");
                        tvRepeticoes.setText("Repetições: " + exercicios.get(0).getRepeticoes());
                    } else {
                        tvNomeExercicio.setText("Treino Terminado");
                        tvAquecimento.setText("");
                        tvPeso.setText("");
                        tvRepeticoes.setText("");
                        btnPesoMenos.setVisibility(View.INVISIBLE);
                        btnPesoMais.setVisibility(View.INVISIBLE);
                        btnRepMenos.setVisibility(View.INVISIBLE);
                        btnRepMais.setVisibility(View.INVISIBLE);
                        treinoOnlineRef.removeValue();
                    }
                }else{
                    finish();
                }
            }
        });
    }

    private void getExerciciosFaltantesDB(){
        exerciciosFaltantesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Exercicio novoExercicio = snapshot.getValue(Exercicio.class);
                if(novoExercicio != null){
                    exercicios.add(novoExercicio);
                    if(exercicios.size() == 1) {
                        tvNomeExercicio.setText(novoExercicio.getNome());
                        tvAquecimento.setText("Aquecimento: " + Math.ceil((float) novoExercicio.getPeso()/2) + " kgs");
                        tvPeso.setText("Peso: " + (float)novoExercicio.getPeso() + " kgs");
                        tvRepeticoes.setText("Repetições: " + novoExercicio.getRepeticoes());
                    }
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