package com.skydesk.client.controller;

import javafx.concurrent.Task;
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
    private Socket socket;
    public volatile int screenWidth = 1366;
    public volatile int screenHeight = 768;
    private ObjectOutputStream oos;

    public void initialize() throws IOException {
        socket = new Socket("192.168.8.140", 9090);
        OutputStream os = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        oos = new ObjectOutputStream(bos);

        imgScreen.fitWidthProperty().bind(root.widthProperty());
        imgScreen.fitHeightProperty().bind(root.heightProperty());

        imgScreen.setOnMouseMoved(this::handleMouseMove);

        imgScreen.setOnMouseDragged(this::handleMouseDrag);

        //imgScreen.setOnMouseClicked(this::handleMouseClick);

        imgScreen.setOnMousePressed(this::handleMousePress);

        imgScreen.setOnMouseReleased(this::handleMouseRelease);

        imgScreen.setOnScroll(this::handleMouseScroll);

        startReceivingScreen();
    }

    private void handleMouseMove(MouseEvent mouseEvent) {
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
    }

    private void handleMouseDrag(MouseEvent mouseEvent) {
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
    }

    private void handleMouseClick(MouseEvent mouseEvent) {
        try {
            MouseButton button = mouseEvent.getButton();
            String buttonAction = switch (button) {
                case PRIMARY -> "PRIMARY";
                case SECONDARY -> "SECONDARY";
                case MIDDLE -> "MIDDLE";
                default -> "UNKNOWN";
            };
            oos.writeObject(buttonAction); // Send button action
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void handleMousePress(MouseEvent mouseEvent) {
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
    }
    private void handleMouseRelease(MouseEvent mouseEvent) {
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
    }

    private void handleMouseScroll(ScrollEvent scrollEvent) {
        try {
            double deltaY = scrollEvent.getDeltaY();
            oos.writeObject(deltaY); // Send scroll delta
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startReceivingScreen() {
        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {
                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);

                // Receive screen dimensions from the server
                screenWidth = (int) ois.readObject();
                screenHeight = (int) ois.readObject();

                // Receive and update screen image continuously
                while (true) {
                    byte[] imageBytes = (byte[]) ois.readObject();
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                    Image screenImage = new Image(bais);
                    updateValue(screenImage);
                }
            }
        };

        // Bind ImageView property to the received screen images
        imgScreen.imageProperty().bind(task.valueProperty());
        new Thread(task).start();
    }
}
