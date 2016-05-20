/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.Main;
import com.poprosturonin.model.assets.Asset;
import com.poprosturonin.model.assets.AssetGroup;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Basic test for {@link AssetsController}
 */
public class AssetsControllerTest extends ApplicationTest {
    private AssetsController assetsController;

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

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/assetspane.fxml"));
        Parent root = fxmlLoader.load();
        assetsController = fxmlLoader.getController();

        //Create window
        stage.setTitle("Spritor AssetPane");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/spritor.png")));
        Main.stage = stage;

        Scene scene = new Scene(root, 1280, 800);

        Main.scene = scene;
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void accordionIsVisible() throws Exception {
        verifyThat("#assetsAccordion", Node::isVisible);
    }

    @Test
    public void groupAdded() throws Exception {
        final String name = "group!@#$%&^(6";
        AssetGroup assetGroup = new AssetGroup(name);
        final Asset emptyAsset = new Asset();
        assetGroup.addElement(emptyAsset); //Add empty asset

        interact(() -> assetsController.displayAssetGroup(assetGroup));
        sleep(100);

        NodeQuery query = lookup("#assetsAccordion");

        Accordion accordion = query.query();

        //Check if pane exists
        ObservableList<TitledPane> titledPanes = accordion.getPanes();
        TitledPane ourTitledPane = null;
        for (TitledPane titledPane : titledPanes) {
            if (titledPane.getText().equals(name)) {
                ourTitledPane = titledPane;
                break;
            }
        }
        Assert.assertNotNull(ourTitledPane);

        //Check if pane contains flow pane with a button
        FlowPane flowPane = (FlowPane) ourTitledPane.getContent();

        ObservableList<Node> nodes = flowPane.getChildren();
        boolean present = false;
        for (Node node : nodes) {
            if (node instanceof Button) {
                ImageView imageView = (ImageView) ((Button) node).getGraphic();
                if (imageView.getImage().equals(emptyAsset.getThumbnail())) {
                    present = true;
                    break;
                }
            }
        }
        Assert.assertTrue(present);

        sleep(100);
    }
}
