package com.skydesk.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerControlAppInitializer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9090);
        } catch (BindException e) {
            System.out.println("Server is already running");
            serverSocket = new ServerSocket(0);
            System.out.println("Instead, server started on port " + serverSocket.getLocalPort());

        }
        System.out.println("Server started");
        System.out.println("Waiting for connection");
        while (true) {
            Socket localSocket = serverSocket.accept();
            System.out.println("Client connected from " + localSocket.getRemoteSocketAddress());

            new Thread(() -> {
                try {
                    OutputStream os = localSocket.getOutputStream();
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    ObjectOutputStream oos = new ObjectOutputStream(bos);

                    while (true) {
                        Robot robot = new Robot();


                        BufferedImage screen =  robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//                        oos.writeObject(screen.getWidth());
//                        oos.writeObject(screen.getHeight());


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
                System.out.println("Entered to a new Thread");
                try {
                    InputStream is = localSocket.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    System.out.println("created output stream");

                    Robot robot = new Robot();

                    while (true) {
                        Object received = ois.readObject();

                        if (received instanceof Point) {
                            Point coordinates = (Point) received;
                            robot.mouseMove(coordinates.x, coordinates.y);

                        } else if (received instanceof String) {
                            String keyText = (String) received;
//                            String keyText = KeyEvent.getKeyText(keyCode);
                            System.out.print(keyText);

                            robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(keyText.charAt(0)));
                            robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(keyText.charAt(0))); // Always release the key after pressing

//                        } else if (received instanceof String) {
//                           String mouseClick = (String) received;
//                           System.out.println("Mouse clicked: " + mouseClick);
//
//                            if ("PRIMARY".equals(mouseClick)) { // Left button
//                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
//                            } else if ("SECONDARY".equals(mouseClick)) { // Right button
//                                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
//                                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
//                            } else if ("MIDDLE".equals(mouseClick)) { // Middle button
//                                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
//                                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
//                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }


    }
}
