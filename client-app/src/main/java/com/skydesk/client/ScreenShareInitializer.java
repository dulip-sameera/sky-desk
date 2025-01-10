package com.skydesk.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenShareInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Skydesk");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/scene/Ui.fxml"))));
        primaryStage.show();




    }
}
