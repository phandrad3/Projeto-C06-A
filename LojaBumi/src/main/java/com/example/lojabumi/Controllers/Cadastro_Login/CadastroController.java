package com.example.lojabumi.Controllers.Cadastro_Login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.lojabumi.Controllers.MudarTela.mudarTela;

public class CadastroController {

    @FXML
    private Button btnLogin;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField data_nascField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField senhaField;
    @FXML
    private TextField confirme_senhaField;


    private boolean validarDataNascimento(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate dataNascimento = LocalDate.parse(data, formatter);
            if (dataNascimento.isAfter(LocalDate.now())) {
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    @FXML
    public void tentarCadastro() {
        try {
            String nome = nomeField.getText().trim();
            String data_nasc = data_nascField.getText().trim();
            String email = emailField.getText().trim();
            String senha = senhaField.getText().trim();
            String confirme_senha = confirme_senhaField.getText().trim();

            if (nome.isEmpty() || data_nasc.isEmpty() || email.isEmpty() || senha.isEmpty() || confirme_senha.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro no cadastro");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, preencha todos os campos.");
                alert.showAndWait();
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro no cadastro");
                alert.setHeaderText(null);
                alert.setContentText("Formato de email inválido.");
                alert.showAndWait();
                return;
            }

            if (!senha.equals(confirme_senha)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro no cadastro");
                alert.setHeaderText(null);
                alert.setContentText("As senhas não coincidem.");
                alert.showAndWait();
                return;
            }
            if (!validarDataNascimento(data_nasc)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro no cadastro");
                alert.setHeaderText(null);
                alert.setContentText("Data de nascimento inválida! Use o formato dd/MM/yyyy.");
                alert.showAndWait();
                return;
            }


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cadastro");
            alert.setHeaderText(null);
            alert.setContentText("Cadastro realizado com sucesso!");
            alert.showAndWait();

            mudarTela(btnLogin, "/view/Login.fxml");

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, preencha todos os campos!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}
