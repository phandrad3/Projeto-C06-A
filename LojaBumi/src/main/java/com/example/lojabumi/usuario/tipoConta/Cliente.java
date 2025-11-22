package com.example.lojabumi.usuario.tipoConta;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.scene.control.Alert;

import java.util.HashMap;
import java.util.Map;

public class Cliente extends Usuario {
    private Map<Produto, Integer> carrinho = new HashMap<>();

    public Cliente(int idUsuario, String nome, String dataNasc, String email, String senha) {
        super(idUsuario, nome, dataNasc, email, senha);
    }

    public Map<Produto, Integer> getCarrinho() {
        try {
            return new HashMap<>(carrinho); // Retorna uma cópia para evitar modificações externas
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter carrinho", e);
        }
    }

    public void addProduto(Produto produto) {
        try {
            if (produto == null) {
                throw new IllegalArgumentException("Produto não pode ser nulo");
            }

            int quantidadeCarrinho = carrinho.getOrDefault(produto, 0);
            int estoqueDisponivel = Estoque.getEstoque(produto);

            if (quantidadeCarrinho >= estoqueDisponivel) {
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Não temos mais estoque desse produto!");
                    alert.showAndWait();
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao exibir alerta de estoque insuficiente", e);
                }
                System.out.println("Estoque insuficiente");
                return;
            }

            carrinho.put(produto, quantidadeCarrinho + 1);
            System.out.println("Produto '" + produto.getNome() + "' adicionado ao carrinho.");

            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Produto adicionado no carrinho!");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao exibir alerta de sucesso", e);
            }
        } catch (IllegalArgumentException e) {
            throw e; // Repassa exceções já tratadas
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao adicionar produto", e);
        }
    }

    public void verCarrinho() {
        try {
            System.out.println("\n--- Meu Carrinho ---");
            if (carrinho.isEmpty()) {
                System.out.println("O carrinho está vazio.");
            } else {
                double total = 0;
                for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                    try {
                        Produto produto = entry.getKey();
                        int quantidade = entry.getValue();
                        double subtotal = produto.getPreco() * quantidade;
                        total += subtotal;
                        System.out.println(produto.getNome() + " | Quantidade: " + quantidade + " | Subtotal: R$ " + String.format("%.2f", subtotal));
                    } catch (NullPointerException e) {
                        throw new IllegalStateException("Erro ao processar item do carrinho", e);
                    }
                }
                System.out.println("-------------------------------------------------");
                System.out.println("TOTAL DO CARRINHO: R$ " + String.format("%.2f", total));
            }
            System.out.println("--------------------\n");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exibir carrinho", e);
        }
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