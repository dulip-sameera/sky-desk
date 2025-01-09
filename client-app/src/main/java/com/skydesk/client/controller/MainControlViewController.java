package com.skydesk.client.controller;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.Key;

public class MainControlViewController {
    public AnchorPane root;
    public ImageView imageScene;
    private Socket socket;

    public void initialize() throws Exception {
        imageScene.fitWidthProperty().bind(root.widthProperty());
        imageScene.fitHeightProperty().bind(root.heightProperty());

        socket = new Socket("192.168.179.79", 9090);

        OutputStream osCoords = socket.getOutputStream();
        BufferedOutputStream bosCoords = new BufferedOutputStream(osCoords);
        ObjectOutputStream oosCoords = new ObjectOutputStream(bosCoords);

        OutputStream osKeys = socket.getOutputStream();
        BufferedOutputStream bosKeys = new BufferedOutputStream(osKeys);
        ObjectOutputStream oosKeys = new ObjectOutputStream(bosKeys);

        imageScene.setOnMouseMoved(mouseEvent -> {
            try {
                oosCoords.writeObject(new Point((int) mouseEvent.getX(), (int) mouseEvent.getY()));
                oosCoords.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imageScene.setOnKeyPressed(keyEvent -> {
            try {
                oosKeys.writeObject(new Key[Integer.parseInt(keyEvent.getCharacter())]);
                oosKeys.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Task<javafx.scene.image.Image> task = new Task<>() {
            @Override
            protected javafx.scene.image.Image call() throws Exception {
                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);
                while (true) {
                    byte[] image = (byte[]) ois.readObject();
                    ByteArrayInputStream bais = new ByteArrayInputStream(image);
                    javafx.scene.image.Image screen = new Image(bais);
                    updateValue(screen);
                }
            }
        };

        imageScene.imageProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

}
