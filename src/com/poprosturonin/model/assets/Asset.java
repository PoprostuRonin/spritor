/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model.assets;

import com.poprosturonin.controller.Utility;
import com.poprosturonin.model.ImageToolkit;
import com.poprosturonin.model.design.Element;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Asset which can be combined with other resources to generate character
 */
public class Asset {
    public final static int thumbnailSize = 48;
    private static Image emptyElementThumbnail;
    private WritableImage image;
    private Image thumbnail;
    private String fileName;
    private String filePath;
    private Color baseColor;
    private ElementResolver elementResolver;

    /**
     * Creates new empty asset, creating {@link com.poprosturonin.model.design.Element}
     * from this asset will create an empty element
     */
    public Asset() {
        if (emptyElementThumbnail == null) { //If not loaded yet
            emptyElementThumbnail = ImageToolkit.scale(
                    new Image(getClass().getResourceAsStream("/resources/images/empty.png")),
                    thumbnailSize,
                    thumbnailSize);
        }

        image = new WritableImage(1, 1);
        baseColor = Color.TRANSPARENT;
        thumbnail = emptyElementThumbnail;
        elementResolver = () -> new Element(this);
    }

    public Asset(File file) {
        try {
            Image loadedImage = new Image(new FileInputStream(file));
            this.image = new WritableImage(loadedImage.getPixelReader(), (int) loadedImage.getWidth(), (int) loadedImage.getHeight());
            this.fileName = file.getName();
            this.filePath = file.getPath();
            this.thumbnail = ImageToolkit.scale(loadedImage, thumbnailSize, thumbnailSize);

            PixelReader reader = this.image.getPixelReader();
            for (int i = 0; i < this.image.getWidth(); i++) {
                for (int j = 0; j < this.image.getHeight(); j++) {
                    Color color = reader.getColor(i, j);
                    if (color.getOpacity() > 0) { //Find first color that isn't transparent
                        baseColor = color;
                        break;
                    }
                }

                if (baseColor != null) {
                    break;
                }
            }

            final Asset thisAsset = this;

            elementResolver = () -> new Element(thisAsset);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Utility.exceptionAlert(e,"File was found previously, but now it couldn't be found");
        }
    }

    public Element getElement() {
        if (elementResolver != null)
            return elementResolver.getElement();
        else {
            Utility.errorAlert("Asset's element resolver isn't available.");
            return null;
        }
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public Image getImage() {
        return image;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() { return filePath;
    }

    /**
     * Simple interface to make assets return specified Elements
     */
    private interface ElementResolver {
        Element getElement();
    }
}
