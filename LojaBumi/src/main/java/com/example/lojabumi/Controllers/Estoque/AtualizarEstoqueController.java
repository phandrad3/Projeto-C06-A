package com.example.lojabumi.Controllers.Estoque;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.produtos.Estoque;
import com.example.lojabumi.produtos.Produto;
import com.example.lojabumi.usuario.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class AtualizarEstoqueController {

    @FXML
    private TextField buscarField;

    @FXML
    private TextField precoField;

    @FXML
    private TextField quantidadeField;

    @FXML
    private Button btnVoltar;

    private Usuario usuario = UserDatabase.getUsuarioLogado();



    private Produto buscarProdutoPorNome() {
        String nomeBusca = buscarField.getText().trim();

        if (nomeBusca.isEmpty()) {
            mostrarErro("Digite o nome do produto.");
            return null;
        }

        for (Produto p : Estoque.getProdutos().values()) {
            if (p.getNome().equalsIgnoreCase(nomeBusca)) {
                return p;
            }
        }

        return null;
    }


    @FXML
    public void atualizarEstoque() {

        Produto produtoEncontrado = buscarProdutoPorNome();

        if (produtoEncontrado == null) {
            mostrarErro("Produto não encontrado no estoque.");
            return;
        }

        if (usuario == null || !usuario.alterarEstoque()) {
            mostrarErro("Acesso negado. Apenas administradores podem atualizar o estoque.");
            return;
        }

        if (!precoField.getText().trim().isEmpty()) {
            try {
                double novoPreco = Double.parseDouble(precoField.getText().trim());

                boolean precoAtualizado = Estoque.atualizarValor(produtoEncontrado, novoPreco, usuario);
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

        boolean sucesso = Estoque.atualizarEstoque(produtoEncontrado, novaQuantidade, usuario);

        if (!sucesso) {
            mostrarErro("Erro ao atualizar estoque. Verifique os dados.");
            return;
        }

        mostrarInfo("Estoque atualizado com sucesso!");
        buscarField.clear();
        precoField.clear();
        quantidadeField.clear();
    }




    @FXML
    public void initialize() {
        btnVoltar.setOnAction(e -> {
                    mudarTela(btnVoltar, "/view/Estoque.fxml");
                }
        );
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
