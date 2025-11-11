package com.example.lojabumi;

import com.example.lojabumi.produtos.*;
import com.example.lojabumi.produtos.tipo.Eletronico;
import com.example.lojabumi.usuario.tipoConta.*;

public class Main {
    public static void main(String[] args) {

        // ==== Criando produtos ====
        Produto notebook = new Eletronico(1, "Notebook Pro", 3500.00);
        Produto mouse = new Eletronico(2, "Mouse Wireless", 150.00);

        // ==== Criando usuários ====
        Administrador adm = new Administrador(1, "Maria", "05/02/1995", "maria@adm.com", "admin123");
        Cliente cliente1 = new Cliente(101, "João", "01/01/1990", "joao@email.com", "12345");

        // ==== Ações do administrador ====
        System.out.println("\n-- Ações do Administrador --");
        adm.adicionarProdutoAoEstoque(notebook, 5);
        adm.adicionarProdutoAoEstoque(mouse, 10);
        adm.listarEstoque();

        adm.alterarPrecoProduto(mouse, 180.00);
        adm.atualizarQuantidadeEstoque(notebook, 8);
        adm.listarEstoque();

        // ==== Ações do cliente ====
        System.out.println("\n-- Ações do Cliente --");
        cliente1.addProduto(notebook);
        cliente1.addProduto(mouse);
        cliente1.addProduto(notebook);
        cliente1.verCarrinho();

        // ==== Tentando ações restritas ao administrador ====
        System.out.println("\n-- Cliente tentando mexer no estoque (deve falhar) --");
        Estoque.adicionarEstoque(mouse, 5, cliente1); // deve exibir "Ta brokiado"
        Estoque.listarEstoque(cliente1);    // idem
    }
}
