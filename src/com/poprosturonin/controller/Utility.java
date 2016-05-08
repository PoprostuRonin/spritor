/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.Main;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * If it is useful, but it doesn't match anything
 */
public class Utility {
    /** Sets anchor for given node
     *
     * @param child given node
     * @param value value to set as anchor
     */
    static void setAnchorAll(Node child, double value) {
        AnchorPane.setTopAnchor(child, value);
        AnchorPane.setBottomAnchor(child, value);
        AnchorPane.setRightAnchor(child, value);
        AnchorPane.setLeftAnchor(child, value);
    }

    public static String getStyleWrong() {
        return "-fx-text-fill: red;";
    }

    /** Short code for simple error alert
     *
     * @param errorMessage message to display
     * @return created alert
     */
    public static Alert errorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(errorMessage);
        return alert;
    }

    /** Short code for simple error alert
     *
     * @param title title of alert's window
     * @param errorMessage message to display
     * @return created alert
     */
    public static Alert errorAlert(String title, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(errorMessage);
        return alert;
    }

    /** Short code for exception alert
     *
     * @param exception message to display
     * @return created alert
     */
    public static Alert exceptionAlert(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("Oops, we got a problem");
        alert.setContentText(exception.getLocalizedMessage());

        // Create expandable Exception.
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String exceptionText = stringWriter.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        return alert;
    }

    /** Short code for exception alert
     *
     * @param exception exception to display
     * @param message message to display
     * @return created alert
     */
    public static Alert exceptionAlert(Exception exception, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("Oops, we got a problem");
        alert.setContentText(message);

        // Create expandable Exception.
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        String exceptionText = stringWriter.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        return alert;
    }

    /**
     * This magic method add icon from main window to our dialog.
     * @param dialog dialog to give icon
     */
    public static void setDefaultIcon(Dialog dialog) {
        ((Stage) dialog.getDialogPane().getScene().getWindow()).getIcons().add(Main.stage.getIcons().get(0));
    }
}
