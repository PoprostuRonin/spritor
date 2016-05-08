/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model.assets;

import java.util.ArrayList;

/**
 * Represents category, group of resources
 */
public class AssetGroup {
    protected ArrayList<Asset> assets;
    protected String name;

    public AssetGroup(String name) {
        assets = new ArrayList<>();
        this.name = name;
    }

    public void addElement(Asset asset) {
        if (!assets.contains(asset))
            assets.add(asset);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Asset> getAssets() {
        return assets;
    }
}
