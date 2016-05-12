/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.design.Element;
import com.poprosturonin.model.design.ElementGroup;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Single {@link ElementGroup} controller. Observes original {@link Element} list on project.
 */
class ElementGroupController implements ListChangeListener<Element> {
    private ElementGroupListController controller;

    /**
     * Related to this object {@link ElementGroup}
     */
    private ElementGroup elementGroup;

    /**
     * Map for binding buttons to {@link Element}
     */
    private Map<Element, Button> elementButtonMap = new HashMap<>();

    /**
     * Main HBox contains all {@link Element} buttons
     */
    private HBox mainHBox;

    /**
     * Main VBox contains {@link Label}, with move {@link Button} and {@link ScrollPane} with {@link #mainHBox}
     */
    private VBox mainVBox;

    ElementGroupController(Pane parent, ElementGroup elementGroup, ElementGroupListController controller, int index) {
        this.elementGroup = elementGroup;
        this.controller = controller;

        elementGroup.getElements().addListener(this);

        BorderPane borderPane = setupButtonsAndLabel();
        HBox hBox = setupElements();
        mainHBox = hBox;

        ScrollPane scrollPane = setupScrollPane();
        scrollPane.setContent(hBox);

        mainVBox = new VBox(borderPane, scrollPane);
        parent.getChildren().add(index, mainVBox);
    }

    ElementGroupController(Pane parent, ElementGroup elementGroup, ElementGroupListController controller) {
        this.elementGroup = elementGroup;
        this.controller = controller;

        elementGroup.getElements().addListener(this);

        BorderPane borderPane = setupButtonsAndLabel();
        HBox hBox = setupElements();
        mainHBox = hBox;

        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setMinViewportHeight(60);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        //If there were no elements to display
        if (hBox.getChildren().size() <= 0) {
            scrollPane.setFitToWidth(true);
        }

        mainVBox = new VBox(borderPane, scrollPane);
        parent.getChildren().add(mainVBox);
    }

    VBox getMainVBox() {
        return mainVBox;
    }

    /**
     * Gets button related to given element
     *
     * @param element given {@link Element}
     * @return button related to given element or null if doesn't exist
     */
    Button getButtonForElement(Element element) {
        return elementButtonMap.get(element);
    }

    /**
     * Called after a change has been made to an Element ObservableList in our ElementGroup
     *
     * @param c an object representing the change that was done
     * @see Change
     */
    @Override
    public void onChanged(Change c) {
        while (c.next()) {
            if (c.wasAdded()) {
                List<Element> added = c.getAddedSubList();
                addAll(added);
            } else if (c.wasRemoved()) {
                List<Element> removed = c.getRemoved();
                removeAll(removed);
            }
        }
    }

    /**
     * Adds given {@link Element} to this controller, not actually {@link ElementGroup}
     * Creates new button for it.
     *
     * @param element given {@link Element}
     */
    private void addElement(Element element) {
        Button button = newButtonForElement(element);
        mainHBox.getChildren().add(button);
        elementButtonMap.put(element, button);
    }

    /**
     * Short code, calls {@link #addElement(Element)} on whole collection
     *
     * @param elements collection
     */
    private void addAll(Collection<Element> elements) {
        for (Element element : elements)
            addElement(element);
    }

    /**
     * Removes given {@link Element} from this controller, not actually {@link ElementGroup}
     * Destroys button which was created for it. If controller doesn't have given {@link Element} does nothing.
     *
     * @param element given {@link Element}
     */
    private void removeElement(Element element) {
        mainHBox.getChildren().remove(elementButtonMap.get(element));
        elementButtonMap.remove(element);
    }

    /**
     * Short code, calls {@link #removeElement(Element)} on whole collection
     *
     * @param elements collection
     */
    private void removeAll(Collection<Element> elements) {
        for (Element element : elements)
            removeElement(element);
    }

    private ScrollPane setupScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setMinViewportHeight(60);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setFitToWidth(true);

        scrollPane.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasString() && !mainHBox.getChildren().contains(event.getGestureSource())) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        scrollPane.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String data = db.getString();
                String[] hashCodes = data.split(":");
                int hashCodeGroup = Integer.parseInt(hashCodes[0]);
                int hashCodeElement = Integer.parseInt(hashCodes[1]);

                ElementGroup foundElementGroup = controller.getProject().getGroup(hashCodeGroup);
                if (foundElementGroup != null) {
                    Element element = controller.getProject().getElement(foundElementGroup, hashCodeElement);
                    if (element != null) {
                        foundElementGroup.getElements().remove(element);
                        elementGroup.addElement(element);
                        controller.getDesignController().setCurrentElement(element);
                        success = true;
                    } else
                        Utility.errorAlert("DragAndDrop issue: element not found by hash code");
                } else
                    Utility.errorAlert("DragAndDrop issue: project not found by hash code");
            }
            event.setDropCompleted(success);
            event.consume();
        });

        return scrollPane;
    }

    /**
     * Creates all necessary buttons for {@link Element} in {@link #elementGroup}
     *
     * @return HBox which contain all buttons
     */
    private HBox setupElements() {
        HBox hBox = new HBox(5.0);

        for (Element element : elementGroup.getElements()) {
            Button button = newButtonForElement(element);

            //Add button to box
            hBox.getChildren().add(button);

            //Add to map
            elementButtonMap.put(element, button);
        }

        return hBox;
    }

    /**
     * Creates new button for element, also creates callbacks.
     *
     * @param element given element
     * @return newly created button
     */
    private Button newButtonForElement(Element element) {
        Button button = new Button("", new ImageView(element.getThumbnail()));

        //Set button callbacks
        button.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = button.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            //We put hashCodes to later find those objects, of course we could use Serializable, but it is simpler this way
            content.putString(Integer.toString(elementGroup.hashCode()) + ":" + Integer.toString(element.hashCode()));
            db.setContent(content);
            event.consume();
        });

        button.setOnAction((ActionEvent event) -> {
            controller.getDesignController().setCurrentElement(element);
        });

        return button;
    }

    /**
     * Creates name label, move buttons and wraps it in {@link BorderPane}
     *
     * @return newly created {@link BorderPane}
     */
    private BorderPane setupButtonsAndLabel() {
        Button deleteGroupButton = new Button("X");
        deleteGroupButton.setOnAction((ActionEvent event) -> {
            controller.getProject().getGroups().remove(elementGroup);
        });

        //Move up button
        Button groupUp = new Button("/\\");
        groupUp.setOnAction((ActionEvent event) -> {
            int index = controller.getProject().getGroups().indexOf(elementGroup);
            if (index > 0) {
                controller.getProject().getGroups().remove(index);
                controller.getProject().getGroups().add(index - 1, elementGroup);
            }
        });

        //Move down button
        Button groupDown = new Button("\\/");
        groupDown.setOnAction((ActionEvent event) -> {
            int index = controller.getProject().getGroups().indexOf(elementGroup);
            if (index < controller.getProject().getGroups().size() - 1) { //Isn't the last one
                controller.getProject().getGroups().remove(elementGroup);
                controller.getProject().getGroups().add(index + 1, elementGroup);
            }
        });

        HBox buttonGroup = new HBox(groupUp, groupDown, deleteGroupButton);
        buttonGroup.setSpacing(5.0);

        //Label
        Label label = new Label(elementGroup.getName());
        label.setPadding(new Insets(5.0));

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(label);
        borderPane.setRight(buttonGroup);

        return borderPane;
    }
}
