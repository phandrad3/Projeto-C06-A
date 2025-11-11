package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.usuario.Usuario;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nome, String dataNasc, String email, String senha) {
        super(idUsuario, nome, dataNasc, email, senha);
    }

    @Override
    public boolean isAdministrador() {
        return true;
    }
}
