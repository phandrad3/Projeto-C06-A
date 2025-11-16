package com.example.lojabumi.Controllers.Carrinho;

import com.example.lojabumi.Controllers.MudarTela;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CarrinhoController {

    @FXML
    private Button btnVoltarProdutos;

    @FXML
    public void initialize() {

        btnVoltarProdutos.setOnAction(e -> {
                    MudarTela.mudarTela(btnVoltarProdutos, "/view/Produtos.fxml");
                }
        );
    }
}
