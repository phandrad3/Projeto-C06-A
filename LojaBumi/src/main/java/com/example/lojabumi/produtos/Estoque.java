package com.example.lojabumi.produtos;
import com.example.lojabumi.usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Estoque {
    private static Map<Integer, Integer> estoque = new HashMap<>();
    private static Map<Integer, Produto> produtos = new HashMap<>();

    public static boolean adicionarEstoque(Produto produto, int quantidade, Usuario usuario) {


        if (produto == null || quantidade <= 0) {
            System.err.println("Erro: Produto inválido ou quantidade não positiva.");
            return false;
        }

        int idProduto = produto.getId();
        estoque.put(idProduto, estoque.getOrDefault(idProduto, 0) + quantidade);
        produtos.put(idProduto, produto);

        System.out.println("Estoque atualizado: " + quantidade + " unidade(s) do produto '" +
                produto.getNome() + "' (ID: " + idProduto + ") adicionadas.");

        return true;
    }

    public static boolean atualizarEstoque(Produto produto, int novaQuantidade, Usuario usuario) {
        if (!usuario.liberarAcesso()) return false;

        if (produto == null || novaQuantidade < 0) {
            System.err.println("Erro: Produto inválido ou quantidade negativa.");
            return false;
        }

        int idProduto = produto.getId();
        estoque.put(idProduto, novaQuantidade);
        produtos.put(idProduto, produto);
        System.out.println("Estoque do produto '" + produto.getNome() + "' (ID: " + idProduto +
                ") atualizado para " + novaQuantidade + " unidade(s).");

        return true;
    }

    public static boolean atualizarValor(Produto produto, double novoValor, Usuario usuario) {
        if (!usuario.liberarAcesso()) return false;

        if (produto == null || novoValor <= 0) {
            System.err.println("Erro: Produto inválido ou valor inválido.");
            return false;
        }

        int idProduto = produto.getId();

        produto.setPreco(novoValor);

        System.out.println("Valor do produto '" + produto.getNome() + "' (ID: " + idProduto +
                ") atualizado para R$ " + novoValor);

        return true;
    }

    public static int getEstoque(Produto produto) {
        if (produto == null) return 0;

        return estoque.getOrDefault(produto.getId(), 0);
    }

    public static boolean removerEstoque(Produto produto, int quantidade, Usuario usuario) {
        if (!usuario.liberarAcesso()) return false;

        if (produto == null || quantidade <= 0) return false;

        int idProduto = produto.getId();
        int estoqueAtual = getEstoque(produto);

        if (estoqueAtual >= quantidade) {
            estoque.put(idProduto, estoqueAtual - quantidade);
            return true;
        }
        return false;
    }

    public static boolean removerProduto(Produto produto, Usuario usuario) {
        if (!usuario.liberarAcesso()) return false;

        if (produto == null) return false;

        int idProduto = produto.getId();
        if (estoque.containsKey(idProduto)) {
            estoque.remove(idProduto);
            produtos.remove(idProduto);
            System.out.println("Produto '" + produto.getNome() + "' removido do estoque.");
        } else {
            System.out.println("Produto não encontrado.");
        }

        return true;
    }

    public static void exibirEstoque() {
        System.out.println("\n--- ESTOQUE COMPLETO ---");
        if (estoque.isEmpty()) {
            System.out.println("O estoque está vazio.");
        } else {
            System.out.println("ID | Nome | Preço | Quantidade");
            System.out.println("---------------------------------");
            for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                int idProduto = entry.getKey();
                int quantidade = entry.getValue();
                Produto produto = produtos.get(idProduto);

                if (produto != null) {
                    System.out.println(idProduto + " | " + produto.getNome() + " | R$ " +
                            String.format("%.2f", produto.getPreco()) + " | " + quantidade);
                }
            }
        }
        System.out.println("------------------------\n");
    }

    public static Map<Integer, Produto> getProdutos() {
        return produtos;
    }
}
