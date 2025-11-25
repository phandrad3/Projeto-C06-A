package com.example.lojabumi.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;

public class MudarTela {

    public static void mudarTela(Button botaoOrigem, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(MudarTela.class.getResource(caminhoFXML));
            Scene scene = new Scene(root);

            Stage stage = (Stage) botaoOrigem.getScene().getWindow();
            stage.setScene(scene);

            Platform.runLater(() -> {
                stage.setMaximized(false);
                stage.setMaximized(true);
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
