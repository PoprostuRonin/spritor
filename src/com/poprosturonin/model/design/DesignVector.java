package com.poprosturonin.model.design;

import java.io.Serializable;

/**
 * Vector created for designing. Operates on integer values.
 */
public class DesignVector implements Serializable {
    public int x;
    public int y;

    public DesignVector(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
