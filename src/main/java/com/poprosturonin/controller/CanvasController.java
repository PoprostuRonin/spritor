/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.ImageToolkit;
import com.poprosturonin.model.Main;
import com.poprosturonin.model.design.DesignVector;
import com.poprosturonin.model.design.Element;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * CanvasController controls middle section of main application window.
 * It has to capture mouse movement on canvas and have ability to display images.
 */
public class CanvasController {
    @FXML
    private ImageView canvas;

    private DesignController designController;

    //Drag position
    private Element element;
    private double moveStartX = 0;
    private double moveStartY = 0;
    private double size = 0;
    private DesignVector originPosition;

    public void setImage(Image image) {
        canvas.setImage(ImageToolkit.scale(image, 200, 200));// (int) canvas.getFitWidth(), (int) canvas.getFitHeight()));
    }

    public DesignVector getCanvasSize() {
        return new DesignVector((int) canvas.getFitWidth(), (int) canvas.getFitHeight());
    }

    @FXML
    private void mousePressed(MouseEvent event) {
        Element currentElement = designController.getCurrentElement();
        if (currentElement != null) {
            element = currentElement;
            size = canvas.getFitWidth();
            originPosition = new DesignVector(element.position);
            moveStartX = event.getX();
            moveStartY = event.getY();
            event.consume();
        }
    }

    @FXML
    private void mouseReleased(MouseEvent event) {
        element = null;
        event.consume();
    }

    @FXML
    private void mouseDrag(MouseEvent event) {
        if (element != null) {
            designController.getElementEditController().update();
            element.position.x = originPosition.x + (int) ((event.getX() - moveStartX) / (size / 10.0));
            element.position.y = originPosition.y + (int) ((event.getY() - moveStartY) / (size / 10.0));

            designController.render();
        }
        event.consume();
    }

    @FXML
    private void mouseEntered(MouseEvent event) {
        Main.scene.setCursor(Cursor.MOVE);
        event.consume();
    }

    @FXML
    private void mouseExited(MouseEvent event) {
        Main.scene.setCursor(Cursor.DEFAULT);
        event.consume();
    }

    public void setDesignController(DesignController designController) {
        this.designController = designController;
    }
}
