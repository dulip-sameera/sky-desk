package com.skydesk.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class FileTransferServerAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Server FileTransfer");
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/scene/FileTransferServerScene.fxml"))));
        } catch (IOException e) {
            System.err.println("Error loading the FXML file: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "There was an error loading the scene. Please try again.",
                    ButtonType.OK);
            alert.setTitle("Error");
            alert.setHeaderText("FXML Loading Error");
            alert.showAndWait();
        }
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Server.close();
    }
}
