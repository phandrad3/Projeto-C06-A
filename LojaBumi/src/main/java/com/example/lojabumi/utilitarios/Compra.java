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

    public static Compra getInstance() {
        return compra;
    }

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
        try {
            if (carrinho == null) {
                throw new IllegalArgumentException("Carrinho não pode ser nulo");
            }

            double total = 0.0;

            for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                try {
                    Produto produto = entry.getKey();
                    int quantidade = entry.getValue();

                    if (produto == null) {
                        throw new IllegalStateException("Produto nulo encontrado no carrinho");
                    }

                    total += produto.getPreco() * quantidade;
                } catch (IllegalStateException e) {
                    throw e; // Repassa exceções já tratadas
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao processar item do carrinho", e);
                }
            }

            return total;
        } catch (IllegalArgumentException e) {
            throw e; // Repassa exceções já tratadas
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular total da compra", e);
        }
    }

    public static boolean finalizarCompra(Map<Produto, Integer> carrinho) {
        try {
            if (carrinho == null) {
                throw new IllegalArgumentException("Carrinho não pode ser nulo");
            }

            List<Produto> produtosInsuficientes = new ArrayList<>();

            // Verificar estoque disponível
            for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                try {
                    Produto produto = entry.getKey();
                    int quantidadeCarrinho = entry.getValue();

                    if (produto == null) {
                        throw new IllegalStateException("Produto nulo encontrado no carrinho");
                    }

                    int quantidadeEstoque = Estoque.getEstoque(produto);

                    if (quantidadeCarrinho > quantidadeEstoque) {
                        produtosInsuficientes.add(produto);
                    }
                } catch (IllegalStateException e) {
                    throw e; // Repassa exceções já tratadas
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao verificar estoque do produto", e);
                }
            }

            if (!produtosInsuficientes.isEmpty()) {
                System.out.println("\n--- ERRO NA FINALIZAÇÃO DA COMPRA ---");
                System.out.println("Não foi possível finalizar a compra devido a estoque insuficiente para os seguintes produtos:");

                for (Produto produto : produtosInsuficientes) {
                    try {
                        int quantidadeCarrinho = carrinho.get(produto);
                        int quantidadeEstoque = Estoque.getEstoque(produto);
                        System.out.println("- " + produto.getNome() +
                                ": necessário " + quantidadeCarrinho +
                                ", disponível " + quantidadeEstoque);
                    } catch (NullPointerException e) {
                        throw new IllegalStateException("Erro ao exibir detalhes do produto", e);
                    }
                }

                System.out.println("Nenhum produto foi removido do estoque.");
                System.out.println("-------------------------------------\n");
                return false;
            }

            // Processar a compra
            boolean compraSucesso = true;
            for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                try {
                    Produto produto = entry.getKey();
                    int quantidade = entry.getValue();

                    if (produto == null) {
                        throw new IllegalStateException("Produto nulo encontrado no carrinho");
                    }

                    if (!Estoque.removerEstoque(produto, quantidade, compra)) {
                        throw new IllegalStateException("Erro ao remover produto do estoque: " + produto.getNome());
                    }
                } catch (IllegalStateException e) {
                    throw e; // Repassa exceções já tratadas
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao processar item da compra", e);
                }
            }

            if (compraSucesso) {
                System.out.println("\n--- COMPRA FINALIZADA COM SUCESSO ---\n");
                return true;
            } else {
                System.out.println("\n--- ERRO AO FINALIZAR COMPRA ---");
                System.out.println("Ocorreram erros durante o processamento da compra.");
                System.out.println("----------------------------------\n");
                return false;
            }
        } catch (IllegalArgumentException e) {
            throw e; // Repassa exceções já tratadas
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao finalizar compra", e);
        }
    }
}