package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.config.SupabaseConfig;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.Locale;
import java.util.Map;

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

    private Usuario usuario = Usuario.getUsuarioLogado();

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

        // Buscar o ID real do produto no banco usando o nome
        Integer idProduto = SupabaseConfig.getProdutoIdByNome(produtoSelecionado.getNome());
        if (idProduto == null) {
            mostrarErro("Produto não encontrado no banco de dados.");
            return;
        }

        String nomeAtual = produtoSelecionado.getNome();
        double precoAtual = produtoSelecionado.getPreco();
        String tipoAtual = produtoSelecionado.getTipo();

        double novoPreco = precoAtual;
        int novaQuantidade;

        if (!precoField.getText().trim().isEmpty()) {
            try {
                novoPreco = Double.parseDouble(precoField.getText().trim());
            } catch (Exception e) {
                mostrarErro("Preço inválido.");
                return;
            }
        }

        // Verificar se a quantidade foi informada (é obrigatório)
        if (quantidadeField.getText().trim().isEmpty()) {
            mostrarErro("Digite a nova quantidade.");
            return;
        }

        try {
            novaQuantidade = Integer.parseInt(quantidadeField.getText().trim());
        } catch (Exception e) {
            mostrarErro("Quantidade inválida.");
            return;
        }

        // Formatar o preço para o JSON (usando ponto como separador decimal)
        String precoFormatado = String.format(Locale.US, "%.2f", novoPreco);

        // Construir JSON com todos os campos necessários
        String jsonInputString = String.format(
                Locale.US,
                "{\"idProduto\": %d, \"nome\": \"%s\", \"quantidade\": %d, \"preco\": %s, \"tipoProduto\": \"%s\"}",
                idProduto, nomeAtual, novaQuantidade, precoFormatado, tipoAtual
        );

        System.out.println("DEBUG JSON: " + jsonInputString);

        boolean sucesso = SupabaseConfig.updateData("produtos",
                String.valueOf(idProduto), jsonInputString);

        if (sucesso) {
            Estoque.atualizarValor(produtoSelecionado, novoPreco, usuario);
            Estoque.atualizarEstoque(produtoSelecionado, novaQuantidade, usuario);

            mostrarInfo("Estoque atualizado com sucesso!");
            precoField.clear();
            quantidadeField.clear();
            escolherProduto.getSelectionModel().clearSelection();
        } else {
            mostrarErro("Erro ao atualizar estoque. Verifique os dados.");
        }
    }

    @FXML
    public void initialize() {
        btnVoltar.setOnAction(e -> {
            mudarTela(btnVoltar, "/view/Estoque.fxml");
        });
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