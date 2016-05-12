/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model.design;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Represents category, group of resources
 */
public class ElementGroup implements Serializable {
    int layer;
    transient private ObservableList<Element> elements = FXCollections.observableArrayList();
    private Element[] savedElements;

    private String name;
    private transient Random random = new Random();

    public ElementGroup(String name) {
        this.name = name;
    }

    public void addElement(Element element) {
        if (!elements.contains(element))
            elements.add(element);
    }

    public String getName() {
        return name;
    }

    public ObservableList<Element> getElements() {
        return elements;
    }

    public Element getRandomElement() {
        if (elements.size() > 0) {
            return elements.get(Math.abs(random.nextInt()) % elements.size());
        } else
            return null;
    }

    /**
     * Serialization
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        Object[] toSave = elements.toArray(); //As FXCollections doesn't give as Serializable object, we can just dump our data into an array
        savedElements = new Element[toSave.length];
        for (int i = 0; i < toSave.length; i++) {
            savedElements[i] = (Element) toSave[i];
        }
        out.defaultWriteObject();
    }

    /**
     * Deserialization
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        elements = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList(savedElements)));

        random = new Random();
    }
}
