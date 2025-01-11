package com.skydesk.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Server extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Skydesk Server");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/scene/ServerScene.fxml"))));
        primaryStage.show();

    }
}
