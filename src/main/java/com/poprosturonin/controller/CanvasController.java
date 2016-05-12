/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.ImageToolkit;
import com.poprosturonin.model.design.DesignVector;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * CanvasController controls middle section of main application window.
 * It has to capture mouse movement on canvas and have ability to display images.
 */
public class CanvasController {
    @FXML
    private ImageView canvas;

    private DesignController designController;

    public void setImage(Image image) {
        canvas.setImage(ImageToolkit.scale(image, (int) canvas.getFitWidth(), (int) canvas.getFitHeight()));
    }

    public DesignVector getCanvasSize() {
        return new DesignVector((int) canvas.getFitWidth(), (int) canvas.getFitHeight());
    }

    public void setDesignController(DesignController designController) {
        this.designController = designController;
    }
}
