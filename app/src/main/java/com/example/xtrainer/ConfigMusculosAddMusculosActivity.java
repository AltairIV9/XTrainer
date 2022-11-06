package com.example.xtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ConfigMusculosAddMusculosActivity extends AppCompatActivity {

    private Button btnAddMusculo;
    private EditText edtNomeMusculo;

    private ArrayList<Musculo> musculos = new ArrayList<>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference musculosRef = database.getReference().child("musculos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_musculos_add_musculos);

        btnAddMusculo = findViewById(R.id.btnAddMusculo);
        edtNomeMusculo = findViewById(R.id.edtNomeMusculo);
        edtNomeMusculo.requestFocus();

        getMusculosDB();
        setButtons();
    }

    private void getMusculosDB(){
        musculosRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Musculo novoMusculo = snapshot.getValue(Musculo.class);
                if(novoMusculo != null) {
                    musculos.add(novoMusculo);
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
        btnAddMusculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musculoExists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfigMusculosAddMusculosActivity.this);

                    builder.setTitle("Músculo já existe!")
                            .setMessage("Escolha outro nome.")
                            .setPositiveButton("Ok", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if(!edtNomeMusculo.getText().toString().isEmpty()){
                    String key = musculosRef.push().getKey();
                    Musculo novoMusculo = new Musculo(edtNomeMusculo.getText().toString(), key);

                    musculosRef.child(key).setValue(novoMusculo);

                    finish();
                }
            }
        });
    }

    private boolean musculoExists(){
        for(int i = 0; i < musculos.size(); i++){
            if(musculos.get(i).getNome().equals(edtNomeMusculo.getText().toString())){
                return true;
            }
        }
        return false;
    }
}