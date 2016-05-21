/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.ImageToolkit;
import com.poprosturonin.model.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Basic test for CanvasController
 */
public class CanvasControllerTest extends ApplicationTest {
    CanvasController canvasController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/canvaspane.fxml"));
        Parent root = fxmlLoader.load();
        canvasController = fxmlLoader.getController();

        //Create window
        stage.setTitle("Spritor CanvasPane");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/spritor.png")));
        Main.stage = stage;

        Scene scene = new Scene(root, 1280, 800);

        Main.scene = scene;
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void imageIsVisible() {
        verifyThat("#canvas", Node::isVisible);
    }

    @Test
    public void imageIsChanging() {
        WritableImage testImage = new WritableImage(4, 4);
        canvasController.setImage(testImage);

        NodeQuery query = lookup("#canvas");

        ImageView node = query.query();

        Image image = node.getImage();

        Assert.assertTrue(image.getWidth() == 200);
        Assert.assertTrue(image.getHeight() == 200);

        WritableImage scaledTestImage = ImageToolkit.scale(testImage, 200, 200);

        PixelReader testImagePixelReader = testImage.getPixelReader();
        PixelReader pixelReader = image.getPixelReader();
        for (int i = 0; i < testImage.getWidth(); i++) {
            for (int j = 0; j < testImage.getHeight(); j++) {
                Assert.assertTrue(testImagePixelReader.getColor(i, j).equals(pixelReader.getColor(i, j)));
            }
        }
    }
}
