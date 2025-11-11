package com.example.lojabumi.produtos;

public abstract class Produto {
    // atributos

    protected int idProduto;
    protected String nome;
    protected double preco;

    // métodos

    public Produto(int idProduto, String nome, double preco){
        this.idProduto = idProduto;
        this.nome = nome;
        this.preco = preco;
    }

    public void atualizarPreco(double novoPreco) {
        if (novoPreco <= 0) {
            System.out.println("Preço inválido!");
        } else {
            this.preco = novoPreco;
            System.out.println("Preço do produto '" + nome + "' atualizado para R$ " + String.format("%.2f", preco));
        }
    }

    // getters e setters

    public int getIdProduto() {
        return idProduto;
    }
    public String getNome() {
        return nome;
    }
    public double getPreco() {
        return preco;
    }
}
