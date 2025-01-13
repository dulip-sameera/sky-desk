package com.skydesk.client.controller;

import com.skydesk.client.util.Icons;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class FileTransferSceneController {
    public AnchorPane root;
    public Label lblRemote;
    public TreeView trRemoteFileSystem;
    public Button btnDownload;
    public StackPane pnDropTarget;
    public ImageView imgDrop;
    public Button btnSelectFile;
    public Button btnUpload;
    public Button btnRemoveUpload;
    public Label lblFileName;

    private File selectedFile;
    private final static int MAX_ACCEPTABLE_FILE_COUNT = 1;
    private final String LBL_FILE_NAME_INITIAL_TEXT = "Drag & Drop or Select a File";
    private String SERVER_HOST = "127.0.0.1";
    private int SERVER_PORT = 8080;

    public void initialize() {
        imgDrop.setImage(new Image(Icons.getPath(Icons.IconType.ICON_UPLOAD)));
        lblFileName.setText(LBL_FILE_NAME_INITIAL_TEXT);
        imgDrop.setOpacity(0.2);
        btnUpload.setDisable(true);

        // Will increase the width when the parent's width increases
        btnUpload.setMaxWidth(Double.MAX_VALUE);
        btnDownload.setMaxWidth(Double.MAX_VALUE);
        btnRemoveUpload.setMaxWidth(Double.MAX_VALUE);
        btnSelectFile.setMaxWidth(Double.MAX_VALUE);

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

        // no file selected
        if (file == null) return;

        // check for permissions
        if (!file.canRead()) {
            alertPermissionDenied();
            return;
        }
        updateFileTransferUI(file);
        selectedFile = file;

    }

    public void btnUploadOnAction(ActionEvent actionEvent) {
        Task<Void> task = new Task<>(){

            @Override
            protected Void call() throws Exception {
                Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                
            }
        };

        new Thread(task).start();
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
        Dragboard dragboard = dragEvent.getDragboard();
        List<File> files = dragboard.getFiles();

        if (files == null && files.isEmpty()) {
            return;
        }

        // don't accept if multiple files are dragged
        if (files.size() > MAX_ACCEPTABLE_FILE_COUNT) {
            return;
        }

        // don't accept if it's a directory
        for (File file : files) {
            if (file.isDirectory()) return;
        }

        dragEvent.acceptTransferModes(TransferMode.COPY);

    }

    public void pnDropTargetOnDragEntered(DragEvent dragEvent) {
        imgDrop.setOpacity(.6);

    }

    public void pnDropTargetOnDragExited(DragEvent dragEvent) {
        imgDrop.setOpacity(.2);

    }

    public void pnDropTargetOnDragDropped(DragEvent dragEvent) {
        selectedFile = dragEvent.getDragboard().getFiles().getFirst();
        updateFileTransferUI(selectedFile);

    }

    private void updateFileTransferUI(File file) {
        final String MIME_TYPE_PDF = "application/pdf";
        final String[] MIME_TYPE_IMAGES = {"image/jpeg", "image/jpg", "image/gif", "image/bmp", "image/webp", "image/png"};
        try {
            String fileMimeType = Files.probeContentType(file.toPath());
            System.out.println("MIME TYPE : " + fileMimeType);
            String iconPath = "";
            if (fileMimeType == null) {
                iconPath = Icons.getPath(Icons.IconType.DEFAULT);
            }else if (MIME_TYPE_PDF.equals(fileMimeType)) {
                iconPath = Icons.getPath(Icons.IconType.ICON_PDF);
            } else if (isImage(fileMimeType, MIME_TYPE_IMAGES)) {
                iconPath = Icons.getPath(Icons.IconType.ICON_IMAGE);
            } else {
                iconPath = Icons.getPath(Icons.IconType.DEFAULT);
            }
            imgDrop.setImage(new Image(iconPath));
        } catch (IOException e) {
            System.err.println("Error while getting file type: " + e.getMessage());
        }
        btnUpload.setDisable(false);
        pnDropTarget.setStyle("-fx-background-color: #72A0C1; -fx-background-radius: 10");
        lblFileName.setText(file.getName());
        imgDrop.setOpacity(.6);
        lblFileName.setTooltip(new Tooltip(file.getName()));

    }

    private boolean isImage(String mimeType, String[] imgMimeTypes) {
        for (String imgMimeType : imgMimeTypes) {
            if (mimeType.equals(imgMimeType)) return true;
        }
        return false;

    }

    public void btnRemoveUploadOnAction(ActionEvent actionEvent) {
        btnUpload.setDisable(true);
        pnDropTarget.setStyle("-fx-background-color:  #DCDCDC; -fx-background-radius: 10");
        lblFileName.setText(LBL_FILE_NAME_INITIAL_TEXT);
        selectedFile = null;
        imgDrop.setImage(new Image(Icons.getPath(Icons.IconType.ICON_UPLOAD)));

    }
}