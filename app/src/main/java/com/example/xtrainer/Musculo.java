package com.example.xtrainer;

public class Musculo {
    private String nome;
    private String id;

    public Musculo() {
    }

    public Musculo(String nome, String id) {
        this.nome = nome;
        this.id = id;
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
}
