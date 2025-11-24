package com.example.lojabumi.produtos.tipo;

import com.example.lojabumi.produtos.Produto;

public class Frios extends Produto {

    public Frios(String nome, double preco){
        super(nome, preco);

    }

    @Override
    public double getPrecoFinal() {
        return getPreco() * 0.90;
    }

}
