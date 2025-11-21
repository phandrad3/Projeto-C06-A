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
        try {
            if (threadMonitor == null || !threadMonitor.isAlive()) {
                executando = true;
                threadMonitor = new Thread(new Estoque());
                threadMonitor.setDaemon(true);
                threadMonitor.start();
                System.out.println("Monitor de estoque iniciado.");
            }
        } catch (SecurityException e) {
            System.err.println("Erro de segurança ao iniciar monitoramento: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao iniciar monitoramento: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (executando) {
                verificarEstoqueCritico();
                Thread.sleep(INTERVALO_VERIFICACAO);
            }
        } catch (InterruptedException e) {
            System.out.println("Monitor de estoque interrompido.");
            executando = false;
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Erro no monitoramento de estoque: " + e.getMessage());
            executando = false;
        }
    }

    private static void verificarEstoqueCritico() {
        try {
            boolean temEstoqueCritico = false;

            for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                try {
                    int idProduto = entry.getKey();
                    int quantidade = entry.getValue();
                    Produto produto = produtos.get(idProduto);

                    if (produto != null && quantidade < LIMITE_CRITICO) {
                        temEstoqueCritico = true;
                        System.out.println("ALERTA: Produto '" + produto.getNome() +
                                "' (ID: " + idProduto + ") com estoque baixo: " + quantidade + " unidades.");
                    }
                } catch (NullPointerException e) {
                    System.err.println("Erro ao verificar produto: " + e.getMessage());
                }
            }

            if (!temEstoqueCritico) {
                System.out.println("Todos os produtos com estoque adequado.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao verificar estoque crítico: " + e.getMessage());
        }
    }

    public static void pararMonitoramento() {
        try {
            executando = false;
            if (threadMonitor != null) {
                threadMonitor.interrupt();
                System.out.println("Monitor de estoque finalizado.");
            }
        } catch (SecurityException e) {
            System.err.println("Erro de segurança ao parar monitoramento: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao parar monitoramento: " + e.getMessage());
        }
    }

    public static boolean adicionarEstoque(Produto produto, int quantidade, Permissao usuario) {
        try {
            if (!usuario.addEstoque()) {
                System.err.println("Erro: Usuário não tem permissão para adicionar estoque");
                return false;
            }

            if (produto == null) {
                System.err.println("Erro: Produto não pode ser nulo");
                return false;
            }

            if (quantidade <= 0) {
                System.err.println("Erro: Quantidade deve ser positiva");
                return false;
            }

            int idProduto = produto.getId();
            estoque.put(idProduto, estoque.getOrDefault(idProduto, 0) + quantidade);
            produtos.put(idProduto, produto);

            System.out.println("Estoque atualizado: " + quantidade + " unidade(s) do produto '" +
                    produto.getNome() + "' (ID: " + idProduto + ") adicionadas.");

            return true;
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao adicionar estoque: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao adicionar estoque: " + e.getMessage());
            return false;
        }
    }

    public static boolean atualizarEstoque(Produto produto, int novaQuantidade, Permissao usuario) {
        try {
            if (!usuario.alterarEstoque()) {
                System.err.println("Erro: Usuário não tem permissão para atualizar estoque");
                return false;
            }

            if (produto == null) {
                System.err.println("Erro: Produto não pode ser nulo");
                return false;
            }

            if (novaQuantidade < 0) {
                System.err.println("Erro: Quantidade não pode ser negativa");
                return false;
            }

            int idProduto = produto.getId();
            estoque.put(idProduto, novaQuantidade);
            produtos.put(idProduto, produto);

            System.out.println("Quantidade atualizada do produto '" + produto.getNome() + "' para " + novaQuantidade);
            return true;
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao atualizar estoque: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    public static boolean atualizarValor(Produto produto, double novoValor, Usuario usuario) {
        try {
            if (!usuario.alterarPreco()) {
                System.err.println("Erro: Usuário não tem permissão para atualizar valor");
                return false;
            }

            if (produto == null) {
                System.err.println("Erro: Produto não pode ser nulo");
                return false;
            }

            if (novoValor <= 0) {
                System.err.println("Erro: Valor deve ser positivo");
                return false;
            }

            int idProduto = produto.getId();
            produto.setPreco(novoValor);

            System.out.println("Valor do produto '" + produto.getNome() + "' (ID: " + idProduto +
                    ") atualizado para R$ " + novoValor);

            return true;
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao atualizar valor: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar valor: " + e.getMessage());
            return false;
        }
    }

    public static int getEstoque(Produto produto) {
        try {
            if (produto == null) {
                System.err.println("Erro: Produto não pode ser nulo");
                return 0;
            }
            return estoque.getOrDefault(produto.getId(), 0);
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao consultar estoque: " + e.getMessage());
            return 0;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao consultar estoque: " + e.getMessage());
            return 0;
        }
    }

    public static boolean removerEstoque(Produto produto, int quantidade, Permissao usuario) {
        try {
            if (!usuario.alterarEstoque()) {
                System.err.println("Erro: Usuário não tem permissão para remover estoque");
                return false;
            }

            if (produto == null) {
                System.err.println("Erro: Produto não pode ser nulo");
                return false;
            }

            if (quantidade <= 0) {
                System.err.println("Erro: Quantidade deve ser positiva");
                return false;
            }

            int idProduto = produto.getId();
            int estoqueAtual = getEstoque(produto);

            if (estoqueAtual < quantidade) {
                System.err.println("Erro: Estoque insuficiente. Disponível: " + estoqueAtual + ", solicitado: " + quantidade);
                return false;
            }

            estoque.put(idProduto, estoqueAtual - quantidade);
            return true;
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao remover estoque: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao remover estoque: " + e.getMessage());
            return false;
        }
    }

    public static boolean removerProduto(Produto produto, Permissao usuario) {
        try {
            if (!usuario.removerProduto()) {
                System.err.println("Erro: Usuário não tem permissão para remover produto");
                return false;
            }

            if (produto == null) {
                System.err.println("Erro: Produto não pode ser nulo");
                return false;
            }

            int idProduto = produto.getId();

            if (estoque.containsKey(idProduto)) {
                estoque.remove(idProduto);
                produtos.remove(idProduto);
                System.out.println("Produto '" + produto.getNome() + "' removido do estoque.");
            } else {
                System.err.println("Erro: Produto não encontrado no estoque");
                return false;
            }

            return true;
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao remover produto: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao remover produto: " + e.getMessage());
            return false;
        }
    }

    public static void exibirEstoque() {
        try {
            System.out.println("\n--- ESTOQUE COMPLETO ---");

            if (estoque.isEmpty()) {
                System.out.println("O estoque está vazio.");
            } else {
                System.out.println("ID | Nome | Preço | Quantidade");
                System.out.println("--------------------------------");

                for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                    try {
                        int id = entry.getKey();
                        int quantidade = entry.getValue();
                        Produto produto = produtos.get(id);

                        if (produto != null) {
                            System.out.println(id + " | " + produto.getNome() + " | R$ " +
                                    String.format("%.2f", produto.getPreco()) +
                                    " | " + quantidade);
                        }
                    } catch (NullPointerException e) {
                        System.err.println("Erro ao exibir produto: " + e.getMessage());
                    }
                }
            }
            System.out.println("--------------------------\n");
        } catch (Exception e) {
            System.err.println("Erro ao exibir estoque: " + e.getMessage());
        }
    }

    public static Map<Integer, Produto> getProdutos() {
        try {
            return new HashMap<>(produtos); // Retorna uma cópia para evitar modificações externas
        } catch (Exception e) {
            System.err.println("Erro ao obter produtos: " + e.getMessage());
            return new HashMap<>(); // Retorna mapa vazio em caso de erro
        }
    }

    public static int getQuantidade(int idProduto) {
        try {
            return estoque.getOrDefault(idProduto, 0);
        } catch (NullPointerException e) {
            System.err.println("Erro de referência nula ao consultar quantidade: " + e.getMessage());
            return 0;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao consultar quantidade: " + e.getMessage());
            return 0;
        }
    }
}