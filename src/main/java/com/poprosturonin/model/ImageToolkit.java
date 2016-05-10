/*
 * Spritor, sprite generator tool
 * Copyright (C) Bartosz Wiśniewski <poprosturonin.com>
 * Copyright (C) Spritor team and contributors
 */

package main.java.com.poprosturonin.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import main.java.com.poprosturonin.model.design.Element;

import java.util.Random;

/**
 * Toolkit for Image manipulation
 */
public class ImageToolkit {
    /**
     * Apply all effects
     *
     * @param element    element to add the background
     * @param background background for element
     * @return final image
     */
    public static WritableImage applyAllEffects(Element element, WritableImage background) {
        Random random = new Random();

        Image imageToDraw = element.getImage();

        //Get position and random move
        int moveX = random.nextInt(element.moveMax.x - element.moveMin.x + 1) + element.moveMin.x;
        int posX = element.position.x + moveX;
        int moveY = random.nextInt(element.moveMax.y - element.moveMin.y + 1) + element.moveMin.y;
        int posY = element.position.y + moveY;

        //Change color
        if (element.colorOverride > 0) {
            Color from = element.colorFrom;
            Color to = element.colorTo;

            double hue = from.getHue() + (to.getHue() - from.getHue()) * random.nextDouble();
            double saturation = from.getSaturation() + (to.getSaturation() - from.getSaturation()) * random.nextDouble();
            double brightness = from.getBrightness() + (to.getBrightness() - from.getBrightness()) * random.nextDouble();

            Color base = element.getBaseColor();

            double relativeHue = hue - base.getHue();
            double relativeSaturation = saturation - base.getSaturation();
            double relativeBrightness = brightness - base.getBrightness();

            if (element.colorOverride == 1)
                imageToDraw = ImageToolkit.applyColor(imageToDraw, relativeHue, relativeSaturation, relativeBrightness);
            else
                imageToDraw = ImageToolkit.applyColorPercent(imageToDraw, relativeHue, relativeSaturation, relativeBrightness, element.colorOverride);
        }

        //Scaling
        int centerAfterScalingX = 0; //Those variables are used to center image after scaling
        int centerAfterScalingY = 0;
        if (element.scaleMinX != 0.0 || element.scaleMaxX != 0.0 || element.scaleMinY != 0.0 || element.scaleMaxY != 0.0) {
            double randomScaleX = element.scaleMinX + (element.scaleMaxX - element.scaleMinX) * random.nextDouble();
            double randomScaleY = element.scaleMinY + (element.scaleMaxY - element.scaleMinY) * random.nextDouble();
            centerAfterScalingX = (int) Math.round(((imageToDraw.getWidth() * (randomScaleX) - imageToDraw.getWidth()) / 2.0) * -1);
            centerAfterScalingY = (int) Math.round(((imageToDraw.getHeight() * (randomScaleY) - imageToDraw.getHeight()) / 2.0) * -1);
            imageToDraw = ImageToolkit.scale(imageToDraw, randomScaleX, randomScaleY);
        }

        //Add grain
        if (element.grain > 0) {
            imageToDraw = ImageToolkit.applyGrain(imageToDraw, element.grain);
        }

        return ImageToolkit.applyImage(imageToDraw, background, posX + centerAfterScalingX, posY + centerAfterScalingY);
    }

    /**
     * Scales image with nearest neighbour algorithm to desired size
     *
     * @param image         image to scale
     * @param desiredWidth  width of output image
     * @param desiredHeight height of output image
     * @return scaled image
     */
    public static WritableImage scale(Image image, int desiredWidth, int desiredHeight) {
        double scaleWidth = image.getWidth() / desiredWidth;
        double scaleHeight = image.getHeight() / desiredHeight;

        WritableImage output = new WritableImage(desiredWidth, desiredHeight);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int i = 0; i < desiredHeight; i++) {
            for (int j = 0; j < desiredWidth; j++) {
                //Position on original image
                int x = (int) Math.floor(j * scaleWidth);
                int y = (int) Math.floor(i * scaleHeight);

                writer.setArgb(j, i, reader.getArgb(x, y));
            }
        }
        return output;
    }

    /**
     * Scales image with nearest neighbour algorithm by scale
     *
     * @param image image to scale
     * @param scale scale in format like 0.2 is 20% of Image's size
     * @return scaled image
     */
    public static WritableImage scale(Image image, double scale) {
        return scale(image, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
    }

    /**
     * Scales image with nearest neighbour algorithm by scale
     *
     * @param image  image to scale
     * @param scaleX scale in format like 0.2 is 20% of Image's size, applies only to width
     * @param scaleY scale in format like 0.2 is 20% of Image's size, applies only to height
     * @return scaled image
     */
    public static WritableImage scale(Image image, double scaleX, double scaleY) {
        return scale(image, (int) (image.getWidth() * scaleX), (int) (image.getHeight() * scaleY));
    }

    /**
     * Applies image to a background
     * @param image image to be applied/added
     * @param background background for image
     * @param x position's X-axis
     * @param y position's Y-axis
     * @return combined given image and background
     */
    public static WritableImage applyImage(Image image, WritableImage background, int x, int y) {
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = background.getPixelWriter();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int ox = x + i;
                int oy = y + j;

                if (reader.getColor(i, j).getOpacity() > 0.0)
                    if (ox > 0 && ox < background.getWidth() && oy > 0 && oy < background.getHeight())
                        writer.setArgb(ox, oy, reader.getArgb(i, j));
            }
        }

        return background;
    }

    /**
     * Applies color modifier to image
     *
     * @param image              image to be edited
     * @param modifierHue        hue to be added in degrees
     * @param modifierSaturation saturation to be added, not required to be in range 0.0 - 1.0
     * @param modifierBrightness brightness to be added, not required to be in range 0.0 - 1.0
     * @return edited image
     */
    public static WritableImage applyColor(Image image, double modifierHue, double modifierSaturation, double modifierBrightness) {
        WritableImage output = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelReader reader = output.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                if (reader.getColor(i, j).getOpacity() > 0) {
                    Color color = reader.getColor(i, j);
                    double newHue = color.getHue() + modifierHue;
                    double newSaturation = color.getSaturation() + modifierSaturation;
                    double newBrightness = color.getBrightness() + modifierBrightness;

                    if (newBrightness > 1) {
                        newBrightness = 1;
                    } else if (newBrightness < 0) {
                        newBrightness = 0;
                    }
                    if (newSaturation > 1) {
                        newSaturation = 1;
                    } else if (newSaturation < 0) {
                        newSaturation = 0;
                    }

                    Color newColor = Color.hsb(newHue, newSaturation, newBrightness);

                    writer.setColor(i, j, newColor);
                }
            }
        }

        return output;
    }

    /**
     * Applies color modifier to image and then interpolates with original
     *
     * @param image              image to be edited
     * @param modifierHue        hue to be added in degrees
     * @param modifierSaturation saturation to be added, not required to be in range 0.0 - 1.0
     * @param modifierBrightness brightness to be added, not required to be in range 0.0 - 1.0
     * @param percent            power of color modification 1.0 fully overwritten, 0.0 original colors, should be in range 0.0 - 1.0
     * @return edited image
     */
    public static WritableImage applyColorPercent(Image image, double modifierHue, double modifierSaturation, double modifierBrightness, double percent) {
        WritableImage output = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelReader reader = output.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                if (reader.getColor(i, j).getOpacity() > 0) {
                    Color color = reader.getColor(i, j);
                    double newHue = color.getHue() + modifierHue;
                    double newSaturation = color.getSaturation() + modifierSaturation;
                    double newBrightness = color.getBrightness() + modifierBrightness;

                    if (newBrightness > 1) {
                        newBrightness = 1;
                    } else if (newBrightness < 0) {
                        newBrightness = 0;
                    }
                    if (newSaturation > 1) {
                        newSaturation = 1;
                    } else if (newSaturation < 0) {
                        newSaturation = 0;
                    }

                    Color newColor = Color.hsb(newHue, newSaturation, newBrightness);

                    //Interpolate
                    double r = color.getRed() + percent * (newColor.getRed() - color.getRed());
                    double g = color.getGreen() + percent * (newColor.getGreen() - color.getGreen());
                    double b = color.getBlue() + percent * (newColor.getBlue() - color.getBlue());

                    Color finalColor = Color.color(r, g, b);

                    writer.setColor(i, j, finalColor);
                }
            }
        }

        return output;
    }

    /**
     * Applies grain to image
     *
     * @param image          image to be edited
     * @param grainAmplitude grain power range amplitude, bigger equals more visible grain
     * @return edited image
     */
    public static WritableImage applyGrain(Image image, double grainAmplitude) {
        WritableImage output = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        PixelReader reader = output.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        Random random = new Random();

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                if (reader.getColor(i, j).getOpacity() > 0) {
                    Color color = reader.getColor(i, j);

                    double newBrightness = color.getBrightness() - grainAmplitude + 2 * grainAmplitude * random.nextDouble();
                    double newSaturation = color.getSaturation() - grainAmplitude + 2 * grainAmplitude * random.nextDouble();

                    if (newBrightness > 1) {
                        newBrightness = 1;
                    } else if (newBrightness < 0) {
                        newBrightness = 0;
                    }
                    if (newSaturation > 1) {
                        newSaturation = 1;
                    } else if (newSaturation < 0) {
                        newSaturation = 0;
                    }

                    writer.setColor(i, j, Color.hsb(color.getHue(), newSaturation, newBrightness));
                }
            }
        }

        return output;
    }
}
