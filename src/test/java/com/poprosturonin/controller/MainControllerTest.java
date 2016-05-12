/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import static org.junit.Assert.assertNotNull;

/**
 * Basic test for main controller
 */
public class MainControllerTest {
    private static boolean setUp = false;
    private MainController mainController;

    @org.junit.Before
    public void setUp() throws Exception {
        if (!setUp) {
            setUp = true;
            Thread t = new Thread("JavaFX Init Thread") {
                public void run() {
                    Application.launch(Main.class);
                }
            };
            t.setDaemon(true);
            t.start();
        }


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.getController();
    }

    @org.junit.Test
    public void getAssetsPane() throws Exception {
        assertNotNull(mainController.getAssetsPane());
    }

    @org.junit.Test
    public void getCanvasPane() throws Exception {
        assertNotNull(mainController.getCanvasPane());
    }

    @org.junit.Test
    public void getDesignPane() throws Exception {
        assertNotNull(mainController.getDesignPane());
    }
}