package com.skydesk.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Random;

public class UiController {
    public AnchorPane root;
    public Button btnScreenShare;
    public boolean isScreenSharing = false;
    public Label lblAddress;
    public AnchorPane headerPane;
    public AnchorPane serverPane;
    public Button btnStartServer;
    public AnchorPane clientPane;
    public TextField txtInputIp;
    public Button btnConnect;
    Stage stage;

    public void initialize() {
        long address = new Random().nextLong(999999999);
        lblAddress.setText("%,d".formatted(address));

    }



    public void btnStartServerOnAction(ActionEvent actionEvent) {

    }

    public void btnConnectOnAction(ActionEvent actionEvent) throws IOException {
        btnStartServer.setDisable(true);
        if (!isScreenSharing) {
            isScreenSharing = true;
            stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/scene/ScreenShareScene.fxml"))));
            stage.show();
            stage.setResizable(false);
            btnScreenShare.setText("Stop Screen Share");

        }else{
            isScreenSharing = false;
            stage.close();
            btnScreenShare.setText("Start Screen Share");

        }
    }
}
