package com.example.lojabumi.produtos;

public abstract class Produto {
    private static int nextId = 1;
    private int id;
    private String nome;
    private double preco;

    public Produto(String nome, double preco) {
        this.id = nextId++;
        this.nome = nome;
        this.preco = preco;

    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String toString() {return getId() +" " +getNome(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Produto produto = (Produto) o;
        return id == produto.id;
    }

    public abstract double getPrecoFinal();

    @Override
    public int hashCode() {
        return id;
    }
}