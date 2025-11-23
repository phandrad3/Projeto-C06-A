package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.usuario.Usuario;

public class Sistema extends Usuario {

    public Sistema(int idUsuario, String nome, String dataNasc, String email, String senha) {
        super(idUsuario, nome, dataNasc, email, senha);
    }

    @Override
    public boolean alterarEstoque() {
        return true;
    }

    @Override
    public boolean alterarPreco() {
        return true;
    }

    @Override
    public boolean addEstoque() {
        return true;
    }

    @Override
    public boolean removerProduto() {
        return true;
    }
}