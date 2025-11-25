package com.example.lojabumi.controllers.cadastro_login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.lojabumi.controllers.MudarTela.mudarTela;
import static com.example.lojabumi.config.SupabaseConfig.insertData;

public class CadastroController {

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnVoltar;
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

    private String converterDataParaBanco(String data) {
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterSaida = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate dataNascimento = LocalDate.parse(data, formatterEntrada);
            return dataNascimento.format(formatterSaida);
        } catch (DateTimeParseException e) {
            return null;
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
                mostrarAlerta(Alert.AlertType.WARNING, "Erro no cadastro", "Por favor, preencha todos os campos.");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                mostrarAlerta(Alert.AlertType.WARNING, "Erro no cadastro", "Formato de email inválido.");
                return;
            }

            if (!senha.equals(confirme_senha)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Erro no cadastro", "As senhas não coincidem.");
                return;
            }

            if (!validarDataNascimento(data_nasc)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Erro no cadastro", "Data de nascimento inválida! Use o formato dd/MM/yyyy.");
                return;
            }

            String dataConvertida = converterDataParaBanco(data_nasc);
            if (dataConvertida == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Erro no cadastro", "Data de nascimento inválida!");
                return;
            }

            String jsonInputString = String.format(
                    "{\"nomeUsuario\":\"%s\",\"dataNasc\":\"%s\",\"email\":\"%s\",\"senha\":\"%s\",\"tipoUsuario\":\"Cliente\"}",
                    nome, dataConvertida, email, senha
            );


            insertData("usuario", jsonInputString);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Cadastro", "Cadastro realizado com sucesso!");
            mudarTela(btnLogin, "/view/Login.fxml");

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro no cadastro", "Ocorreu um erro ao realizar o cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    public void initialize() {
        btnVoltar.setOnAction(e -> mudarTela(btnVoltar, "/view/Login.fxml"));
    }
}

