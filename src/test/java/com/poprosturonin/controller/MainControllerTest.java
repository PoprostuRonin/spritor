/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.google.common.base.Predicate;
import com.poprosturonin.model.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import static javafx.scene.input.KeyCode.T;
import static org.junit.Assert.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Basic test for main controller
 */
public class MainControllerTest extends ApplicationTest {
    private static boolean setUp = false;
    private MainController mainController;

    @BeforeClass
    public static void setupSpec() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

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

        verifyThat("File", (Node input) -> {
            return (input instanceof MenuButton) && ((MenuButton) input).isShowing();
        });

        clickOn("New");

        NodeQuery query = lookup("#newProjectDialog");

        verifyThat(query, Node::isVisible);

        Node node = query.query();
        interact(() -> node.getScene().getWindow().hide()); //Hide dialog
        sleep(100);
    }

    @Test
    public void aboutButton() throws Exception {
        clickOn("Help");

        verifyThat("Help", (Node input) -> {
            return (input instanceof MenuButton) && ((MenuButton) input).isShowing();
        });

        clickOn("About");

        NodeQuery query = lookup("#aboutDialog");

        verifyThat(query, Node::isVisible);

        Node node = query.query();
        interact(() -> node.getScene().getWindow().hide()); //Hide dialog
        sleep(100);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.getController();

        //Create window
        stage.setTitle("Spritor");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/spritor.png")));
        Main.stage = stage;

        Scene scene = new Scene(root, 1280, 800);

        Main.scene = scene;
        stage.setScene(scene);
        stage.show();
    }
}