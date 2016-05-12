/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package com.poprosturonin.model;

import com.poprosturonin.controller.Utility;
import com.poprosturonin.model.design.Element;
import com.poprosturonin.model.design.ElementGroup;
import com.poprosturonin.model.design.Project;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

/**
 * Generator for the final products
 */
public class Generator {
    private Project project;
    private File destination;
    private Random random;
    private Thread workingThread;
    private GeneratorProgressListener changeListener;

    public Generator(File destination, Project project) {
        this.project = project;
        this.destination = destination;
        random = new Random();
    }

    public boolean isWorking() {
        return workingThread != null && workingThread.isAlive();
    }

    private void update(int current, int amount) {
        if (changeListener != null)
            changeListener.changed(current, amount);
    }

    public void setOnProgressChanged(GeneratorProgressListener changed) {
        changeListener = changed;
    }

    /** Starts generator within new thread
     *
     * @param numberOfFiles amount of sprites that should be generated
     */
    public void start(int numberOfFiles) {
        if (workingThread == null || !workingThread.isAlive()) {
            workingThread = new Thread(() -> {
                for (int n = 0; n < numberOfFiles; n++) {
                    WritableImage canvas = new WritableImage(16, 16);

                    //Write
                    PixelWriter writer = canvas.getPixelWriter();
                    for (int i = 0; i < canvas.getWidth(); i++) {
                        for (int j = 0; j < canvas.getHeight(); j++) {
                            writer.setColor(i, j, Color.TRANSPARENT);
                        }
                    }

                    ObservableList<ElementGroup> elementGroups = project.getGroups();
                    for (int i = elementGroups.size() - 1; i >= 0; i--) //Read from end
                    {
                        ElementGroup elementGroup = elementGroups.get(i);
                        if (elementGroup.getElements().size() > 0) {
                            Element elementToDraw = elementGroup.getRandomElement();

                            //Apply effects and put element to the rest
                            canvas = ImageToolkit.applyAllEffects(elementToDraw, canvas);
                        }
                    }

                    //Output
                    File output = new File(destination, Integer.toString(n) + ".png");
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(canvas, null);
                    try {
                        ImageIO.write(bufferedImage, "png", output);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utility.errorAlert(e.getMessage());
                    }

                    //Progress changed
                    update(n,numberOfFiles);
                }
            });
            workingThread.start();
        }
    }

    public interface GeneratorProgressListener {
        void changed(int current, int amount);
    }
}
