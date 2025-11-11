package com.example.lojabumi.usuario;

public abstract class Usuario {
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

    public abstract boolean isAdministrador();

    public boolean liberarAcesso() {
        if (!isAdministrador()) {
            System.err.println("Ta brokiado");
            return false;
        }
        return true;
    }
}
