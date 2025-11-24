package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class RemoverProdutoController {


    @FXML
    private Button btnRemover;

    @FXML
    private Button btnVoltar;

    @FXML
    private ChoiceBox<Produto> escolherProduto;


    private final Usuario usuario = UserDatabase.getUsuarioLogado();

    @FXML
    public void initialize() {
        btnVoltar.setOnAction(e -> mudarTela(btnVoltar, "/view/Estoque.fxml"));

        btnRemover.setOnAction(e -> removerProduto());

        Platform.runLater(() -> {
            escolherProduto.lookup(".label").setStyle("-fx-text-fill: white;");
        });
        atualizarChoiceBox();
    }

    private void atualizarChoiceBox() {
        escolherProduto.getItems().clear();
        escolherProduto.getItems().addAll(Estoque.getProdutos().values());
    }

    @FXML
    private void removerProduto() {

        Produto produtoSelecionado = escolherProduto.getValue();
        if (produtoSelecionado == null) {
            mostrarErro("Selecione um produto.");
            return;
        }

        if (usuario == null || !usuario.removerProduto()) {
            mostrarErro("Acesso negado. Apenas administradores podem remover produtos.");
            return;
        }

        boolean sucesso = Estoque.removerProduto(produtoSelecionado, usuario);

        if (sucesso) {
            mostrarInfo("Produto removido com sucesso!");
            atualizarChoiceBox();



            if (!escolherProduto.getItems().isEmpty()) {
                escolherProduto.getSelectionModel().selectFirst();
            }

        } else {
            mostrarErro("Erro ao remover produto.");
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
}
