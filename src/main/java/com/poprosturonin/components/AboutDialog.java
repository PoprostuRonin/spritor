/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.components;

import com.poprosturonin.controller.Utility;
import com.poprosturonin.model.Main;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * About program dialog
 */
public class AboutDialog extends Dialog {
    public AboutDialog() {
        getDialogPane().setId("aboutDialog");

        setTitle("About");
        Utility.setDefaultIcon(this);

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        //Logo
        HBox logoHBox = new HBox(5);
        logoHBox.setPadding(new Insets(20.0));
        //Logo ImageView
        ImageView logoImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/spritor.png")));
        logoImageView.setFitWidth(60);
        logoImageView.setFitHeight(60);
        //Logo Label
        Label logoLabel = new Label("Spritor");
        logoLabel.setFont(Font.font("System", 36));
        //Logo final
        logoHBox.getChildren().addAll(logoImageView, logoLabel);
        vBox.getChildren().add(logoHBox);

        //About text
        Label about = new Label("Sprite generator");
        vBox.getChildren().add(about);

        Label about2 = new Label("This software is licensed under MIT License");
        vBox.getChildren().add(about2);

        Label about3 = new Label("Official website:");
        vBox.getChildren().add(about3);

        Hyperlink hyperlink = new Hyperlink("https://github.com/PoprostuRonin/spritor");
        hyperlink.setOnAction((ActionEvent event) ->
                Main.hostServices.showDocument("https://github.com/PoprostuRonin/spritor")
        );
        vBox.getChildren().add(hyperlink);

        //Final composition
        stackPane.getChildren().add(vBox);

        getDialogPane().setContent(stackPane);

        ButtonType buttonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(buttonType);
    }
}
