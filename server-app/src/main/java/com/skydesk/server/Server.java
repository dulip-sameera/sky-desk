package com.skydesk.server;

import com.skydesk.shared.protocol.FileShareProtocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int SERVER_PORT = 8080;

    private static ServerSocket serverSocket;


    /*
        File Share Protocol
        fileName: String
        fileSize: long
        fileContent: byte[]
     */

    public static void start(File saveLocation) throws IOException {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            try {
                serverSocket = new ServerSocket(0);
            } catch (IOException ex) {
                System.err.println("Could not start the server");
                e.printStackTrace();
            }
        }
        System.out.println("Server started on port " + serverSocket.getLocalPort());
        while (true) {
            System.out.println("Waiting for connection...");
            Socket localSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + localSocket.getRemoteSocketAddress());

            new Thread(() -> {
                try {
                    InputStream inputStream = localSocket.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

                    if (objectInputStream.readObject() instanceof FileShareProtocol(
                            String fileName, Long fileSize, byte[] fileContent
                    )) {
                        if (fileName == null || fileName.isEmpty()) {
                            return;
                        }

                        if (fileSize == null) {
                            return;
                        }

                        if (fileContent == null) {
                            return;
                        }

                        File downloadingFilePoint = new File(saveLocation, fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(downloadingFilePoint);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                        bufferedOutputStream.write(fileContent, 0, fileContent.length);
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();

                    }

                } catch (EOFException e) {
                    System.err.println("Connection Lost");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close the server");
                e.printStackTrace();
            }
        }
    }



//    public static void downloadFile(File file, File location) throws IOException {
//        while (true) {
//            Socket localSocket = serverSocket.accept();
//            System.out.println("Accepted connection from " + localSocket.getRemoteSocketAddress());
//
//            new Thread(() -> {
//                try {
//                    InputStream inputStream = localSocket.getInputStream();
//                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//                    ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
//
//                    if (objectInputStream.readObject() instanceof FileShareProtocol(
//                            String fileName, Long fileSize, byte[] fileContent
//                    )) {
//                        if (fileName == null || fileName.isEmpty()) {
//                            return;
//                        }
//
//                        if (fileSize == null) {
//                            return;
//                        }
//
//                        if (fileContent == null) {
//                            return;
//                        }
//
//                        File downloadingFilePoint = new File(location, fileName);
//                        FileOutputStream fileOutputStream = new FileOutputStream(downloadingFilePoint);
//                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
//
//                        bufferedOutputStream.write(fileContent, 0, fileContent.length);
//                        bufferedOutputStream.flush();
//                        bufferedOutputStream.close();
//
//                    }
//
//                } catch (EOFException e) {
//                    System.err.println("Connection Lost");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
//    }
}
