package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class RemoverEstoqueController {

    @FXML
    private ChoiceBox<Produto> escolherProduto;

    @FXML
    private TextField quantidadeRemoverField;

    @FXML
    private Label quantidadeAtualLabel;

    @FXML
    private Button btnVoltar;

    private Usuario usuario = Usuario.getUsuarioLogado();

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

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeRemoverField.getText().trim());
        } catch (Exception e) {
            mostrarErro("Quantidade inválida.");
            return;
        }

        boolean sucesso = Estoque.removerEstoque(produto, quantidade, usuario);

        if (!sucesso) {
            mostrarErro("Não foi possível remover. Quantidade insuficiente no estoque.");
            return;
        }

        mostrarInfo("Removido com sucesso!");

        quantidadeAtualLabel.setText("Quantidade Atual: " + Estoque.getEstoque(produto));
        quantidadeRemoverField.clear();
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
                }
        );
        Platform.runLater(() -> {
            escolherProduto.lookup(".label").setStyle("-fx-text-fill: white;");
        });
        escolherProduto.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            atualizarQuantidade(newVal);
        });
    }
}