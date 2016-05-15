/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model.design;

import com.poprosturonin.model.assets.Asset;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
     * Returns asset path from linked {@link Asset}
     */
    public String getAssetPath() {
        return asset.getAssetPath();
    }
}
