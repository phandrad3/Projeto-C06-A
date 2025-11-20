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

public class RemoverEstoqueController {

    @FXML
    private TextField buscarField;

    @FXML
    private TextField quantidadeRemoverField;

    @FXML
    private Label quantidadeAtualLabel;

    @FXML
    private Button btnVoltar;

    private Usuario usuario = UserDatabase.getUsuarioLogado();


    private Produto buscarProduto() {
        String nomeBusca = buscarField.getText().trim();

        if (nomeBusca.isEmpty()) {
            quantidadeAtualLabel.setText("Quantidade Atual: -");
            return null;
        }

        for (Produto p : Estoque.getProdutos().values()) {
            if (p.getNome().equalsIgnoreCase(nomeBusca)) {

                int qtd = Estoque.getEstoque(p);
                quantidadeAtualLabel.setText("Quantidade Atual: " + qtd);
                return p;
            }
        }

        quantidadeAtualLabel.setText("Quantidade Atual: Produto não encontrado");
        return null;
    }


    @FXML
    private void removerEstoque() {

        Produto produto = buscarProduto();

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
        buscarField.clear();
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

        buscarField.textProperty().addListener((obs, oldV, newV) -> {
            buscarProduto();
        });
        btnVoltar.setOnAction(e -> {
                    mudarTela(btnVoltar, "/view/Estoque.fxml");
                }
        );
    }
}
