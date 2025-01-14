package com.skydesk.server;

import com.skydesk.shared.protocol.FileShareProtocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int PORT = 8080;
    private static String FILE_DOWNLOAD_PATH = System.getProperty("user.home") + "/Downloads/SkyDesk/";
    private static ServerSocket serverSocket = null;
    private static Socket localSocket = null;

    public static void start() {
        start(PORT, FILE_DOWNLOAD_PATH);

    }

    public static void start(int port, String fileDownloadPath) {
        PORT = port;
        FILE_DOWNLOAD_PATH = fileDownloadPath;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            while (true) {
                try {
                    localSocket = serverSocket.accept();
                    BufferedInputStream bis = new BufferedInputStream(localSocket.getInputStream());
                    ObjectInputStream ois = new ObjectInputStream(bis);


                    Object respond = ois.readObject();

                    // file share download file
                    if (respond instanceof FileShareProtocol fileShare) {
                       new Thread(() -> {
                           File file = new File(fileDownloadPath, fileShare.fileName());
                           try {
                               FileOutputStream fos = new FileOutputStream(file);
                               BufferedOutputStream bos = new BufferedOutputStream(fos);
                               long totalBytesReceived = 0;

                               while (totalBytesReceived < fileShare.fileSize()) {
                                   if (ois.readObject() instanceof FileShareProtocol fileShareProtocol) {
                                       byte[] content = fileShareProtocol.fileContent();
                                       bos.write(content);
                                       bos.flush();
                                       totalBytesReceived += content.length;
                                   }
                               }

                           } catch (FileNotFoundException e) {
                               throw new RuntimeException(e);
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           } catch (ClassNotFoundException e) {
                               throw new RuntimeException(e);
                           }
                       }).start();
                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



//    private final static int SERVER_PORT = 8080;
//
//    private static ServerSocket serverSocket;
//
//
//    /*
//        File Share Protocol
//        fileName: String
//        fileSize: long
//        fileContent: byte[]
//     */
//
//    public static void start(File saveLocation) throws IOException {
//        try {
//            serverSocket = new ServerSocket(SERVER_PORT);
//        } catch (IOException e) {
//            try {
//                serverSocket = new ServerSocket(0);
//            } catch (IOException ex) {
//                System.err.println("Could not start the server");
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Server started on port " + serverSocket.getLocalPort());
//        while (true) {
//            System.out.println("Waiting for connection...");
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
//                        File downloadingFilePoint = new File(saveLocation, fileName);
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
//
//    public static void close() {
//        if (serverSocket != null) {
//            try {
//                serverSocket.close();
//            } catch (IOException e) {
//                System.err.println("Could not close the server");
//                e.printStackTrace();
//            }
//        }
//    }



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
