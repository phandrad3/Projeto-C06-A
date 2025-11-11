package com.example.lojabumi.produtos;

import com.example.lojabumi.usuario.Usuario;
import java.util.HashMap;
import java.util.Map;

public class Estoque {
    private static final Map<Integer, Integer> estoque = new HashMap<>();
    private static final Map<Integer, Produto> produtosMap = new HashMap<>();

    public static void adicionarEstoque(Produto produto, int quantidade, Usuario usuario) {
        if (!usuario.liberarAcesso()) return;
        if (produto == null || quantidade <= 0) {
            System.err.println("Erro: Produto inválido ou quantidade não positiva.");
            return;
        }

        int idProduto = produto.getIdProduto();
        estoque.put(idProduto, estoque.getOrDefault(idProduto, 0) + quantidade);
        produtosMap.put(idProduto, produto);
        System.out.println("Estoque atualizado: " + quantidade + " unidade(s) do produto '" +
                produto.getNome() + "' (ID: " + idProduto + ") adicionadas.");
    }

    public static boolean atualizarEstoque(Produto produto, int novaQuantidade, Usuario usuario) {
        if (!usuario.liberarAcesso()) return false;
        if (produto == null || novaQuantidade < 0) {
            System.err.println("Erro: Produto inválido ou quantidade negativa.");
            return false;
        }

        int idProduto = produto.getIdProduto();
        estoque.put(idProduto, novaQuantidade);
        produtosMap.put(idProduto, produto);
        System.out.println("Estoque do produto '" + produto.getNome() + "' (ID: " + idProduto +
                ") atualizado para " + novaQuantidade + " unidade(s).");
        return true;
    }

    public static void removerProduto(Produto produto, Usuario usuario) {
        if (!usuario.liberarAcesso()) return;
        if (produto == null) return;

        int idProduto = produto.getIdProduto();
        if (estoque.containsKey(idProduto)) {
            estoque.remove(idProduto);
            produtosMap.remove(idProduto);
            System.out.println("Produto '" + produto.getNome() + "' removido do estoque.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public static void listarProdutosEmEstoque(Usuario usuario) {
        if (!usuario.liberarAcesso()) return;

        System.out.println("\n=== LISTA DE PRODUTOS EM ESTOQUE ===");
        if (estoque.isEmpty()) {
            System.out.println("Não há produtos em estoque.");
        } else {
            for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                int idProduto = entry.getKey();
                int quantidade = entry.getValue();
                Produto produto = produtosMap.get(idProduto);

                if (produto != null && quantidade > 0) {
                    System.out.println("ID: " + idProduto +
                            " | Nome: " + produto.getNome() +
                            " | Preço: R$ " + String.format("%.2f", produto.getPreco()) +
                            " | Quantidade: " + quantidade);
                }
            }
            System.out.println("====================================\n");
        }
    }

    public static int getEstoque(Produto produto) {
        if (produto == null) return 0;
        return estoque.getOrDefault(produto.getIdProduto(), 0);
    }

    public static boolean removerEstoque(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) return false;

        int idProduto = produto.getIdProduto();
        int estoqueAtual = getEstoque(produto);

        if (estoqueAtual >= quantidade) {
            estoque.put(idProduto, estoqueAtual - quantidade);
            return true;
        }
        return false;
    }

    public static Produto buscarProdutoPorNome(String nome) {
        for (Produto produto : produtosMap.values()) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                return produto;
            }
        }
        System.out.println("Produto '" + nome + "' não encontrado no estoque.");
        return null;
    }
}
