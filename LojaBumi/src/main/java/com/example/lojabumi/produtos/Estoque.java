package com.example.lojabumi.produtos;

import com.example.lojabumi.usuario.Permissao;
import com.example.lojabumi.usuario.Usuario;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.Map;

public class Estoque implements Runnable {
    private static StringBuilder mensagem = new StringBuilder();
    private static boolean alertaAtivo = false;
    private static Map<Integer, Integer> estoque = new HashMap<>();
    private static Map<Integer, Produto> produtos = new HashMap<>();
    private static final int LIMITE_CRITICO = 5;
    private static final int INTERVALO_VERIFICACAO = 30000; // 60 segundos
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
            throw new IllegalStateException("Erro de segurança ao iniciar monitoramento", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao iniciar monitoramento", e);
        }
    }

    @Override
    public void run() {
        try {
            while (executando) {
                verificarEstoque();
                Thread.sleep(INTERVALO_VERIFICACAO);
            }
        } catch (InterruptedException e) {
            System.out.println("Monitor de estoque interrompido.");
            executando = false;
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            throw new RuntimeException("Erro no monitoramento de estoque", e);
        }
    }

    private static void verificarEstoque() {
        try {
            mensagem.setLength(0);
            boolean abaixoEstoque = false;

            for (Map.Entry<Integer, Integer> entry : estoque.entrySet()) {
                int idProduto = entry.getKey();
                int quantidade = entry.getValue();
                Produto produto = produtos.get(idProduto);

                if (produto != null && quantidade < LIMITE_CRITICO) {
                    abaixoEstoque = true;
                    mensagem.append("- ")
                            .append(produto.getNome())
                            .append(": ")
                            .append(quantidade)
                            .append(" unidades\n");
                }
            }

            if (abaixoEstoque && !alertaAtivo) {
                System.out.println("ALERTA: " + mensagem.toString());

                final String mensagemFinal = mensagem.toString();

                Platform.runLater(() -> {
                    mostrarNotificacao(mensagemFinal);
                });
            }
        } catch (Exception e) {
            System.err.println("Erro crítico no monitoramento de estoque: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void mostrarNotificacao(String mensagem) {
        try {
            alertaAtivo = true;

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alerta de Estoque Crítico");
            alert.setHeaderText("Produtos com estoque baixo!");
            alert.setContentText(mensagem);


            alert.setOnHidden(event -> {
                alertaAtivo = false;
            });

            alert.setOnCloseRequest(event -> {
                alertaAtivo = false;
            });

            alert.show();
        } catch (Exception e) {
            alertaAtivo = false;
            System.err.println("Erro ao criar alerta: " + e.getMessage());
            throw e;
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
            throw new IllegalStateException("Erro de segurança ao parar monitoramento", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao parar monitoramento", e);
        }
    }

    public static boolean adicionarEstoque(Produto produto, int quantidade, Permissao usuario) {
        try {
            if (!usuario.addEstoque()) {
                throw new IllegalStateException("Usuário não tem permissão para adicionar estoque");
            }

            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }

            if (quantidade <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser positiva");
            }

            int idProduto = produto.getId();
            estoque.put(idProduto, estoque.getOrDefault(idProduto, 0) + quantidade);
            produtos.put(idProduto, produto);

            System.out.println("Estoque atualizado: " + quantidade + " unidade(s) do produto '" +
                    produto.getNome() + "' (ID: " + idProduto + ") adicionadas.");

            return true;
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao adicionar estoque", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao adicionar estoque", e);
        }
    }

    public static boolean atualizarEstoque(Produto produto, int novaQuantidade, Permissao usuario) {
        try {
            if (!usuario.alterarEstoque()) {
                throw new IllegalStateException("Usuário não tem permissão para atualizar estoque");
            }

            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }

            if (novaQuantidade < 0) {
                throw new IllegalArgumentException("Quantidade não pode ser negativa");
            }

            int idProduto = produto.getId();
            estoque.put(idProduto, novaQuantidade);
            produtos.put(idProduto, produto);

            System.out.println("Quantidade atualizada do produto '" + produto.getNome() + "' para " + novaQuantidade);
            return true;
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao atualizar estoque", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao atualizar estoque", e);
        }
    }

    public static boolean atualizarValor(Produto produto, double novoValor, Usuario usuario) {
        try {
            if (!usuario.alterarPreco()) {
                throw new IllegalStateException("Usuário não tem permissão para atualizar valor");
            }

            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }

            if (novoValor <= 0) {
                throw new IllegalArgumentException("Valor deve ser positivo");
            }

            int idProduto = produto.getId();
            produto.setPreco(novoValor);

            System.out.println("Valor do produto '" + produto.getNome() + "' (ID: " + idProduto +
                    ") atualizado para R$ " + novoValor);

            return true;
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao atualizar valor", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao atualizar valor", e);
        }
    }

    public static int getEstoque(Produto produto) {
        try {
            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }
            return estoque.getOrDefault(produto.getId(), 0);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao consultar estoque", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao consultar estoque", e);
        }
    }

    public static boolean removerEstoque(Produto produto, int quantidade, Permissao usuario) {
        try {
            if (!usuario.alterarEstoque()) {
                throw new IllegalStateException("Usuário não tem permissão para remover estoque");
            }

            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }

            if (quantidade <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser positiva");
            }

            int idProduto = produto.getId();
            int estoqueAtual = getEstoque(produto);

            if (estoqueAtual < quantidade) {
                throw new IllegalStateException("Estoque insuficiente. Disponível: " + estoqueAtual + ", solicitado: " + quantidade);
            }

            estoque.put(idProduto, estoqueAtual - quantidade);
            return true;
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao remover estoque", e);
        } catch (IllegalArgumentException e) {
            throw e; // Repassa exceções já tratadas
        } catch (IllegalStateException e) {
            throw e; // Repassa exceções já tratadas
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao remover estoque", e);
        }
    }

    public static boolean removerProduto(Produto produto, Permissao usuario) {
        try {
            if (!usuario.removerProduto()) {
                throw new IllegalStateException("Usuário não tem permissão para remover produto");
            }

            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }

            int idProduto = produto.getId();

            if (estoque.containsKey(idProduto)) {
                estoque.remove(idProduto);
                produtos.remove(idProduto);
                System.out.println("Produto '" + produto.getNome() + "' removido do estoque.");
            } else {
                throw new IllegalStateException("Produto não encontrado no estoque");
            }

            return true;
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao remover produto", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao remover produto", e);
        }
    }

    public static Map<Integer, Produto> getProdutos() {
        try {
            return new HashMap<>(produtos);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter produtos", e);
        }
    }

    public static int getQuantidade(int idProduto) {
        try {
            return estoque.getOrDefault(idProduto, 0);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Erro de referência nula ao consultar quantidade", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao consultar quantidade", e);
        }
    }
}