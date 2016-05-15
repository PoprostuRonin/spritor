/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model.assets;

import com.poprosturonin.controller.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages resources (mostly images)
 */
public class AssetManager {
    /**
     * List of every loaded element
     */
    private ArrayList<Asset> assets = new ArrayList<>();

    /**
     * Map for matching folder to group
     */
    private Map<String, AssetGroup> groups = new HashMap<>();

    /** Looks for asset by given path, because of that this method may return wrong asset
     *
     * @param path given path
     * @return found asset or null if not found
     */
    public Asset findAsset(String path) {
        for (Asset asset : assets) {
            if (asset.getAssetPath().equals(path) || asset.getPath().equals(path)) {
                return asset;
            }
        }
        return null;
    }

    /**
     * Loads images from external resources folder to memory as {@link Asset}
     */
    public void load() {
        final File assetsFolder = new File(System.getProperty("user.dir") + "/assets");

        //Create group for storing resources without desired group
        AssetGroup groupNone = new AssetGroup("None");
        groups.put("None", groupNone); //This actually allows to create directory named "None", but that's ok
        groupNone.addElement(new Asset()); //Empty element actually

        if (!assetsFolder.mkdir()) { //If there was no directory there is no sense to do anything more
            final File[] files = assetsFolder.listFiles();
            if (files != null) { //Check if folder isn't empty
                for (final File file : files) {
                    if (file.isDirectory()) {
                        //Check if group exists
                        if (!groups.containsKey(file.getName())) {
                            //Creating new group
                            groups.put(file.getName(), new AssetGroup(file.getName()));
                        }
                        //Get files from directory (only pngs)
                        final File[] filesInDirectory = file.listFiles((File pathname) -> {
                            return pathname.getPath().endsWith(".png");
                        }); //FileFilter
                        //Get group
                        AssetGroup assetGroup = groups.get(file.getName());
                        //Load all files in directory into specific group
                        for (final File fileInDirectory : filesInDirectory) {
                            try {
                                Asset loadedAsset = new Asset(fileInDirectory);
                                assets.add(loadedAsset);
                                assetGroup.addElement(loadedAsset);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Utility.errorAlert(e.getMessage());
                            }
                        }
                    } else {
                        try {
                            Asset loadedAsset = new Asset(file);
                            assets.add(loadedAsset);
                            groupNone.addElement(loadedAsset);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utility.errorAlert(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public ArrayList<AssetGroup> getAssetGroups() {
        return new ArrayList<>(groups.values());
    }
}
