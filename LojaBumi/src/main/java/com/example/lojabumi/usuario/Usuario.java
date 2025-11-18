package com.example.lojabumi.usuario;

import com.example.lojabumi.usuario.Permissao;

public abstract class Usuario implements Permissao{
    protected int idUsuario;
    protected String nome;
    protected String dataNasc;
    protected String email;
    protected String senha;

    public Usuario(int idUsuario, String nome, String dataNasc, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
