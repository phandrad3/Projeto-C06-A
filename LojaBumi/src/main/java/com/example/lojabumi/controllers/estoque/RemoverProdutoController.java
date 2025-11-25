package com.example.lojabumi.controllers.estoque;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.utilitarios.Verificacoes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static com.example.lojabumi.controllers.MudarTela.mudarTela;

public class RemoverProdutoController {


    @FXML
    private Button btnRemover;

    @FXML
    private Button btnVoltar;

    @FXML
    private ChoiceBox<Produto> escolherProduto;


    private final Usuario usuario = Verificacoes.getUsuarioLogado();

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

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Remoção");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja remover o produto " + produtoSelecionado.getNome() + "?");

        if (confirmacao.showAndWait().get() != ButtonType.OK) {
            return;
        }

        Integer idProduto = SupabaseConfig.getProdutoIdByNome(produtoSelecionado.getNome());
        if (idProduto == null) {
            mostrarErro("Produto não encontrado no banco de dados.");
            return;
        }

        boolean sucesso = SupabaseConfig.deleteData("produtos",
                String.valueOf(idProduto));

        if (sucesso) {
            Estoque.removerProduto(produtoSelecionado, usuario);

            mostrarInfo("Produto removido com sucesso!");
            atualizarChoiceBox();
            escolherProduto.getSelectionModel().clearSelection();
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
