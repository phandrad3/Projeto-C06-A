package com.example.lojabumi.produtos;

public class Produtos {
    protected int idProduto;
    protected String nome;
    protected  int quantidade;
    protected double preco;
    protected String fornecedor;

    public Produtos(int idProduto, String nome, int quantidade, double preco){
        this.idProduto = idProduto;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }

}
