package com.example.lojabumi.produtos;

import com.example.lojabumi.usuario.Usuario;

public abstract class Produto {
    protected int idProduto;
    protected String nome;
    protected double preco;

    public Produto(int idProduto, String nome, double preco) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.preco = preco;
    }

    public void atualizarPreco(double novoPreco, Usuario usuario) {
        if (!usuario.liberarAcesso()) return;

        if (novoPreco <= 0) {
            System.out.println("Preço inválido!");
        } else {
            this.preco = novoPreco;
            System.out.println("Preço do produto '" + nome + "' atualizado para R$ " + String.format("%.2f", preco));
        }
    }

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
