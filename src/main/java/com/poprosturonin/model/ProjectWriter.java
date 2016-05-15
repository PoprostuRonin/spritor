/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model;

import com.poprosturonin.controller.Utility;
import com.poprosturonin.model.design.ElementGroup;
import com.poprosturonin.model.design.Project;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Writes project on disk
 */
public class ProjectWriter {
    public ProjectWriter(Project project, File output) {
        Document document = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.newDocument();
        } catch (Exception e) {
            Utility.exceptionAlert(e, "Couldn't save project");
        }

        if (document != null) {
            Element projectElement = document.createElement("project");
            document.appendChild(projectElement);

            //Canvas element
            Element canvasElement = document.createElement("canvas");
            canvasElement.setAttribute("width", Integer.toString(project.getCanvasWidth()));
            canvasElement.setAttribute("height", Integer.toString(project.getCanvasHeight()));
            projectElement.appendChild(canvasElement);

            //Groups
            Element groupsElement = document.createElement("groups");
            projectElement.appendChild(groupsElement);

            for (ElementGroup elementGroup : project.getGroups()) {
                Element groupElement = document.createElement("elementGroup");
                groupElement.setAttribute("name", elementGroup.getName());

                for (com.poprosturonin.model.design.Element element : elementGroup.getElements()) {
                    Element elementElement = document.createElement("element");
                    groupElement.appendChild(elementElement);

                    elementElement.setAttribute("path", element.getAssetPath());

                    elementElement.setAttribute("position.x", Integer.toString(element.position.x));
                    elementElement.setAttribute("position.y", Integer.toString(element.position.y));

                    elementElement.setAttribute("moveMin.x", Integer.toString(element.moveMin.x));
                    elementElement.setAttribute("moveMin.y", Integer.toString(element.moveMin.y));

                    elementElement.setAttribute("moveMax.x", Integer.toString(element.moveMax.x));
                    elementElement.setAttribute("moveMax.y", Integer.toString(element.moveMax.y));

                    elementElement.setAttribute("scaleMin.x", Double.toString(element.scaleMinX));
                    elementElement.setAttribute("scaleMin.y", Double.toString(element.scaleMinY));

                    elementElement.setAttribute("scaleMax.x", Double.toString(element.scaleMaxX));
                    elementElement.setAttribute("scaleMax.y", Double.toString(element.scaleMaxY));

                    elementElement.setAttribute("grain", Double.toString(element.grain));
                    elementElement.setAttribute("colorOverride", Double.toString(element.colorOverride));

                    elementElement.setAttribute("colorTo.r", Double.toString(element.colorTo.getRed()));
                    elementElement.setAttribute("colorTo.g", Double.toString(element.colorTo.getGreen()));
                    elementElement.setAttribute("colorTo.b", Double.toString(element.colorTo.getBlue()));

                    elementElement.setAttribute("colorFrom.r", Double.toString(element.colorTo.getRed()));
                    elementElement.setAttribute("colorFrom.g", Double.toString(element.colorTo.getGreen()));
                    elementElement.setAttribute("colorFrom.b", Double.toString(element.colorTo.getBlue()));
                }
                groupsElement.appendChild(groupElement);
            }
        }

        TransformerFactory transformerFactory = null;
        Transformer transformer = null;
        DOMSource source = new DOMSource(document);

        try {
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
        } catch (Exception e) {
            Utility.exceptionAlert(e, "Couldn't save project");
        }

        //Testing
        try {
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
