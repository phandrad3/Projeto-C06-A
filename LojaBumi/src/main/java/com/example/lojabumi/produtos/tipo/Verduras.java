package com.example.lojabumi.produtos.tipo;

import com.example.lojabumi.produtos.Produto;

public class Verduras extends Produto {

    public Verduras(String nome, double preco) {
        super(nome, preco);

    }

    @Override
    public double getPrecoFinal() {
        return getPreco() * 0.50;
    }

    }
