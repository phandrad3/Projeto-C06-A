package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class RemoverProdutoController {

    @FXML
    private TextField buscarField;

    @FXML
    private Button btnRemover;

    @FXML
    private Button btnVoltar;

    @FXML
    private Label detalhesLabel;

    private Produto produtoEncontrado;

    private final Usuario usuario = UserDatabase.getUsuarioLogado();

    @FXML
    public void initialize() {
        // Botão Voltar
        btnVoltar.setOnAction(e -> mudarTela(btnVoltar, "/view/Estoque.fxml"));

        // Botão Remover
        btnRemover.setOnAction(e -> removerProduto());
    }

    @FXML
    private void removerProduto() {
        String nomeBusca = buscarField.getText().trim();

        if (nomeBusca.isEmpty()) {
            mostrarErro("Digite o nome do produto.");
            return;
        }

        Produto encontrado = null;
        for (Produto p : Estoque.getProdutos().values()) {
            if (p.getNome().equalsIgnoreCase(nomeBusca)) {
                encontrado = p;
                break;
            }
        }

        if (encontrado == null) {
            mostrarErro("Produto não encontrado no estoque.");
            detalhesLabel.setText("Detalhes: -");
            return;
        }

        produtoEncontrado = encontrado;

        if (usuario == null || !usuario.liberarAcesso()) {
            mostrarErro("Acesso negado. Apenas administradores podem remover produtos.");
            return;
        }

        boolean sucesso = Estoque.removerProduto(produtoEncontrado, usuario);

        if (sucesso) {
            mostrarInfo("Produto removido com sucesso!");
            detalhesLabel.setText("Detalhes: -");
            buscarField.clear();
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
