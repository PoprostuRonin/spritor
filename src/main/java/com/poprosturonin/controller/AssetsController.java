/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import main.java.com.poprosturonin.model.assets.Asset;
import main.java.com.poprosturonin.model.assets.AssetGroup;

import java.util.ArrayList;

/**
 * Assets com.poprosturonin.controller controls left section of main application window.
 * It displays available {@link Asset}
 */
public class AssetsController {
    @FXML
    private Accordion accordion;

    private DesignController designController;

    /**
     * Displays {@link AssetGroup} as {@link javafx.scene.layout.FlowPane} under {@link javafx.scene.control.Accordion}
     * @param group to be displayed
     */
    public void displayAssetGroup(AssetGroup group) {
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.TOP_CENTER);
        flowPane.setHgap(3.0);
        flowPane.setVgap(3.0);

        //Get resources from group
        ArrayList<Asset> assets = group.getAssets();
        for (Asset asset : assets) {
            ImageView imageView = new ImageView(asset.getThumbnail());

            Button button = new Button("", imageView);
            button.setOnAction((ActionEvent action) -> designController.putNew(asset.getElement()));
            flowPane.getChildren().add(button);
        }

        TitledPane titledPane = new TitledPane(group.getName(), flowPane);
        accordion.getPanes().add(titledPane);
    }

    void setDesignController(DesignController designController) {
        this.designController = designController;
    }
}
