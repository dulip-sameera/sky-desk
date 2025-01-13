package com.skydesk.client.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
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

    private File selectedFile;

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

        // no file selected
        if (file == null) return;

        // check for permissions
        if (!file.canRead()) {
            alertPermissionDenied();
            return;
        }
        selectedFile = file;

    }

    public void btnUploadOnAction(ActionEvent actionEvent) {
    }

    public void btnDownloadOnAction(ActionEvent actionEvent) {
    }

    private void alertPermissionDenied() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Permission Denied");
        alert.setHeaderText("Permission Denied");
        alert.setContentText("You do not have permission to upload the file.");
        alert.showAndWait();
    }

    public void pnDropTargetOnDragOver(DragEvent dragEvent) {
        System.out.println("Drag over");

        dragEvent.acceptTransferModes(TransferMode.COPY);
    }

    public void pnDropTargetOnDragEntered(DragEvent dragEvent) {
        System.out.println("Drag entered");
    }

    public void pnDropTargetOnDragExited(DragEvent dragEvent) {
        System.out.println("Drag exited");
    }

    public void pnDropTargetOnDragDropped(DragEvent dragEvent) {
        System.out.println("Drag dropped");
    }
}