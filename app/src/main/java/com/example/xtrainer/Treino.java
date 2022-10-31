package com.example.xtrainer;

import java.util.ArrayList;

public class Treino {
    private String nome;
    private String id;
    private ArrayList<String> musculos;

    public Treino() {}

    public Treino(String nome, String id, ArrayList<String> musculos) {
        this.nome = nome;
        this.id = id;
        this.musculos = musculos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getMusculos() {
        return musculos;
    }

    public void setMusculos(ArrayList<String> musculos) {
        this.musculos = musculos;
    }
}
