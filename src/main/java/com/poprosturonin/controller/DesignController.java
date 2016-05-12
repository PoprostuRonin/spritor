/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.ImageToolkit;
import com.poprosturonin.model.design.Element;
import com.poprosturonin.model.design.ElementGroup;
import com.poprosturonin.model.design.Project;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The most important of all controllers. Interacts with user to design sprite.
 * Holds {@link ElementGroupListController} and {@link ElementEditController}
 */
public class DesignController implements Initializable {
    private final String styleCurrent = "-fx-base: #b6e7c9;";

    private CanvasController canvasController;
    private ElementEditController elementEditController;
    private ElementGroupListController elementGroupListController;

    //Designing
    private Element currentElement;
    private Project project;
    private WritableImage canvas;
    @FXML
    private AnchorPane topPane;
    @FXML
    private AnchorPane bottomPane;
    @FXML
    private SplitPane splitPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Load element edit
        elementEditController = loadElementEdit();

        //Load element groups (as view)
        elementGroupListController = loadElementGroups();
        if (elementGroupListController != null)
            elementGroupListController.setDesignController(this);

        //Set up timer for looped render method
        Timeline timer = new Timeline(new KeyFrame(javafx.util.Duration.millis(1000), ae -> render()));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    /**
     * Loads view with {@link ElementEditController} to be able to edit Elements data
     * @return loaded {@link ElementEditController}
     */
    private ElementEditController loadElementEdit() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/elementedit.fxml"));
            Parent root = fxmlLoader.load();
            bottomPane.getChildren().add(root);
            Utility.setAnchorAll(root, 0);
            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.exceptionAlert(e);
            return null;
        }
    }

    /**
     * Loads view with {@link ElementGroupListController} to be able to edit ElementGroups in project
     * @return loaded {@link ElementGroupListController}
     */
    private ElementGroupListController loadElementGroups() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/elementgroups.fxml"));
            Parent root = fxmlLoader.load();
            topPane.getChildren().add(root);
            Utility.setAnchorAll(root, 0);
            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            Utility.exceptionAlert(e);
            return null;
        }
    }

    /**
     * Puts new element in project
     * @param element new element to be put in project
     */
    void putNew(Element element) {
        if(project != null) {
            project.putNew(element);
            setCurrentElement(element);
        }
    }

    /**
     * Sets new currently edited element
     * @param element new element to set as current
     */
    public void setCurrentElement(Element element) {
        if (currentElement != element) {
            elementEditController.setCurrentElement(element);
            if (currentElement != null) { //Reset style of old button
                Button button = elementGroupListController.getButtonForElement(currentElement);
                if (button != null)
                    button.setStyle(null);
            }
            currentElement = element;
        }

        //Paint new button (or still old one)
        Button button = elementGroupListController.getButtonForElement(currentElement);
        if (button != null)
            button.setStyle(styleCurrent);

        render(); //Update without any delay
    }

    /**
     * Renders project in Design mode, outputs image to method {@link CanvasController#setImage(javafx.scene.image.Image)}
     */
    private void render() {
        if(canvas != null && project != null) {
            //Clean canvas
            PixelWriter writer = canvas.getPixelWriter();
            for (int i = 0; i < canvas.getWidth(); i++) {
                for (int j = 0; j < canvas.getHeight(); j++) {
                    writer.setColor(i, j, Color.TRANSPARENT);
                }
            }

            ObservableList<ElementGroup> elementGroups = project.getGroups();
            for (int i = elementGroups.size() - 1; i >= 0; i--) //Read from end
            {
                ElementGroup elementGroup = elementGroups.get(i);
                Element elementToDraw = null;

                //Get proper element to draw
                if (elementGroup.getElements().contains(currentElement)) {
                    elementToDraw = currentElement;
                } else {
                    if (elementGroup.getElements().size() > 0) {
                        elementToDraw = elementGroup.getRandomElement();
                    }
                }

                //If we got something
                if (elementToDraw != null) {
                    canvas = ImageToolkit.applyAllEffects(elementToDraw, canvas);
                }
            }

            //TO-DO: make borders to show move vectors
            canvasController.setImage(canvas);
        }
    }

    void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
    }

    Project getProject() {
        return project;
    }

    void setProject(Project project) {
        if(this.project == null) {
            this.project = project;

            //Set new project to the child controllers
            elementGroupListController.setProject(project);
            elementEditController.setCurrentElement(null);

            canvas = new WritableImage(project.getCanvasWidth(), project.getCanvasHeight());
        }
        else
            closeProject();
    }

    boolean hasProject() {
        return project != null;
    }

    void closeProject() {
        //Clean
        elementGroupListController.clear();
        elementEditController.setCurrentElement(null);

        canvas = null;
        project = null;
    }


}
