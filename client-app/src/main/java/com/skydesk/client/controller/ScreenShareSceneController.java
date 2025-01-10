package com.skydesk.client.controller;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;

public class ScreenShareSceneController {
    public AnchorPane root;
    public ImageView imgScreen;
    Socket socket;
    double screenWidth, screenHeight;

    public void initialize() throws IOException {


        socket = new Socket("127.0.0.1", 9090);

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {

                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);

                while (true) {
                    byte[] image = (byte[]) ois.readObject();
                    ByteArrayInputStream bais = new ByteArrayInputStream(image);
                    Image screen = new Image(bais);
                    updateValue(screen);

                }
            }
        };
        imgScreen.fitWidthProperty().bind(root.widthProperty());
        imgScreen.fitHeightProperty().bind(root.heightProperty());
        imgScreen.imageProperty().bind(task.valueProperty());


        new Thread(task).start();

    }
}
