package org.jala.edu.core.infrastructure.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Modal windows and confirmations that inherit the owner window's stylesheets.
 */
public final class Dialogs {

    private Dialogs() {
    }

    /**
     * Shows {@code content} in a window that blocks its owner until closed.
     */
    public static void showModal(Node owner, String title, Parent content) {
        Scene ownerScene = owner.getScene();
        Scene scene = new Scene(content);
        scene.getStylesheets().setAll(ownerScene.getStylesheets());

        Stage stage = new Stage();
        stage.initOwner(ownerScene.getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Asks the user to confirm an action; returns {@code true} only when they choose OK.
     */
    public static boolean confirm(Node owner, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.OK, ButtonType.CANCEL);
        alert.initOwner(owner.getScene().getWindow());
        alert.setTitle("Confirm");
        alert.setHeaderText(header);
        alert.getDialogPane().getStylesheets().setAll(owner.getScene().getStylesheets());
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }

    /**
     * Closes the window that contains {@code node}.
     */
    public static void close(Node node) {
        ((Stage) node.getScene().getWindow()).close();
    }
}
