package com.skydesk.client.controller;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;

public class ScreenShareSceneController {
    public AnchorPane root;
    public ImageView imgScreen;
    Socket socket;
    public volatile int screenWidth = 1366;
    public volatile int screenHeight = 768;

    public void initialize() throws IOException {


//        socket = new Socket("192.168.8.140", 9090);
        socket = new Socket("127.0.0.1", 9090);

        OutputStream os = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        imgScreen.setOnMouseMoved(mouseEvent -> {
            try {
                oos.writeObject(new Point((int)( mouseEvent.getX()*(screenWidth/imgScreen.getFitWidth())),
                        (int)( mouseEvent.getY()*(screenHeight/imgScreen.getFitHeight()))));
                oos.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        imgScreen.setOnMouseClicked(mouseEvent -> {
            try {
                MouseButton button = mouseEvent.getButton();
                switch (button){
                    case PRIMARY -> oos.writeObject("PRIMARY");
                    case SECONDARY -> oos.writeObject("SECONDARY");
                    case MIDDLE -> oos.writeObject("MIDDLE");
                    default -> oos.writeObject("UNKNOWN");
                }
                oos.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });


        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {

                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);
                screenWidth = (int) ois.readObject();
                screenHeight = (int) ois.readObject();

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
