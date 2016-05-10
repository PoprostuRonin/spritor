/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.model.design;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.java.com.poprosturonin.model.Main;
import main.java.com.poprosturonin.model.assets.Asset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents design-ready and generator-ready asset
 */
public class Element implements Serializable {
    public DesignVector position = new DesignVector(0, 0);

    public DesignVector moveMin = new DesignVector(0, 0);
    public DesignVector moveMax = new DesignVector(0, 0);

    public double scaleMinX = 1;
    public double scaleMinY = 1;
    public double scaleMaxX = 1;
    public double scaleMaxY = 1;

    public transient Color colorFrom;
    public transient Color colorTo;

    public double colorOverride = 1;
    public double grain = 0;

    private transient Color baseColor;

    /**
     * Linked {@link Asset}
     */
    private transient Asset asset;

    /**
     * Creates new element with assigned asset for later reconstruction
     */
    public Element(Asset asset) {
        this.asset = asset;
        this.baseColor = asset.getBaseColor();
        this.colorTo = asset.getBaseColor();
        this.colorFrom = asset.getBaseColor();
    }

    /**
     * Returns Image from linked {@link Asset}
     */
    public Image getImage() {
        return asset.getImage();
    }

    /**
     * Returns thumbnail from linked {@link Asset}
     */
    public Image getThumbnail() {
        return asset.getThumbnail();
    }

    /**
     * Returns filename from linked {@link Asset}
     */
    public String getFileName() {
        return asset.getFileName();
    }

    /**
     * Returns base color of linked {@link Asset}
     */
    public Color getBaseColor() {
        return baseColor;
    }

    /**
     * Serialization
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        if (asset.getPath() != null) {
            out.writeUTF(asset.getPath());

            //Save colors (we need to do it manually as Color isn't serializable
            out.writeDouble(colorTo.getHue());
            out.writeDouble(colorTo.getSaturation());
            out.writeDouble(colorTo.getBrightness());
            out.writeDouble(colorFrom.getHue());
            out.writeDouble(colorFrom.getSaturation());
            out.writeDouble(colorFrom.getBrightness());
        }
    }

    /** Deserialization */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        try {
            String path = in.readUTF();
            asset = Main.assetManager.findAsset(path);
            colorTo = Color.hsb(in.readDouble(), in.readDouble(), in.readDouble());
            colorFrom = Color.hsb(in.readDouble(), in.readDouble(), in.readDouble());
            baseColor = asset.getBaseColor();
        } catch (Exception e) { //If anything goes wrong just make it an empty element
            asset = new Asset();
            baseColor = asset.getBaseColor();
            colorFrom = asset.getBaseColor();
            colorTo = asset.getBaseColor();
        }
    }
}