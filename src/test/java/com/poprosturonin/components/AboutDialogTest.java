/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.components;

import com.poprosturonin.JavaFXThreadingRule;
import com.poprosturonin.model.Main;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Test for 'About' dialog.
 */
public class AboutDialogTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    @Test
    public void content() throws Exception {
        if (Main.stage == null) {
            Main.stage = new Stage();

            Image image = new Image(getClass().getResourceAsStream("/images/spritor.png"));
            assertNotNull(image);

            Main.stage.getIcons().add(image);
        }

        AboutDialog aboutDialog = new AboutDialog();
        assertNotNull(aboutDialog);

        StackPane stackPane = (StackPane) (aboutDialog.getDialogPane().getContent());
        assertNotNull(stackPane);

        VBox vBox = (VBox) stackPane.getChildren().get(0);
        assertNotNull(vBox);
    }

    @After
    public void tearDown() throws Exception {

    }
}