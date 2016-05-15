/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model.design;

import com.poprosturonin.model.assets.Asset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

/**
 * Project represent set up of {@link ElementGroup}s and {@link Asset}s
 * Projects are the final form of work which come right to the generator.
 */
public class Project implements Serializable {
    transient private ObservableList<ElementGroup> groups = FXCollections.observableArrayList();

    private ElementGroup[] savedGroups;
    private int canvasWidth;
    private int canvasHeight;
    private String path = "";

    private boolean changed = false;

    public Project(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    /**
     * Checks whether project was changed.
     * @return true when project was changed, otherwise returns false
     */
    public boolean isChanged() { return changed; }

    /**
     * Sets whether project was changed. Used to prevent loss of data on closing project.
     * @param changed project changed
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Gets project file path. This path is default path for saving project.
     * @return file path
     */
    public String getPath() { return path; }

    /**
     * Sets project file path where project should be saved
     * @param path file path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Checks whether path is set
     * @return true if path is set, otherwise returns false
     */
    public boolean isPathSet() { return !path.isEmpty(); }

    /**
     * Puts new {@link Element} into defaultGroup
     *
     * @return Assigned ElementGroup to the given Element
     */
    public ElementGroup putNew(Element element) {
        if (groups.size() <= 0)
            groups.add(new ElementGroup("Default"));
        groups.get(0).addElement(element);
        return groups.get(0);
    }

    /**
     * Creates new, empty ElemenetGroup
     */
    public ElementGroup newGroup(String name) {
        ElementGroup elementGroup = new ElementGroup(name);
        groups.add(elementGroup);
        return elementGroup;
    }

    /**
     * Creates new ElementGroup with one Element
     */
    public ElementGroup newGroup(String name, Element element) {
        ElementGroup elementGroup = new ElementGroup(name);
        groups.add(elementGroup);
        elementGroup.addElement(element);
        return elementGroup;
    }

    public ElementGroup getGroup(int hashcode) {
        for (ElementGroup group : groups) {
            if (group.hashCode() == hashcode)
                return group;
        }
        return null;
    }

    public Element getElement(ElementGroup group, int hashcode) {
        for (Element element : group.getElements()) {
            if (element.hashCode() == hashcode)
                return element;
        }
        return null;
    }

    public int getCanvasWidth() { return canvasWidth; }

    public int getCanvasHeight() { return canvasHeight; }

    public void removeGroup(ElementGroup elementGroup) {
        groups.remove(elementGroup);
    }

    public ObservableList<ElementGroup> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        String string = "Project " + Integer.toString(hashCode()) + ": ";
        string += groups.toString();
        string += " Canvas width: " + Integer.toString(canvasWidth);
        string += " Canvas height: " + Integer.toString(canvasHeight);
        return string;
    }
}
