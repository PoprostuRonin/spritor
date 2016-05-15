/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.controller;

import com.poprosturonin.model.design.Element;
import com.poprosturonin.model.design.ElementGroup;
import com.poprosturonin.model.design.Project;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls list of {@link ElementGroupController}. Observes original {@link ElementGroup} list on project.
 */
public class ElementGroupListController implements ListChangeListener {
    private Project project;
    private DesignController controller;
    private Map<ElementGroup, ElementGroupController> elementGroupViews = new HashMap<>();

    @FXML
    private VBox mainVBox;
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private Button newGroup;

    /**
     * Called after a change has been made to an ElementGroup's ObservableList
     *
     * @param c an object representing the change that was done
     * @see Change
     */
    @Override
    public void onChanged(Change c) {
        while (c.next()) {
            if (c.wasAdded()) {
                List<ElementGroup> added = c.getAddedSubList();
                addAll(added, c.getFrom()); //c.getFrom() returns index that it was added
            } else if (c.wasRemoved()) {
                List<ElementGroup> removed = c.getRemoved(); //we don't need to know index
                removeAll(removed);
            }
        }
    }

    /**
     * Gets {@link ElementGroupController} from list related to given {@link ElementGroup}
     *
     * @param elementGroup {@link ElementGroup} related to the {@link ElementGroupController}
     * @return {@link ElementGroupController} for given {@link ElementGroup} or null if doesn't exist
     */
    public ElementGroupController getControllerForElementGroup(ElementGroup elementGroup) {
        return elementGroupViews.get(elementGroup);
    }

    /**
     * Looks for a button related to given {@link Element}
     *
     * @param element {@link Element} related to its button
     * @return {@link Button} for given {@link Element} or null if doesn't exist
     */
    public Button getButtonForElement(Element element) {
        for (ElementGroupController elementGroupController : elementGroupViews.values()) {
            Button button = elementGroupController.getButtonForElement(element);
            if (button != null)
                return button;
        }
        return null;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        clear();
        if (project != null) {
            addAll(project.getGroups());
            project.getGroups().addListener(this);
        }
    }

    /**
     * Clears view from project related UI elements
     */
    void clear() {
        mainVBox.getChildren().clear();
        elementGroupViews.clear();
    }

    /**
     * Adds given {@link ElementGroup} to this controller, not actually {@link Project}.
     * Creates new {@link ElementGroupController} for it.
     *
     * @param elementGroup given {@link ElementGroup}
     */
    private void add(ElementGroup elementGroup) {
        if (!elementGroupViews.containsKey(elementGroup))
            elementGroupViews.put(elementGroup, new ElementGroupController(mainVBox, elementGroup, this));
    }

    /**
     * Short code, calls {@link #add(ElementGroup)} on whole collection
     *
     * @param elementGroups collection
     */
    private void addAll(Collection<ElementGroup> elementGroups) {
        for (ElementGroup elementGroup : elementGroups)
            add(elementGroup);
    }

    /**
     * Adds given {@link ElementGroup} to this controller at given index, not actually {@link Project}
     * Creates new {@link ElementGroupController} for it.
     *
     * @param elementGroup given {@link ElementGroup}
     * @param index        index to add at given {@link ElementGroup}
     */
    private void add(ElementGroup elementGroup, int index) {
        if (!elementGroupViews.containsKey(elementGroup))
            elementGroupViews.put(elementGroup, new ElementGroupController(mainVBox, elementGroup, this, index));
    }

    /**
     * Short code, calls {@link #add(ElementGroup, int)} on whole collection
     *
     * @param elementGroups collection
     * @param index         index to add at given {@link ElementGroup}
     */
    private void addAll(Collection<ElementGroup> elementGroups, int index) {
        for (ElementGroup elementGroup : elementGroups)
            add(elementGroup, index);
    }

    /**
     * Removes given {@link ElementGroup} from this controller, not actually {@link Project}
     * Destroys {@link ElementGroupController} which was created for it. If controller doesn't have given {@link ElementGroup} does nothing.
     *
     * @param elementGroup given {@link ElementGroup}
     */
    private void remove(ElementGroup elementGroup) {
        mainVBox.getChildren().remove(elementGroupViews.get(elementGroup).getMainVBox());
        elementGroupViews.remove(elementGroup);
    }

    /**
     * Short code, calls {@link #remove(ElementGroup)} on whole collection
     *
     * @param elementGroups collection
     */
    private void removeAll(Collection<ElementGroup> elementGroups) {
        for (ElementGroup elementGroup : elementGroups)
            remove(elementGroup);
    }

    @FXML
    private void newGroupAction(ActionEvent event) {
        project.newGroup("New group");
        event.consume();
    }

    @FXML
    private void newGroupDragOver(DragEvent event) {
        if (event.getGestureSource() != newGroup && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    @FXML
    private void newGroupDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
            String data = db.getString();
            String[] hashCodes = data.split(":");
            int hashCodeGroup = Integer.parseInt(hashCodes[0]);
            int hashCodeElement = Integer.parseInt(hashCodes[1]);

            ElementGroup elementGroup = project.getGroup(hashCodeGroup);
            if (elementGroup != null) {
                Element element = project.getElement(elementGroup, hashCodeElement);
                if (element != null) {
                    ElementGroup newElementGroup = project.newGroup(element.getFileName(), element);
                    elementGroup.getElements().remove(element); //Remove from old group

                    getDesignController().setCurrentElement(element);
                    success = true;
                } else
                    Utility.errorAlert("DragAndDrop issue: element not found by hash code");
            } else
                Utility.errorAlert("DragAndDrop issue: element group not found by hash code");
        }
        event.setDropCompleted(success);
        event.consume();
    }

    DesignController getDesignController() {
        return controller;
    }

    void setDesignController(DesignController designController) {
        this.controller = designController;
    }
}
