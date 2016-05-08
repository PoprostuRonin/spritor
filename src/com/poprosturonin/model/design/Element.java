package com.poprosturonin.model.design;

import com.poprosturonin.model.Main;
import com.poprosturonin.model.assets.Asset;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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

    public double scaleMinX = 0;
    public double scaleMinY = 0;
    public double scaleMaxX = 0;
    public double scaleMaxY = 0;

    public transient Color colorFrom;
    public transient Color colorTo;

    public double colorOverride = 1;
    public double grain = 0;

    private transient Color baseColor;

    /**
     * Linked {@link Asset}
     */
    private transient Asset asset;

    public Element(Asset asset) {
        this.asset = asset;
        this.colorTo = asset.getBaseColor();
        this.colorFrom = asset.getBaseColor();
        this.baseColor = asset.getBaseColor();
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
        out.writeUTF(asset.getPath());

        //Save colors (we need to do it manually as Color isn't serializable
        out.writeDouble(colorTo.getHue());
        out.writeDouble(colorTo.getSaturation());
        out.writeDouble(colorTo.getBrightness());

        out.writeDouble(colorFrom.getHue());
        out.writeDouble(colorFrom.getSaturation());
        out.writeDouble(colorFrom.getBrightness());
    }

    /** Deserialization */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String path = in.readUTF();
        System.out.println(path);
        asset = Main.assetManager.findAsset(path);
        colorTo = Color.hsb(in.readDouble(),in.readDouble(),in.readDouble());
        colorFrom = Color.hsb(in.readDouble(),in.readDouble(),in.readDouble());
        baseColor = asset.getBaseColor();
    }
}
