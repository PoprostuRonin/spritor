/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.components;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Pair;
import main.java.com.poprosturonin.controller.Utility;
import main.java.com.poprosturonin.model.Generator;
import main.java.com.poprosturonin.model.Main;

import java.io.File;

/**
 * Dialog used to start generating. Doesn't show process of {@link Generator}
 * As result returns pair of amount sprites to generate and destination, both values are valid.
 */
public class GenerateDialog extends Dialog<Pair<Integer, File>> {
    private File destination;
    private int amount = 0;

    public GenerateDialog() {
        setTitle("Generate");
        Utility.setDefaultIcon(this);

        VBox vBox = new VBox(10.0);
        vBox.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(10.0);
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label("Target number");
        TextField textField = new TextField();
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            //TextField validation
            try {
                amount = Integer.parseInt(observable.getValue());
                if (amount < 0)
                    textField.setStyle(Utility.getStyleWrong());
                else
                    textField.setStyle(null);
            } catch (NumberFormatException e) {
                amount = 0;
                textField.setStyle(Utility.getStyleWrong());
            }
        });
        hBox.getChildren().addAll(label, textField);
        vBox.getChildren().add(hBox);

        //Directory selection button
        Button button = new Button("Select destination");
        button.setOnAction((event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select destination");
            destination = directoryChooser.showDialog(Main.stage);
        }));
        vBox.getChildren().add(button);

        //Generate button
        ButtonType generateButton = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(generateButton);
        getDialogPane().setContent(vBox);

        //Result converter
        setResultConverter(dialogButton -> {
            if (dialogButton == generateButton) {
                if (amount > 0 && destination != null && destination.isDirectory()) {
                    return new Pair<>(amount, destination);
                } else if (amount <= 0) {
                    Utility.errorAlert("Amount should be a number and be bigger than zero.");
                } else if (destination == null) {
                    Utility.errorAlert("Destination was not set.");
                } else if (!destination.isDirectory()) {
                    Utility.errorAlert("Destination should be a directory.");
                }
            }
            return null;
        });
    }
}
