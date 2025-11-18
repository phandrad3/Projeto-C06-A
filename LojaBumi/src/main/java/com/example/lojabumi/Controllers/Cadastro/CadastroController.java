package com.example.lojabumi.Controllers.Cadastro;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.usuario.tipoConta.Administrador;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class CadastroController {
    @FXML
    private TextField emailField;

    @FXML
    private TextField senhaField;

    @FXML
    private Button btnEntrar;

    @FXML
    public void tentarLogin() {
        String email = emailField.getText();
        String senha = senhaField.getText();

        Object usuario = UserDatabase.autenticar(email, senha);

        if (usuario != null) {

            if (usuario instanceof Administrador) {
                System.out.println("Entrou o brabao");
                mudarTela(btnEntrar,"/view/Produtos.fxml");
            } else {
                System.out.println("entrou clientela porra");
                mudarTela(btnEntrar,"/view/Produtos.fxml");
            }

        } else {
            System.out.println("Nao tem cadastro disso ai");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Inválido");
            alert.setHeaderText(null);
            alert.setContentText("Não existe cadastro com esse email e senha.");
            alert.showAndWait();
        }
    }
}
