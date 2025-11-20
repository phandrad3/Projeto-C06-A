package com.example.lojabumi;

import com.example.lojabumi.usuario.Usuario;

public class Main {
    public static void main(String[] args) {
        UserDatabase.carregarUsuariosDoBanco();
        com.example.lojabumi.HelloApplication.main(args);
    }
}