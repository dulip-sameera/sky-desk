package com.skydesk.server;

import javafx.scene.input.MouseButton;

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
                    Object button = ois.readObject();
                    if(button instanceof Point coordinates) {
                        robot.mouseMove(coordinates.x, coordinates.y);
                    }else if(button.toString().equals("PRIMARY")) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }else if(button.toString().equals("SECONDARY")) {
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    }


                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();



    }

}
