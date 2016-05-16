/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Basic test for main controller
 */
public class MainControllerTest extends ApplicationTest {
    private static boolean setUp = false;
    private MainController mainController;

    @Test
    public void getAssetsPane() throws Exception {
        assertNotNull(mainController.getAssetsPane());
    }

    @Test
    public void getCanvasPane() throws Exception {
        assertNotNull(mainController.getCanvasPane());
    }

    @Test
    public void getDesignPane() throws Exception {
        assertNotNull(mainController.getDesignPane());
    }

    @Test
    public void newButton() throws Exception {
        clickOn("File");
        clickOn("New");
        verifyThat("#newProjectDialog", Node::isVisible);
        closeCurrentWindow();
    }

    @Test
    public void aboutButton() throws Exception {
        clickOn("Help");
        clickOn("About");
        verifyThat("#aboutDialog", Node::isVisible);
        closeCurrentWindow();
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.getController();

        //Create window
        stage.setTitle("Spritor");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/spritor.png")));
        Main.stage = stage;

        Scene scene = new Scene(root, 1280, 800);

        stage.setScene(scene);
        stage.show();
    }
}