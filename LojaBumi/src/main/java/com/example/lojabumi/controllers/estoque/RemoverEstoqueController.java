package com.example.lojabumi.controllers.estoque;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.utilitarios.Verificacoes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Locale;

import static com.example.lojabumi.controllers.MudarTela.mudarTela;

public class RemoverEstoqueController {

    @FXML
    private ChoiceBox<Produto> escolherProduto;

    @FXML
    private TextField quantidadeRemoverField;

    @FXML
    private Label quantidadeAtualLabel;

    @FXML
    private Button btnVoltar;

    private Usuario usuario = Verificacoes.getUsuarioLogado();

    private void atualizarChoiceBox() {
        escolherProduto.getItems().clear();
        escolherProduto.getItems().addAll(Estoque.getProdutos().values());
    }

    private void atualizarQuantidade(Produto produtoSelecionado) {
        if (produtoSelecionado == null) {
            quantidadeAtualLabel.setText("Quantidade Atual: -");
            return;
        }

        int qtd = Estoque.getEstoque(produtoSelecionado);
        quantidadeAtualLabel.setText("Quantidade Atual: " + qtd);
    }

    @FXML
    private void removerEstoque() {
        Produto produto = escolherProduto.getValue();

        if (produto == null) {
            mostrarErro("Produto não encontrado no estoque.");
            return;
        }

        if (usuario == null || !usuario.alterarEstoque()) {
            mostrarErro("Apenas administradores podem remover estoque.");
            return;
        }

        if (quantidadeRemoverField.getText().trim().isEmpty()) {
            mostrarErro("Digite a quantidade a remover.");
            return;
        }

        int quantidadeRemover;
        try {
            quantidadeRemover = Integer.parseInt(quantidadeRemoverField.getText().trim());
            if (quantidadeRemover <= 0) {
                mostrarErro("A quantidade deve ser maior que zero.");
                return;
            }
        } catch (Exception e) {
            mostrarErro("Quantidade inválida.");
            return;
        }

        Integer idProduto = SupabaseConfig.getProdutoIdByNome(produto.getNome());
        if (idProduto == null) {
            mostrarErro("Produto não encontrado no banco de dados.");
            return;
        }

        int quantidadeAtual = Estoque.getEstoque(produto);

        if (quantidadeAtual < quantidadeRemover) {
            mostrarErro("Não foi possível remover. Quantidade insuficiente no estoque.");
            return;
        }

        int novaQuantidade = quantidadeAtual - quantidadeRemover;

        String nomeAtual = produto.getNome();
        double precoAtual = produto.getPreco();
        String tipoAtual = produto.getTipo();

        String precoFormatado = String.format(Locale.US, "%.2f", precoAtual);

        String jsonInputString = String.format(
                Locale.US,
                "{\"idProduto\": %d, \"nome\": \"%s\", \"quantidade\": %d, \"preco\": %s, \"tipoProduto\": \"%s\"}",
                idProduto, nomeAtual, novaQuantidade, precoFormatado, tipoAtual
        );

        System.out.println("DEBUG JSON: " + jsonInputString);

        boolean sucesso = SupabaseConfig.updateData("produtos",
                String.valueOf(idProduto), jsonInputString);

        if (sucesso) {
            Estoque.removerEstoque(produto, quantidadeRemover, usuario);

            atualizarQuantidade(produto);
            quantidadeRemoverField.clear();

            mostrarInfo("Removido com sucesso!");
        } else {
            mostrarErro("Erro ao atualizar estoque. Verifique os dados.");
        }
    }

    private void mostrarErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        atualizarChoiceBox();
        btnVoltar.setOnAction(e -> {
            mudarTela(btnVoltar, "/view/Estoque.fxml");
        });
        Platform.runLater(() -> {
            escolherProduto.lookup(".label").setStyle("-fx-text-fill: white;");
        });
        escolherProduto.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            atualizarQuantidade(newVal);
        });
    }
}