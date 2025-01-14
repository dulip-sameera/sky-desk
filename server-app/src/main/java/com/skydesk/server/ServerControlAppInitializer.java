package com.skydesk.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import static javafx.application.Application.launch;

public class ServerControlAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) throws Exception {

        Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("/scene/ServerControlView.fxml")));
        primarystage.setScene(mainScene);
        primarystage.setTitle("Server Desktop");
        primarystage.setResizable(true);
        primarystage.show();
        primarystage.centerOnScreen();
    }



}
