package com.skydesk.client;

import com.skydesk.client.controller.ScreenShareSceneController;
import com.skydesk.shared.util.DesktopColor;
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
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/scene/ScreenShareScene.fxml"))));
        primaryStage.show();
        primaryStage.setFullScreen(false);

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DesktopColor.revertDesktop();
    }
}
