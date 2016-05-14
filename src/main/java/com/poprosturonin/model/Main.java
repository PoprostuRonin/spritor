/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model;

import com.poprosturonin.controller.MainController;
import com.poprosturonin.model.assets.AssetGroup;
import com.poprosturonin.model.assets.AssetManager;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    public static Stage stage;
    public static AssetManager assetManager;
    public static HostServices hostServices;
    public static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.stage = primaryStage;
        Main.hostServices = getHostServices();

        System.out.println(this.getClass().getResource("/fxml/main.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        //Create window
        primaryStage.setTitle("Spritor");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/spritor.png")));
        Scene scene = new Scene(root, 1280, 800);
        Main.scene = scene;

        assetManager = new AssetManager();
        assetManager.load();

        //Display groups
        ArrayList<AssetGroup> groups = assetManager.getAssetGroups();
        for (AssetGroup group : groups) {
            mainController.getAssetsPane().displayAssetGroup(group);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
