package com.example.lojabumi;

import com.example.lojabumi.produtos.*;
import com.example.lojabumi.usuario.tipoConta.*;
import com.example.lojabumi.utilitarios.Compra;

public class Main {
    public static void main(String[] args) {

        Administrador adm = new Administrador(1, "Carlos", "10/10/1985", "carlos@admin.com", "admin123");
        Cliente cliente1 = new Cliente(101, "João", "01/01/1990", "joao@email.com", "12345");

        Produto notebook = new Produto("Notebook Pro", 3500.00) {};
        Produto mouse = new Produto("Mouse Wireless", 150.00) {};

        System.out.println("\n=== AÇÕES DO ADMINISTRADOR ===");
        Estoque.adicionarEstoque(notebook, 5, adm);
        Estoque.adicionarEstoque(mouse, 10, adm);
        Estoque.atualizarEstoque(notebook, 8, adm);

        System.out.println("\n=== AÇÕES DO CLIENTE ===");
        Estoque.adicionarEstoque(mouse, 5, cliente1);
        Estoque.removerProduto(notebook, cliente1);

        cliente1.addProduto(notebook);
        cliente1.addProduto(mouse);
        cliente1.addProduto(notebook);

        cliente1.verCarrinho();

        Estoque.exibirEstoque();
        Compra.finalizarCompra(cliente1.getCarrinho());
        Estoque.exibirEstoque();

        System.out.println("\n=== ESTOQUE FINAL (VISÃO DO ADMIN) ===");
    }
}
