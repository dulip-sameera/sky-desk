package com.skydesk.client;

import com.skydesk.shared.protocol.FileShareProtocol;

import java.io.*;
import java.net.Socket;

public class Client {
    private static String HOST = "127.0.0.1";
    private static int PORT = 8080;
    private static Socket socket;

    public static void connect() {
        connect(HOST, PORT);
    }

    public static void connect(String host, int port) {
        try {
            socket = new Socket(host, port);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void disconnect() {
        try {
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sendFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File not found");
            return;
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int bytesRead = 0;
            while (bytesRead != -1) {
                byte[] buffer = new byte[1024];
                bytesRead = bis.read(buffer);
//                byte[] chunk = new byte[1024];
//                System.arraycopy(buffer, 0, chunk, 0, bytesRead);
                oos.writeObject(new FileShareProtocol(file.getName(), file.length(), buffer));
                oos.flush();
            }
            System.out.println("File Upload Complete : " + file.getName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
