package com.skydesk.client.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;

public class FileTransferSceneController {
    public AnchorPane root;
    public Label lblRemote;
    public TreeView trRemoteFileSystem;
    public Button btnDownload;
    public AnchorPane pnDropTarget;
    public ImageView imgDrop;
    public Button btnSelectFile;
    public Button btnUpload;

    public void btnDownloadOnAction(ActionEvent actionEvent) {
    }

    public void btnSelectFileOnAction(ActionEvent actionEvent) {
        File initialDirectory = new File(System.getProperty("user.home"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());

    }

    public void btnUploadOnAction(ActionEvent actionEvent) {
    }
}
