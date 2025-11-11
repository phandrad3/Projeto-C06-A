package com.example.lojabumi;

import com.example.lojabumi.produtos.*;
import com.example.lojabumi.usuario.tipoConta.*;

public class Main {
    public static void main(String[] args) {

        Administrador adm = new Administrador(1, "Carlos", "10/10/1985", "carlos@admin.com", "admin123");
        Cliente cliente1 = new Cliente(101, "João", "01/01/1990", "joao@email.com", "12345");

        Produto notebook = new Produto(1, "Notebook Pro", 3500.00) {};
        Produto mouse = new Produto(2, "Mouse Wireless", 150.00) {};

        System.out.println("\n=== AÇÕES DO ADMINISTRADOR ===");
        Estoque.adicionarEstoque(notebook, 5, adm);
        Estoque.adicionarEstoque(mouse, 10, adm);
        Estoque.atualizarEstoque(notebook, 8, adm);
        notebook.atualizarPreco(3800.00, adm);
        Estoque.listarEstoque(adm);

        System.out.println("\n=== AÇÕES DO CLIENTE ===");
        Estoque.adicionarEstoque(mouse, 5, cliente1);
        Estoque.removerProduto(notebook, cliente1);
        notebook.atualizarPreco(5000.00, cliente1);

        cliente1.addProduto(notebook);
        cliente1.addProduto(mouse);
        cliente1.addProduto(notebook);

        cliente1.verCarrinho();

        System.out.println("\n=== ESTOQUE FINAL (VISÃO DO ADMIN) ===");
        Estoque.listarEstoque(adm);
    }
}
