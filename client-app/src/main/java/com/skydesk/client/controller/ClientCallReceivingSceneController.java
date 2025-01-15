package com.skydesk.client.controller;

public class ClientCallReceivingSceneController {

    private volatile boolean isServerRunning = true; // Flag to control server thread
    private volatile boolean isClientRunning = true; // Flag to control client thread
    private Thread serverThread;
    private Thread clientThread;

    public void initialize() {

    }

}
