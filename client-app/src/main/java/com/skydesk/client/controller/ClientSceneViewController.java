package com.skydesk.client.controller;

import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;

public class ClientSceneViewController {
    public AnchorPane root;
    public ImageView imageScene;
    public TextField txtInput;
    private Socket socket;

    public void initialize() throws Exception {
        imageScene.fitWidthProperty().bind(root.widthProperty());
        imageScene.fitHeightProperty().bind(root.heightProperty());

        txtInput.prefWidthProperty().bind(root.widthProperty());
        txtInput.prefWidthProperty().bind(root.heightProperty());



        socket = new Socket("127.0.0.1", 9090);
        System.out.println("Connected Successfully");

//        OutputStream osCoords = socket.getOutputStream();
//        BufferedOutputStream bosCoords = new BufferedOutputStream(osCoords);
//        ObjectOutputStream oosCoords = new ObjectOutputStream(bosCoords);

        OutputStream osKeys = socket.getOutputStream();
        BufferedOutputStream bosKeys = new BufferedOutputStream(osKeys);
        ObjectOutputStream oosKeys = new ObjectOutputStream(bosKeys);

//        imageScene.setOnMouseMoved(mouseEvent -> {
//            try {
//                oosCoords.writeObject(new Point((int) mouseEvent.getX(), (int) mouseEvent.getY()));
//                oosCoords.flush();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        txtInput.setOnKeyPressed(keyEvent -> {
            try {
//                System.out.println(keyEvent.getCode().getCode());
                oosKeys.writeObject(keyEvent.getText());
                System.out.println("1 = " + keyEvent.getText());
                System.out.println("2 = " + keyEvent.getText().getClass());
                System.out.println();
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

    public void imageSceneOnMouseClicked(MouseEvent mouseEvent) throws IOException {

//        OutputStream osTouch = socket.getOutputStream();
//        BufferedOutputStream bosTouch = new BufferedOutputStream(osTouch);
//        ObjectOutputStream oosTouch = new ObjectOutputStream(bosTouch);
//
//        String clickType = mouseEvent.getButton().toString();
////        new MouseClick(clickType,(int) mouseEvent.getScreenX(),(int) mouseEvent.getScreenY());
//        System.out.println("Click Type: " + clickType);
//        oosTouch.writeObject(clickType);
//        oosTouch.flush();

    }
}


