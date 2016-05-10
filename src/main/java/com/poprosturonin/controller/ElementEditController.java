/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import main.java.com.poprosturonin.model.design.Element;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/**
 * Current element edit panel
 */
public class ElementEditController implements Initializable {
    private final double grainDisplayMagnifier = 8;
    private final double colorOverrideDisplayMagnifier = 1;
    private Element currentElement;
    private DecimalFormat percentFormat;
    private DecimalFormat decimalFormat;
    @FXML
    private TextField positionX;
    @FXML
    private TextField positionY;
    @FXML
    private TextField moveMaxX;
    @FXML
    private TextField moveMaxY;
    @FXML
    private TextField moveMinX;
    @FXML
    private TextField moveMinY;
    @FXML
    private TextField scaleMaxX;
    @FXML
    private TextField scaleMaxY;
    @FXML
    private TextField scaleMinX;
    @FXML
    private TextField scaleMinY;
    @FXML
    private ColorPicker colorFrom;
    @FXML
    private ColorPicker colorTo;
    @FXML
    private Slider colorOverride;
    @FXML
    private Slider grain;
    @FXML
    private Label colorOverrideLabel;
    @FXML
    private Label grainLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        percentFormat = new DecimalFormat("#.#%");
        decimalFormat = new DecimalFormat("#.##");

        setControlsState(false);

        //This is awful
        positionX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.position.x = getIntegerValue(positionX, currentElement.position.x, 0);
            }
        });
        positionY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.position.y = getIntegerValue(positionY, currentElement.position.y, 0);
            }
        });
        moveMaxX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.moveMax.x = getIntegerValue(moveMaxX, currentElement.moveMax.x, 0);
            }
        });
        moveMaxY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.moveMax.y = getIntegerValue(moveMaxY, currentElement.moveMax.y, 0);
            }
        });
        moveMinX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.moveMin.x = getIntegerValue(moveMinX, currentElement.moveMin.x, 0);
            }
        });
        moveMinY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.moveMin.y = getIntegerValue(moveMinY, currentElement.moveMin.y, 0);
            }
        });
        scaleMinX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.scaleMinX = getDoubleValue(scaleMinX, currentElement.scaleMinX, 0);
            }
        });
        scaleMinY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.scaleMinY = getDoubleValue(scaleMinY, currentElement.scaleMinY, 0);
            }
        });
        scaleMaxX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.scaleMaxX = getDoubleValue(scaleMaxX, currentElement.scaleMaxX, 0);
            }
        });
        scaleMaxY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (currentElement != null) {
                currentElement.scaleMaxY = getDoubleValue(scaleMaxY, currentElement.scaleMaxY, 0);
            }
        });
        colorTo.setOnAction((event) -> {
            if (currentElement != null) {
                currentElement.colorTo = colorTo.getValue();
            }
        });
        colorFrom.setOnAction((event) -> {
            if (currentElement != null) {
                currentElement.colorFrom = colorFrom.getValue();
            }
        });
        colorOverride.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (currentElement != null && colorOverride.getValue() <= 1 && colorOverride.getValue() >= 0) {
                currentElement.colorOverride = colorOverride.getValue();
                colorOverrideLabel.setText(formatDoublePercent(colorOverride.getValue() * colorOverrideDisplayMagnifier));
            }
        });
        grain.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (currentElement != null && grain.getValue() <= 1 && grain.getValue() >= 0) {
                currentElement.grain = grain.getValue();
                grainLabel.setText(formatDoublePercent(grain.getValue() * grainDisplayMagnifier));
            }
        });
    }

    /**
     * Change element to edit
     * @param element new element
     */
    void setCurrentElement(Element element) {
        saveValues();
        currentElement = element;
        resetErrorDisplay();
        displayElementValues();
    }

    /**
     * Reset error coloring on text fields
     */
    private void resetErrorDisplay() {
        positionX.setStyle(null);
        positionY.setStyle(null);
        moveMaxX.setStyle(null);
        moveMaxY.setStyle(null);
        moveMinX.setStyle(null);
        moveMinY.setStyle(null);
        scaleMaxX.setStyle(null);
        scaleMaxY.setStyle(null);
        scaleMinX.setStyle(null);
        scaleMinY.setStyle(null);
    }

    /**
     * Save values from text fields to element
     */
    private void saveValues() {
        if (currentElement != null) {
            currentElement.position.x = getIntegerValue(positionX, currentElement.position.x, 0);
            currentElement.position.y = getIntegerValue(positionY, currentElement.position.y, 0);
            currentElement.moveMax.x = getIntegerValue(moveMaxX, currentElement.moveMax.x, 0);
            currentElement.moveMax.y = getIntegerValue(moveMaxY, currentElement.moveMax.y, 0);
            currentElement.moveMin.x = getIntegerValue(moveMinX, currentElement.moveMin.x, 0);
            currentElement.moveMin.y = getIntegerValue(moveMinY, currentElement.moveMin.y, 0);
            currentElement.scaleMaxX = getDoubleValue(scaleMaxX, currentElement.scaleMaxX, 0);
            currentElement.scaleMaxY = getDoubleValue(scaleMaxY, currentElement.scaleMaxY, 0);
            currentElement.scaleMinX = getDoubleValue(scaleMinX, currentElement.scaleMinX, 0);
            currentElement.scaleMinY = getDoubleValue(scaleMinY, currentElement.scaleMinY, 0);
            currentElement.colorFrom = colorFrom.getValue();
            currentElement.colorTo = colorTo.getValue();
            currentElement.colorOverride = colorOverride.getValue();
            currentElement.grain = grain.getValue();
        }
    }

    /**
     * Edits text fields to display current element's data
     */
    private void displayElementValues() {
        if (currentElement != null) {
            setControlsState(true);
            positionX.setText(Integer.toString(currentElement.position.x));
            positionY.setText(Integer.toString(currentElement.position.y));

            moveMaxX.setText(Integer.toString(currentElement.moveMax.x));
            moveMaxY.setText(Integer.toString(currentElement.moveMax.y));

            moveMinX.setText(Integer.toString(currentElement.moveMin.x));
            moveMinY.setText(Integer.toString(currentElement.moveMin.y));

            scaleMaxX.setText(Double.toString(currentElement.scaleMaxX));
            scaleMaxY.setText(Double.toString(currentElement.scaleMaxY));
            scaleMinX.setText(Double.toString(currentElement.scaleMinX));
            scaleMinY.setText(Double.toString(currentElement.scaleMinY));

            colorTo.setValue(currentElement.colorTo);
            colorFrom.setValue(currentElement.colorFrom);

            colorOverride.setValue(currentElement.colorOverride);
            grain.setValue(currentElement.grain);

            colorOverrideLabel.setText(formatDoublePercent(colorOverride.getValue()));
            grainLabel.setText(formatDoublePercent(grain.getValue()));
        }
        else
            setControlsState(false);
    }

    /** Sets controls as enabled or disabled
     *
     * @param isActive controls should be enabled or disabled
     */
    private void setControlsState(boolean isActive) {
        boolean isDisabled = !isActive;
        positionX.setDisable(isDisabled);
        positionY.setDisable(isDisabled);

        moveMaxX.setDisable(isDisabled);
        moveMaxY.setDisable(isDisabled);

        moveMinX.setDisable(isDisabled);
        moveMinY.setDisable(isDisabled);

        scaleMaxX.setDisable(isDisabled);
        scaleMaxY.setDisable(isDisabled);
        scaleMinX.setDisable(isDisabled);
        scaleMinY.setDisable(isDisabled);

        colorTo.setDisable(isDisabled);
        colorFrom.setDisable(isDisabled);

        colorOverride.setDisable(isDisabled);
        grain.setDisable(isDisabled);
    }

    /**
     * Gets text from given text field and tries to parse
     * @param textField textField holding value as text
     * @param currentValue current values, returned in case of parsing failure
     * @param defaultValue default value, returned in case of emptying field
     * @return parsed integer number if successful or given current and default value
     */
    private int getIntegerValue(TextField textField, int currentValue, int defaultValue) {
        int output = currentValue;
        try {
            output = Integer.parseInt(textField.getText());
            textField.setStyle(null);
        } catch (NumberFormatException exception) {
            if (textField.getText().trim().length() <= 0) {
                textField.setText(Integer.toString(defaultValue));
                output = defaultValue;
            } else
                textField.setStyle(Utility.getStyleWrong());
        }
        return output;
    }

    /**
     * Gets text from given text field and tries to parse
     * @param textField textField holding value as text
     * @param currentValue current values, returned in case of parsing failure
     * @param defaultValue default value, returned in case of emptying field
     * @return parsed integer number if successful or given current and default value
     */
    private double getDoubleValue(TextField textField, double currentValue, double defaultValue) {
        double output = currentValue;
        try {
            output = Double.parseDouble(textField.getText());
            textField.setStyle(null);
        } catch (NumberFormatException exception) {
            if (textField.getText().trim().length() <= 0) {
                textField.setText(formatDouble2Places(defaultValue));
                output = defaultValue;
            } else
                textField.setStyle(Utility.getStyleWrong());
        }
        return output;
    }

    /**
     * Formats value to be display as percent values as fe. slider's label
     *
     * @param value value to be formatted
     * @return formatted value as string
     */
    private String formatDoublePercent(double value) {
        return percentFormat.format(value);
    }

    /**
     * Formats value to be display as number with 2 places after dot
     *
     * @param value value to be formatted
     * @return formatted value as string
     */
    private String formatDouble2Places(double value) {
        return decimalFormat.format(value);
    }
}