package com.skydesk.client.controller;

import com.github.sarxos.webcam.Webcam;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientVideoController {

    public ImageView imgCamera;
    public AnchorPane root;

    private volatile boolean isServerRunning = true; // Flag to control server thread
    private volatile boolean isClientRunning = true; // Flag to control client thread
    private Thread serverThread;
    private Thread clientThread;

    public void initialize() {

        // Start client display and server camera streaming in separate threads
        serverThread = new Thread(() -> {
            try {
                onServerCam();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        clientThread = new Thread(this::clientDisplay);
        clientThread.start();
    }

    //audio capture////////////////////////
    public void audioCapture() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
        microphone.open(format);
        microphone.start();

    }
    //end audio capture////////////////////////

    // Client Screen Display
    public void clientDisplay() {

        imgCamera.fitWidthProperty().bind(root.widthProperty());
        imgCamera.fitHeightProperty().bind(root.heightProperty());

        Task<Image> task = new Task<>() {
            @Override
            protected Image call() throws Exception {
                Socket socket = new Socket("192.168.43.190", 5051);
                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);

                while (isClientRunning) {
                    byte[] image = (byte[]) ois.readObject();
                    updateValue(new Image(new ByteArrayInputStream(image)));
                }

                socket.close(); // Close the client socket when stopped
                return null;
            }
        };

        imgCamera.imageProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    // Server Camera Stream
    public void onServerCam() throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();

        ServerSocket serverSocket = new ServerSocket(5050);
        System.out.println("Server started on port 5050");


        while (isServerRunning) {
            System.out.println("Waiting for connection...");
            Socket localSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + localSocket.getRemoteSocketAddress());

            new Thread(() -> {
                try {
                    OutputStream os = localSocket.getOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    ObjectOutputStream oos = new ObjectOutputStream(bos);

                    while (isServerRunning) {
                        BufferedImage image = webcam.getImage();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", baos);
                        oos.writeObject(baos.toByteArray());
                        oos.flush();
                        Thread.sleep(1000 / 27); // 27 FPS
                    }

                    localSocket.close(); // Close the socket when stopped
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        serverSocket.close(); // Close the server socket when stopped
        webcam.close(); // Close the webcam
    }

    public void btnCutOnAction(ActionEvent actionEvent) {
        System.out.println("Call is cut");

        // Stop both server and client threads
        isServerRunning = false;
        isClientRunning = false;

        try {
            if (serverThread != null && serverThread.isAlive()) {
                serverThread.interrupt(); // Interrupt server thread if running
            }
            if (clientThread != null && clientThread.isAlive()) {
                clientThread.interrupt(); // Interrupt client thread if running
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //redirect home pg
        try {
            // Load the new scene
            Parent secondaryRoot = FXMLLoader.load(getClass().getResource("/scene/ClientCallHomeScene.fxml"));

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
    }
}

