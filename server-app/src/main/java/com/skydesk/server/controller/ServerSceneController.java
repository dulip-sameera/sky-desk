package com.skydesk.server.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSceneController {
    public Label lblServerStarted;
    public AnchorPane root;
    public Button btnStartServer;
    private ServerSocket serverSocket;

    public void initialize() {

    }

    public void btnStartServerOnAction(ActionEvent actionEvent) throws Exception {
        startServer();

    }

    public void startServer() throws Exception {

        serverSocket = new ServerSocket(9090);
        lblServerStarted.setText("Server started");
        System.out.println("Server started on port 9090");
        System.out.println("Waiting for connections...");
        Socket localSocket = serverSocket.accept();
        System.out.println("Accepted Connection from " + localSocket.getRemoteSocketAddress());
        new Thread(() -> {
            try {
                OutputStream os = localSocket.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(os);
                ObjectOutputStream oos = new ObjectOutputStream(bos);


                while (true) {
                    Robot robot = new Robot();
                    BufferedImage screen = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(screen, "jpeg", baos);
                    oos.writeObject(baos.toByteArray());
                    oos.flush();
                    Thread.sleep(1000/27);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try{
                InputStream is = localSocket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);

                Robot robot = new Robot();
                while (true) {
                    Point coordinates = (Point) ois.readObject();
                    robot.mouseMove(coordinates.x, coordinates.y);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
