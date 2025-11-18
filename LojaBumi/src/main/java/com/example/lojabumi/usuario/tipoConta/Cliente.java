package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Cliente extends Usuario {
    private Map<Produto, Integer> carrinho = new HashMap<>();

    public Cliente(int idUsuario, String nome, String dataNasc, String email, String senha) {
        super(idUsuario, nome, dataNasc, email, senha);
    }

    public Map<Produto, Integer> getCarrinho() {
        return carrinho;
    }

    public void addProduto(Produto produto) {
        int quantidadeCarrinho = carrinho.getOrDefault(produto, 0);

        if (quantidadeCarrinho >= Estoque.getEstoque(produto)) {
            System.out.println("Erro: Estoque insuficiente para o produto '" + produto.getNome() + "'.");
            return;
        }

        carrinho.put(produto, quantidadeCarrinho + 1);
        System.out.println("Produto '" + produto.getNome() + "' adicionado ao carrinho.");
    }

    public void verCarrinho() {
        System.out.println("\n--- Meu Carrinho ---");
        if (carrinho.isEmpty()) {
            System.out.println("O carrinho está vazio.");
        } else {
            double total = 0;
            for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                Produto produto = entry.getKey();
                int quantidade = entry.getValue();
                double subtotal = produto.getPreco() * quantidade;
                total += subtotal;
                System.out.println(produto.getNome() + " | Quantidade: " + quantidade + " | Subtotal: R$ " + String.format("%.2f", subtotal));
            }
            System.out.println("-------------------------------------------------");
            System.out.println("TOTAL DO CARRINHO: R$ " + String.format("%.2f", total));
        }
        System.out.println("--------------------\n");
    }

    @Override
    public boolean alterarEstoque() {
        return false;
    }

    @Override
    public boolean alterarPreco() {
        return false;
    }

    @Override
    public boolean addEstoque() {
        return false;
    }

    @Override
    public boolean removerProduto() {
        return false;
    }

}
