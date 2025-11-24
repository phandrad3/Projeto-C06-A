package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class AtualizarEstoqueController {

    @FXML
    private ChoiceBox<Produto> escolherProduto;

    @FXML
    private TextField precoField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private Button btnVoltar;


    private Usuario usuario = UserDatabase.getUsuarioLogado();


    private void atualizarChoiceBox() {
        escolherProduto.getItems().clear();
        escolherProduto.getItems().addAll(Estoque.getProdutos().values());
    }


    @FXML
    public void atualizarEstoque() {

        Produto produtoSelecionado = escolherProduto.getValue();

        if (produtoSelecionado == null) {
            mostrarErro("Escolha um produto.");
            return;
        }

        if (usuario == null || !usuario.alterarEstoque()) {
            mostrarErro("Acesso negado. Apenas administradores podem atualizar o estoque.");
            return;
        }

        if (!precoField.getText().trim().isEmpty()) {
            try {
                double novoPreco = Double.parseDouble(precoField.getText().trim());

                boolean precoAtualizado = Estoque.atualizarValor(produtoSelecionado, novoPreco, usuario);
                if (!precoAtualizado) {
                    mostrarErro("Erro ao atualizar o preço. Verifique os dados.");
                    return;
                }

            } catch (Exception e) {
                mostrarErro("Preço inválido.");
                return;
            }
        }

        if (quantidadeField.getText().trim().isEmpty()) {
            mostrarErro("Digite a nova quantidade.");
            return;
        }

        int novaQuantidade;
        try {
            novaQuantidade = Integer.parseInt(quantidadeField.getText().trim());
        } catch (Exception e) {
            mostrarErro("Quantidade inválida.");
            return;
        }

        boolean sucesso = Estoque.atualizarEstoque(produtoSelecionado, novaQuantidade, usuario);

        if (!sucesso) {
            mostrarErro("Erro ao atualizar estoque. Verifique os dados.");
            return;
        }

        mostrarInfo("Estoque atualizado com sucesso!");
        precoField.clear();
        quantidadeField.clear();
        escolherProduto.getSelectionModel().clearSelection();
    }

    @FXML
    public void initialize() {
        btnVoltar.setOnAction(e -> {
                    mudarTela(btnVoltar, "/view/Estoque.fxml");
                }
        );
        atualizarChoiceBox();
        Platform.runLater(() -> {
            escolherProduto.lookup(".label").setStyle("-fx-text-fill: white;");
        });
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
}
