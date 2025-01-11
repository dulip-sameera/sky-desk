package com.skydesk.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class UiController {
    public AnchorPane root;
    public Button btnScreenShare;
    public boolean isScreenSharing = false;
    Stage stage;

    public void initialize() {

    }

    public void btnScreenShareOnAction(ActionEvent actionEvent) throws Exception {
        if (!isScreenSharing) {
            isScreenSharing = true;
            stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/scene/ScreenShareScene.fxml"))));
            stage.show();
            btnScreenShare.setText("Stop Screen Share");

        }else{
            isScreenSharing = false;
            stage.close();
            btnScreenShare.setText("Start Screen Share");

        }

    }




}
