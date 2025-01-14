package com.skydesk.server.controller;

import com.skydesk.server.Server;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public class FileTransferServerSceneController {

    public void initialize() {
        File saveLocation = new File(System.getProperty("user.home") + "/Downloads/SkyDesk");
        saveLocation.mkdirs();


        try {
            Server.start(saveLocation);
        } catch (IOException e) {
            System.out.println("Connection Lost");
        }
    }
}
