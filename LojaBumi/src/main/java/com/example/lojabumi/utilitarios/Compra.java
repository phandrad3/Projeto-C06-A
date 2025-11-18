package com.example.lojabumi.utilitarios;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Permissao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Compra implements Permissao {

    private static final Compra compra = new Compra();

    private Compra() {}

    @Override
    public boolean alterarEstoque() {
        return true;
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

    public static double calcularTotal(Map<Produto, Integer> carrinho) {
        double total = 0.0;

        for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();
            total += produto.getPreco() * quantidade;
        }

        return total;
    }

    public static boolean finalizarCompra(Map<Produto, Integer> carrinho) {
        List<Produto> produtosInsuficientes = new ArrayList<>();

        for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
            Produto produto = entry.getKey();
            int quantidadeCarrinho = entry.getValue();
            int quantidadeEstoque = Estoque.getEstoque(produto);

            if (quantidadeCarrinho > quantidadeEstoque) {
                produtosInsuficientes.add(produto);
            }
        }

        if (!produtosInsuficientes.isEmpty()) {
            System.out.println("\n--- ERRO NA FINALIZAÇÃO DA COMPRA ---");
            System.out.println("Não foi possível finalizar a compra devido a estoque insuficiente para os seguintes produtos:");

            for (Produto produto : produtosInsuficientes) {
                int quantidadeCarrinho = carrinho.get(produto);
                int quantidadeEstoque = Estoque.getEstoque(produto);
                System.out.println("- " + produto.getNome() +
                        ": necessário " + quantidadeCarrinho +
                        ", disponível " + quantidadeEstoque);
            }

            System.out.println("Nenhum produto foi removido do estoque.");
            System.out.println("-------------------------------------\n");
            return false;
        }

        for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
            Produto produto = entry.getKey();
            int quantidade = entry.getValue();

            Estoque.removerEstoque(produto, quantidade, compra);
        }

        System.out.println("\n--- COMPRA FINALIZADA COM SUCESSO ---\n");
        return true;
    }

}
