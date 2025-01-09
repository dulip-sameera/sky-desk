package com.skydesk.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

public class ServerControlAppInitializer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Server started");
        while (true) {
            System.out.println("Waiting for connection");
            Socket localSocket = serverSocket.accept();
            System.out.println("Client connected from " + localSocket.getRemoteSocketAddress());

            new Thread(() -> {
                try {
                    OutputStream os = localSocket.getOutputStream();
//                    System.out.println(os.toString());
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    ObjectOutputStream oos = new ObjectOutputStream(bos);

                    while (true) {
                        Robot robot = new Robot();
                        BufferedImage screen =  robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
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
                try {
                    InputStream isCoords = localSocket.getInputStream();
                    BufferedInputStream bisCoords = new BufferedInputStream(isCoords);
                    ObjectInputStream oisCoords = new ObjectInputStream(bisCoords);

                    Robot robot = new Robot();
                    while (true) {
                        Point coordinates = (Point) oisCoords.readObject();
                        robot.mouseMove(coordinates.x, coordinates.y);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {

                try {
                    InputStream isKeys = localSocket.getInputStream();
                    BufferedInputStream bisKeys = new BufferedInputStream(isKeys);
                    ObjectInputStream oisKeys = new ObjectInputStream(bisKeys);

                    Robot robot = new Robot();

                    while (true) {
                        Key keys = (Key) oisKeys.readObject();
                        robot.keyPress(keys.hashCode());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }


    }
}
