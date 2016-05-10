/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.model;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.com.poprosturonin.controller.MainController;
import main.java.com.poprosturonin.model.assets.AssetGroup;
import main.java.com.poprosturonin.model.assets.AssetManager;

import java.util.ArrayList;

public class Main extends Application {
    public static Stage stage;
    public static AssetManager assetManager;
    public static HostServices hostServices;

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
        primaryStage.getIcons().add(new Image("/images/spritor.png"));
        Scene scene = new Scene(root, 1280, 800);

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