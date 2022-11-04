package com.example.xtrainer;

import java.util.ArrayList;

public class Treino {
    private String nome;
    private String id;
    private String musculo1;
    private String musculo2;

    public Treino() {}

    public Treino(String nome, String id, String musculo1, String musculo2) {
        this.nome = nome;
        this.id = id;
        this.musculo1 = musculo1;
        this.musculo2 = musculo2;
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

    public String getMusculo1() {
        return musculo1;
    }

    public void setMusculo1(String musculo1) {
        this.musculo1 = musculo1;
    }

    public String getMusculo2() {
        return musculo2;
    }

    public void setMusculo2(String musculo2) {
        this.musculo2 = musculo2;
    }
}
