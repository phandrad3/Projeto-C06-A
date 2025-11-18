package com.example.lojabumi;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.produtos.tipo.Eletronico;
import com.example.lojabumi.produtos.tipo.Frios;
import com.example.lojabumi.usuario.tipoConta.Administrador;
import com.example.lojabumi.usuario.tipoConta.Cliente;

public class Main {
    public static void main(String[] args) {

        Administrador adm = new Administrador(1, "Carlos", "10/10/1985", "carlos@admin.com", "admin123");
        Cliente cliente1 = new Cliente(101, "João", "01/01/1990", "joao@email.com", "12345");



        UserDatabase.adicionarUsuario(adm);
        UserDatabase.adicionarUsuario(cliente1);

        com.example.lojabumi.HelloApplication.main(args);
    }
}
