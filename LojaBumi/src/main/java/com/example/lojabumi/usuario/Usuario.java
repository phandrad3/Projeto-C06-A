package com.example.lojabumi.usuario;


public abstract class Usuario implements Permissao {
    private int  idUsuario;
    private String nome;
    private String dataNasc;
    private String email;
    private String senha;



    public Usuario(int idUsuario, String nome, String dataNasc, String email, String senha) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }


}