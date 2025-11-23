package com.example.lojabumi.Controllers.Cadastro;

import com.example.lojabumi.UserDatabase;
import com.example.lojabumi.usuario.Usuario;
import com.example.lojabumi.produtos.Estoque;
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
        try {
            // Validar campos preenchidos
            String email = emailField.getText().trim();
            String senha = senhaField.getText().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                throw new IllegalArgumentException("Por favor, preencha todos os campos.");
            }

            // Validar formato do email (simples)
            if (!email.contains("@") || !email.contains(".")) {
                throw new IllegalArgumentException("Formato de email inválido.");
            }

            // Tentar autenticar
            Object usuario = UserDatabase.autenticar(email, senha);

            if (usuario != null) {
                if (usuario instanceof Administrador) {
                    System.out.println("Login de administrador realizado com sucesso");
                    Estoque.iniciarMonitoramento();
                    mudarTela(btnEntrar, "/view/Produtos.fxml");
                } else {
                    System.out.println("Login de cliente realizado com sucesso");
                    mudarTela(btnEntrar, "/view/Produtos.fxml");
                }
            } else {
                throw new IllegalStateException("Email ou senha incorretos.");
            }

        } catch (IllegalArgumentException e) {
            // Erros de validação
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dados Inválidos");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } catch (IllegalStateException e) {
            // Erros de autenticação
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Inválido");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        } catch (Exception e) {
            // Erros inesperados (banco de dados, mudança de tela, etc.)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no Sistema");
            alert.setHeaderText("Ocorreu um erro inesperado");
            alert.setContentText("Não foi possível realizar o login. Tente novamente mais tarde.");
            alert.showAndWait();

            // Log do erro para depuração
            System.err.println("Erro durante o login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}