package com.skydesk.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientControlAppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) throws Exception {

        Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("/scene/ClientControlView.fxml")));
        primarystage.setScene(mainScene);
        primarystage.setTitle("SkyDesk");
        primarystage.setResizable(true);
        primarystage.show();
        primarystage.centerOnScreen();
    }
}
