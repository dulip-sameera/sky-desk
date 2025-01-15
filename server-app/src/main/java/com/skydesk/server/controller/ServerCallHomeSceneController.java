package com.skydesk.server.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerCallHomeSceneController {

    public Button btnConnect;

    public TextField txtUserName;

    public Label lblValidate;

    @FXML
    private AnchorPane root;

    public void initialize() {


    }

    public void btnConnectOnAction(ActionEvent actionEvent) throws IOException {
        String ipAddress = null;
        String userName = txtUserName.getText().toLowerCase();

        if (userName.equals("sasika")) ipAddress = "127.0.0.1";
        if (userName.equals("dulip")) ipAddress = "127.0.0.2";
        if (userName.equals("anuk")) ipAddress = "127.0.0.3";
        if (userName.equals("harshana")) ipAddress = "127.0.0.4";
        if (userName.equals("")) ipAddress = "127.0.0.5";

        System.out.println(userName);
        if (ipAddress != null) {
            try {
                // Load the new scene
                Parent secondaryRoot = FXMLLoader.load(getClass().getResource("/scene/ServerVideoScene.fxml"));

                // Get the current stage
                Stage stage = (Stage) root.getScene().getWindow();

                // Set the new scene
                stage.setScene(new Scene(secondaryRoot));
                stage.sizeToScene();
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load the scene. Check the FXML file path.");
            }
        } else {
            System.out.println("Invalid UserName");
            lblValidate.setText("Invalid UserName. Please Enter a valid UserName");
        }
    }
}
