package com.example.lojabumi.controllers.estoque;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.*;
import com.example.lojabumi.produtos.tipo.*;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.utilitarios.Verificacoes;
import javafx.application.Platform;
import javafx.fxml.FXML;


import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.util.Locale;

import static com.example.lojabumi.controllers.MudarTela.mudarTela;

public class AddProdutoController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField precoField;
    @FXML
    private TextField quantidadeField;
    @FXML
    private ChoiceBox<String> tipoChoiceBox;
    @FXML
    private Button btnAdicionar;
    @FXML
    private Button btnVoltar;

    @FXML
    public void initialize() {
        tipoChoiceBox.getItems().addAll("Eletrônicos", "Frutas", "Frios", "Verduras", "Não perecíveis");

        btnAdicionar.setOnAction(e -> adicionarProduto());

        btnVoltar.setOnAction(e -> {
                    mudarTela(btnVoltar, "/view/Estoque.fxml"
                    );
                }
        );
        Platform.runLater(() -> {
            tipoChoiceBox.lookup(".label").setStyle("-fx-text-fill: white;");
        });
    }


    private void adicionarProduto() {
        Usuario usuario = Verificacoes.getUsuarioLogado();

        String nome = nomeField.getText().trim();
        String precoText = precoField.getText().trim();
        String quantidadeText = quantidadeField.getText().trim();
        String tipo = tipoChoiceBox.getValue();

        if (nome.isEmpty() || precoText.isEmpty() || quantidadeText.isEmpty() || tipo == null) {
            new Alert(Alert.AlertType.WARNING, "Por favor, preencha todos os campos!").showAndWait();
            return;
        }

        double preco;
        int quantidade;

        try {
            preco = Double.parseDouble(precoText);
            quantidade = Integer.parseInt(quantidadeText);
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Preço ou quantidade inválidos!").showAndWait();
            return;
        }

        Produto produto;
        switch (tipo) {
            case "Eletrônicos":
                produto = new Eletronico(nome, preco);
                break;
            case "Frutas":
                produto = new Frutas(nome, preco);
                break;
            case "Frios":
                produto = new Frios(nome, preco);
                break;
            case "Verduras":
                produto = new Verduras(nome, preco);
                break;
            default:
                produto = new NaoPereciveis(nome, preco);
        }
        boolean sucesso = Estoque.adicionarEstoque(produto, quantidade, usuario);

        if (sucesso) {
            new Alert(Alert.AlertType.INFORMATION, "Produto adicionado com sucesso!").showAndWait();
            String tableName = "produtos";
            String precoFormatado = String.format(Locale.US, "%.2f", preco);

            String jsonInputString = String.format(
                    Locale.US,
                    "{\"nome\": \"%s\", \"quantidade\": %d, \"preco\": %s, \"tipoProduto\": \"%s\"}",
                    produto.getNome(), quantidade, precoFormatado, tipo
            );

            System.out.println("DEBUG JSON: " + jsonInputString);

            SupabaseConfig.insertData(tableName, jsonInputString);

            nomeField.clear();
            precoField.clear();
            quantidadeField.clear();
            tipoChoiceBox.getSelectionModel().clearSelection();

        } else {
            new Alert(Alert.AlertType.ERROR, "Não foi possível adicionar o produto.").showAndWait();
        }
    }

}
