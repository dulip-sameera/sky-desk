package com.skydesk.server;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAppInitializer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Server started on port 9090");
        System.out.println("Waiting for connections...");
        Socket localSocket = serverSocket.accept();
        System.out.println("Accepted Connection from " + localSocket.getRemoteSocketAddress());
        new Thread(() -> {
            try {
                OutputStream os = localSocket.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(os);
                ObjectOutputStream oos = new ObjectOutputStream(bos);

                BufferedImage screen;
                screen = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                oos.writeObject(screen.getWidth());
                oos.writeObject(screen.getHeight());


                while (true) {
                    Robot robot = new Robot();
                    screen = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(screen, "jpeg", baos);
                    oos.writeObject(baos.toByteArray());
                    oos.flush();
                    Thread.sleep(1000 / 27);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try {
                InputStream is = localSocket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ObjectInputStream ois = new ObjectInputStream(bis);

                Robot robot = new Robot();
                boolean dragging = false;

                while (true) {
                    Object event = ois.readObject();

                    if (event instanceof Point coordinates) {
                        robot.mouseMove(coordinates.x, coordinates.y);
                    } else if (event instanceof String button) {
                        switch (button) {
                            case "PRIMARY_PRESS":
                                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                                dragging = true;
                                break;
                            case "PRIMARY_RELEASE":
                                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                                dragging = false;
                                break;
                            case "SECONDARY_PRESS":
                                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                                break;
                            case "SECONDARY_RELEASE":
                                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                                break;
                            case "MIDDLE_PRESS":
                                robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                                break;
                            case "MIDDLE_RELEASE":
                                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                                break;
                        }
                    } else if (event instanceof Double scrollAmount) {
                        robot.mouseWheel((int) (double) scrollAmount);
                    } else if (event instanceof Point dragEvent) {
                        if (dragging) {
                            robot.mouseMove(dragEvent.x, dragEvent.y);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
