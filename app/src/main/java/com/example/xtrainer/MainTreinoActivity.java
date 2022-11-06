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
import android.widget.Button;
import android.widget.TextView;

import com.example.xtrainer.databinding.ActivityMainBinding;
import com.example.xtrainer.databinding.ActivityMainTreinoBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainTreinoActivity extends AppCompatActivity {

    private TextView tvNomeTreino;
    private TextView tvMusculo1;
    private TextView tvMusculo2;
    private Button btnIniciarTreino;

    private Treino treino;
    private Musculo musculo1, musculo2;
    private ArrayList<Exercicio> exercicios1 = new ArrayList<>();
    private ArrayList<Exercicio> exercicios2 = new ArrayList<>();
    private ArrayAdapter listAdapterExercicios1, listAdapterExercicios2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference treinoAtualRef = database.getReference().child("treinoAtual");
    private DatabaseReference treinoOnlineRef = database.getReference().child("treinoOnline").child("treino");
    private DatabaseReference exerciciosFaltantesRef = database.getReference().child("treinoOnline").child("exerciciosFaltantes");
    private DatabaseReference musculosRef = database.getReference().child("musculos");
    private DatabaseReference exerciciosRef = database.getReference().child("exercicios");

    ActivityMainTreinoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainTreinoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvNomeTreino = findViewById(R.id.tvNomeTreino);
        tvMusculo1 = findViewById(R.id.tvMusculo1);
        tvMusculo2 = findViewById(R.id.tvMusculo2);
        btnIniciarTreino = findViewById(R.id.btnIniciarTreino);

        getTreinoOnlineDB();
        setOnClick();
    }

    private void setOnClick(){
        btnIniciarTreino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                treinoOnlineRef.setValue(treino);
                exerciciosFaltantesRef.removeValue();
                for(Exercicio exercicio : exercicios1){
                    exerciciosFaltantesRef.child(exercicio.getId()).setValue(exercicio);
                }
                for(Exercicio exercicio : exercicios2){
                    exerciciosFaltantesRef.child(exercicio.getId()).setValue(exercicio);
                }
                startActivity(new Intent(MainTreinoActivity.this, MainTreinoOnlineActivity.class));
            }
        });
    }

    private void getTreinoOnlineDB(){
        treinoAtualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                treino = snapshot.getValue(Treino.class);
                tvNomeTreino.setText(treino.getNome());

                getMusculosDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMusculosDB(){
        musculosRef.child(treino.getMusculo1()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                musculo1 = snapshot.getValue(Musculo.class);
                if(tvMusculo1 != null) {
                    tvMusculo1.setText(musculo1.getNome());

                    listAdapterExercicios1 = new TreinoExerciciosListAdapter(MainTreinoActivity.this, exercicios1);
                    binding.lvExercicios1.setAdapter(listAdapterExercicios1);
                    exerciciosRef.child(musculo1.getId()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Exercicio novoExercicio = snapshot.getValue(Exercicio.class);
                            if (novoExercicio != null) {
                                exercicios1.add(novoExercicio);
                                listAdapterExercicios1.notifyDataSetChanged();
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainTreinoActivity.this);

                    builder.setTitle("Músculo não encontrado!")
                            .setMessage("O músculo 1 do treino não foi encontrado. Crie o treino novamente.")
                            .setPositiveButton("Ok", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        musculosRef.child(treino.getMusculo2()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                musculo2 = snapshot.getValue(Musculo.class);
                if(musculo2 != null) {
                    tvMusculo2.setText(musculo2.getNome());

                    listAdapterExercicios2 = new TreinoExerciciosListAdapter(MainTreinoActivity.this, exercicios2);
                    binding.lvExercicios2.setAdapter(listAdapterExercicios2);
                    exerciciosRef.child(musculo2.getId()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Exercicio novoExercicio = snapshot.getValue(Exercicio.class);
                            if (novoExercicio != null) {
                                exercicios2.add(novoExercicio);
                                listAdapterExercicios2.notifyDataSetChanged();
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
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainTreinoActivity.this);

                    builder.setTitle("Músculo não encontrado!")
                            .setMessage("O músculo 2 do treino não foi encontrado. Crie o treino novamente.")
                            .setPositiveButton("Ok", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}