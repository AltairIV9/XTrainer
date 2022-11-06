package com.example.xtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigActivity extends AppCompatActivity {

    private Button btnConfigMusculos, btnConfigExercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        btnConfigMusculos = findViewById(R.id.btnConfigMusculos);
        btnConfigExercicios = findViewById(R.id.btnConfigExercicios);

        setButtons();
    }

    private void setButtons(){
        btnConfigMusculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigActivity.this, ConfigMusculosActivity.class));
            }
        });

        btnConfigExercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigActivity.this, ConfigExerciciosActivity.class));
            }
        });
    }
}
