package com.example.xtrainer;

public class Exercicio {
    private String nome;
    private String id;
    private String idMusculo;
    private int peso;
    private int repeticoes;

    public Exercicio(){}

    public Exercicio(String nome, String id, String idMusculo, int peso, int repeticoes) {
        this.nome = nome;
        this.id = id;
        this.idMusculo = idMusculo;
        this.peso = peso;
        this.repeticoes = repeticoes;
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

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }

    public String getIdMusculo() {
        return idMusculo;
    }

    public void setIdMusculo(String idMusculo) {
        this.idMusculo = idMusculo;
    }
}
