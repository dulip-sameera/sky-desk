package com.skydesk.client.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ScreenShareSceneController {
    public AnchorPane root;
    public ImageView imgScreen;
    public TextField txtInput;
    private Socket socket;
    public volatile int screenWidth = 1366;
    public volatile int screenHeight = 768;
    private ObjectOutputStream oos;

    public void initialize() throws IOException {
        socket = new Socket("192.168.8.106", 9090);
//        socket = new Socket("127.0.0.1", 9090);
        OutputStream os = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        oos = new ObjectOutputStream(bos);
        root.setPrefHeight(screenHeight);
        root.setPrefWidth(screenWidth);
        imgScreen.setFitWidth(screenWidth);
        imgScreen.setFitHeight(screenHeight);


        //imgScreen.fitWidthProperty().bind(root.widthProperty());
        //imgScreen.fitHeightProperty().bind(root.heightProperty());

        txtInput.prefWidthProperty().bind(root.widthProperty());
        txtInput.prefWidthProperty().bind(root.heightProperty());

        imgScreen.setOnMouseMoved(mouseEvent -> {
            try {
                Point point = new Point(
                        (int) (mouseEvent.getX() * (screenWidth / imgScreen.getFitWidth())),
                        (int) (mouseEvent.getY() * (screenHeight / imgScreen.getFitHeight()))
                );
                oos.writeObject(point); // Send mouse coordinates
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imgScreen.setOnMouseDragged(mouseEvent -> {
            try {
                Point point = new Point(
                        (int) (mouseEvent.getX() * (screenWidth / imgScreen.getFitWidth())),
                        (int) (mouseEvent.getY() * (screenHeight / imgScreen.getFitHeight()))
                );
                oos.writeObject(point); // Send drag coordinates
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        imgScreen.setOnMousePressed(mouseEvent -> {
            try {
                MouseButton button = mouseEvent.getButton();
                String buttonAction = switch (button) {
                    case PRIMARY -> "PRIMARY_PRESS";
                    case SECONDARY -> "SECONDARY_PRESS";
                    case MIDDLE -> "MIDDLE_PRESS";
                    default -> "UNKNOWN";
                };
                oos.writeObject(buttonAction); // Send button action
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imgScreen.setOnMouseReleased(mouseEvent -> {
            try {
                MouseButton button = mouseEvent.getButton();
                String buttonAction = switch (button) {
                    case PRIMARY -> "PRIMARY_RELEASE";
                    case SECONDARY -> "SECONDARY_RELEASE";
                    case MIDDLE -> "MIDDLE_RELEASE";
                    default -> "UNKNOWN";
                };
                oos.writeObject(buttonAction); // Send button action
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imgScreen.setOnScroll(scrollEvent -> {
            try {
                double deltaY = scrollEvent.getDeltaY();
                oos.writeObject(deltaY); // Send scroll delta
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        txtInput.setOnKeyPressed(keyEvent -> {
            try {
                oos.writeObject(keyEvent.getText());
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        startReceivingScreen();
    }



    private void startReceivingScreen() {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {
                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);

                screenWidth = (int) ois.readObject();
                screenHeight = (int) ois.readObject();

                while (true) {
                    byte[] imageBytes = (byte[]) ois.readObject();
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                    Image screenImage = new Image(bais);
                    updateValue(screenImage);
                }
            }
        };

        imgScreen.imageProperty().bind(task.valueProperty());
        new Thread(task).start();
    }


}
