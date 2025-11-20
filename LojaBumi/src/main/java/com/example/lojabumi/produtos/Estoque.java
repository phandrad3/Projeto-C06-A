package com.example.lojabumi.produtos;

import com.example.lojabumi.usuario.Permissao;
import com.example.lojabumi.usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Estoque implements Runnable {
    private static Map<Integer, Integer> estoque = new HashMap<>();
    private static Map<Integer, Produto> produtos = new HashMap<>();

    // Atributos para o monitoramento
    private static final int LIMITE_CRITICO = 5;
    private static final int INTERVALO_VERIFICACAO = 60000; // 60 segundos
    private static Thread threadMonitor;
    private static boolean executando = false;

    public static void iniciarMonitoramento() {
        if (threadMonitor == null || !threadMonitor.isAlive()) { // verifica se uma thread está em execução
            executando = true;
            threadMonitor = new Thread(new Estoque());
            threadMonitor.setDaemon(true); // quando o programa principal terminar, a thread será automaticamente interrompida
            threadMonitor.start();
        }
    }

    @Override
    public void run() {
        while (executando) {
            try {
                verificarEstoqueCritico();
                Thread.sleep(INTERVALO_VERIFICACAO); // pausa a execução da thread atual por um número especificado
            } catch (InterruptedException e) {
                System.out.println("Monitor de estoque interrompido.");
                executando = false;
            }
        }
    }

    private static void verificarEstoqueCritico() {
        boolean temEstoqueCritico = false;

        for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
            int idProduto = entry.getKey();
            int quantidade = entry.getValue();
            Produto produto = produtos.get(idProduto);

            if (produto != null && quantidade < LIMITE_CRITICO) {
                temEstoqueCritico = true;
                System.out.println("ALERTA: Produto '" + produto.getNome() +
                        "' (ID: " + idProduto + ") com estoque baixo: " + quantidade + " unidades.");
            }
        }

        if (!temEstoqueCritico) {
            System.out.println("Todos os produtos com estoque adequado.");
        }
    }

    public static void pararMonitoramento() {
        executando = false;
        if (threadMonitor != null) {
            threadMonitor.interrupt();
            System.out.println("Monitor de estoque finalizado.");
        }
    }

    public static boolean adicionarEstoque(Produto produto, int quantidade, Permissao usuario) {
        if (!usuario.addEstoque()) return false;

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

    public static boolean atualizarEstoque(Produto produto, int novaQuantidade, Permissao usuario) {
        if (!usuario.alterarEstoque()) return false;

        if (produto == null || novaQuantidade < 0) {
            System.err.println("Erro: Produto inválido ou quantidade negativa.");
            return false;
        }

        int idProduto = produto.getId();
        estoque.put(idProduto, novaQuantidade);
        produtos.put(idProduto, produto);

        System.out.println("Quantidade atualizada do produto '" + produto.getNome() + "' para " + novaQuantidade);
        return true;
    }


    public static boolean atualizarValor(Produto produto, double novoValor, Usuario usuario) {
        if (!usuario.alterarPreco()) return false;

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

    public static boolean removerEstoque(Produto produto, int quantidade, Permissao usuario) {
        if (!usuario.alterarEstoque()) return false;

        if (produto == null || quantidade <= 0) return false;

        int idProduto = produto.getId();
        int estoqueAtual = getEstoque(produto);

        if (estoqueAtual < quantidade) return false;

        estoque.put(idProduto, estoqueAtual - quantidade);
        return true;
    }

    public static boolean removerProduto(Produto produto, Permissao usuario) {
        if (!usuario.removerProduto()) return false;

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
            System.out.println("--------------------------------");

            for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                int id = entry.getKey();
                int quantidade = entry.getValue();
                Produto produto = produtos.get(id);

                if (produto != null) {
                    System.out.println(id + " | " + produto.getNome() + " | R$ " +
                            String.format("%.2f", produto.getPreco()) +
                            " | " + quantidade);
                }
            }
        }
        System.out.println("--------------------------\n");
    }

    public static Map<Integer, Produto> getProdutos() {
        return produtos;
    }

    public static int getQuantidade(int idProduto) {
        return estoque.getOrDefault(idProduto, 0);
    }
}