/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model;

import com.poprosturonin.controller.Utility;
import com.poprosturonin.model.assets.Asset;
import com.poprosturonin.model.design.ElementGroup;
import com.poprosturonin.model.design.Project;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

/**
 * Project loader
 */
public class ProjectLoader {
    private Project project;

    public ProjectLoader(File file) {
        if (file.exists()) {
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder = null;
            try {
                //Get document builder
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (Exception e) {
                Utility.exceptionAlert(e);
            }

            //Build document
            Document document = null;
            if (dBuilder != null) {
                try {
                    document = dBuilder.parse(file);
                    document.getDocumentElement().normalize();
                } catch (IOException ioe) {
                    Utility.exceptionAlert(ioe, "Couldn't load file");
                } catch (SAXException se) {
                    Utility.exceptionAlert(se);
                }
            }

            if (document != null) {
                //Get canvas
                int canvasWidth = 16;
                int canvasHeight = 16;
                try {
                    Element canvasElement = (Element) document.getElementsByTagName("canvas").item(0);
                    canvasWidth = Integer.parseInt(canvasElement.getAttribute("width"));
                    canvasHeight = Integer.parseInt(canvasElement.getAttribute("height"));
                } catch (Exception e) {
                    Utility.exceptionAlert(e, "Canvas element couldn't be read. Project was initialized with default canvas");
                }

                //Create project
                project = new Project(canvasWidth, canvasHeight);

                //Lists elementGroups
                NodeList nodeList = document.getElementsByTagName("elementGroup");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    String groupName = "Group";
                    Node groupNode = nodeList.item(i);
                    Element groupElement = (Element) groupNode;
                    groupName = groupElement.getAttribute("name");

                    //Create ElementGroup
                    ElementGroup elementGroup = new ElementGroup(groupName);
                    project.getGroups().add(elementGroup);

                    //Lists elements in elementGroup
                    NodeList elementsNodes = groupElement.getElementsByTagName("element");

                    for (int j = 0; j < elementsNodes.getLength(); j++) {
                        Node elementNode = elementsNodes.item(j);
                        if (elementNode != null) {
                            Element elementElement = (Element) elementNode;

                            com.poprosturonin.model.design.Element element = null;

                            try {
                                String path = elementElement.getAttribute("path");
                                Asset asset = Main.assetManager.findAsset(path);
                                if (asset != null) {
                                    element = new com.poprosturonin.model.design.Element(asset);
                                } else {
                                    element = new com.poprosturonin.model.design.Element(new Asset());
                                }
                            } catch (Exception e) {
                                Utility.exceptionAlert(e, "Element couldn't be created, skipping...");
                            }

                            if (element != null) {
                                element.position.x = getIntegerValue(elementElement, "position.x", element.position.x);
                                element.position.y = getIntegerValue(elementElement, "position.y", element.position.y);
                                element.moveMax.x = getIntegerValue(elementElement, "moveMax.x", element.moveMax.x);
                                element.moveMax.y = getIntegerValue(elementElement, "moveMax.y", element.moveMax.y);
                                element.moveMin.x = getIntegerValue(elementElement, "moveMin.x", element.moveMin.x);
                                element.moveMin.y = getIntegerValue(elementElement, "moveMin.y", element.moveMin.y);
                                element.scaleMaxX = getDoubleValue(elementElement, "scaleMax.x", element.scaleMaxX);
                                element.scaleMinX = getDoubleValue(elementElement, "scaleMin.x", element.scaleMinX);
                                element.scaleMaxY = getDoubleValue(elementElement, "scaleMax.y", element.scaleMaxY);
                                element.scaleMinY = getDoubleValue(elementElement, "scaleMin.y", element.scaleMinY);
                                element.grain = getDoubleValue(elementElement, "grain", element.grain);
                                element.colorOverride = getDoubleValue(elementElement, "colorOverride", element.colorOverride);
                                element.colorTo = getColor(elementElement, "colorTo", element.colorTo);
                                element.colorFrom = getColor(elementElement, "colorFrom", element.colorTo);
                                elementGroup.addElement(element);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean hasLoaded() {
        return project != null;
    }

    public Project getProject() {
        return project;
    }

    private int getIntegerValue(Element element, String attributeName, int defaultValue) {
        if (element.hasAttribute(attributeName)) {
            int value = Integer.parseInt(element.getAttribute(attributeName));
            return value;
        }
        return defaultValue;
    }

    private double getDoubleValue(Element element, String attributeName, double defaultValue) {
        if (element.hasAttribute(attributeName)) {
            double value = Double.parseDouble(element.getAttribute(attributeName));
            return value;
        }
        return defaultValue;
    }

    private Color getColor(Element element, String attributeName, Color defaultValue) {
        if (element.hasAttribute(attributeName + ".h") && element.hasAttribute(attributeName + ".s") && element.hasAttribute(attributeName + ".b")) {
            double h = Double.parseDouble(element.getAttribute(attributeName + ".h"));
            double s = Double.parseDouble(element.getAttribute(attributeName + ".s"));
            double b = Double.parseDouble(element.getAttribute(attributeName + ".b"));

            if (element.hasAttribute(attributeName + ".a")) {
                double a = Double.parseDouble(element.getAttribute(attributeName + ".a")); //Opacity
                return Color.hsb(h, s, b, a);
            } else
                return Color.hsb(h, s, b);
        } else if (element.hasAttribute(attributeName + ".r") && element.hasAttribute(attributeName + ".g") && element.hasAttribute(attributeName + ".b")) {
            double r = Double.parseDouble(element.getAttribute(attributeName + ".r"));
            double g = Double.parseDouble(element.getAttribute(attributeName + ".g"));
            double b = Double.parseDouble(element.getAttribute(attributeName + ".b"));

            if (element.hasAttribute(attributeName + ".a")) {
                double a = Double.parseDouble(element.getAttribute(attributeName + ".a")); //Opacity
                return Color.color(r, g, b, a);
            } else
                return Color.color(r, g, b);
        }
        return defaultValue;
    }
}
