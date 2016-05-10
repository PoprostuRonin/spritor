/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.components;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import main.java.com.poprosturonin.controller.Utility;

/**
 * New project
 */
public class NewProjectDialog extends Dialog<Pair<Integer, Integer>> {
    private final int defaultSize = 16;

    public NewProjectDialog() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10.0);
        gridPane.setHgap(10.0);

        setTitle("New");
        Utility.setDefaultIcon(this);

        Label canvasWidthLabel = new Label("Canvas width");
        TextField canvasWidthTextField = new TextField();
        canvasWidthTextField.setPrefWidth(60);
        canvasWidthTextField.setText(Integer.toString(defaultSize)); //Default value
        canvasWidthTextField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) ->
                {
                    //Text validation
                    try {
                        int value = Integer.parseInt(canvasWidthTextField.getText());
                        if (value < 0)
                            canvasWidthTextField.setStyle(Utility.getStyleWrong());
                        else
                            canvasWidthTextField.setStyle(null);
                    } catch (NumberFormatException e) {
                        canvasWidthTextField.setStyle(Utility.getStyleWrong());
                    }
                });

        Label canvasHeightLabel = new Label("Canvas height");
        TextField canvasHeightTextField = new TextField();
        canvasHeightTextField.setPrefWidth(60);
        canvasHeightTextField.setText(Integer.toString(defaultSize)); //Default value
        canvasHeightTextField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) ->
                {
                    //Text validation
                    try {
                        int value = Integer.parseInt(canvasHeightTextField.getText());
                        if (value < 0)
                            canvasHeightTextField.setStyle(Utility.getStyleWrong());
                        else
                            canvasHeightTextField.setStyle(null);
                    } catch (NumberFormatException e) {
                        canvasHeightTextField.setStyle(Utility.getStyleWrong());
                    }
                });

        //Fill GridPane
        gridPane.add(canvasWidthLabel, 0, 0);
        gridPane.add(canvasWidthTextField, 1, 0);

        gridPane.add(canvasHeightLabel, 0, 1);
        gridPane.add(canvasHeightTextField, 1, 1);

        //Generate button
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(createButtonType);
        getDialogPane().setContent(gridPane);

        //Result converter
        setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    int width = Integer.parseInt(canvasWidthTextField.getText());
                    int height = Integer.parseInt(canvasHeightTextField.getText());
                    if (width <= 0 || height <= 0) {
                        Utility.errorAlert("Width and height must be integer, bigger than zero.");
                        return null;
                    }
                    return new Pair<>(width, height);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Utility.errorAlert("Width and height must be integer, bigger than zero.");
                    return null;
                }
            } else
                return null;
        });
    }
}
