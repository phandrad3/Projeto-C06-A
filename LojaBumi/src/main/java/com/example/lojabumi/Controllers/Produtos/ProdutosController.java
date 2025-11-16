package com.example.lojabumi.Controllers.Produtos;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.usuario.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class ProdutosController {


    private List<String> carrinho = new ArrayList<>();


    @FXML
    private void adicionarAoCarrinho(ActionEvent event) {

        Button botao = (Button) event.getSource();
        String produto = "Produto";

        carrinho.add(produto);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Carrinho");

        alert.setContentText(produto + " adicionado ao carrinho!");
        alert.showAndWait();
    }

    @FXML
    private Button sair;
    @FXML
    private Button estoque;
    @FXML
    private Button btncarrinho;

    @FXML
    public void initialize() {
        sair.setOnAction(e -> {
                    mudarTela(sair, "/view/Cadastro.fxml");
                }
        );
        btncarrinho.setOnAction(e -> {
                    mudarTela(btncarrinho, "/view/Carrinho.fxml");
                }
        );
    }

    @FXML
    public void entrarEstoque() {

        Usuario usuario = UserDatabase.getUsuarioLogado();

        if (!usuario.liberarAcesso()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Acesso Negado");
            alert.setHeaderText(null);
            alert.setContentText("Você não tem permissão para acessar o estoque!");
            alert.showAndWait();
            return;
        }
        System.out.println("vc podeee hahaha");
        mudarTela(estoque, "/view/Estoque.fxml");
    }

}


