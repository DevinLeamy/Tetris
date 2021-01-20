package com.company;
import java.awt.*;

public class Block {
    private boolean taken;
    private Color color;
    public Block(Color color, boolean taken) {
        this.color = color;
        this.taken = taken;
    }

    public boolean isTaken() {
        return this.taken;
    }

    public Color getColor() {
        return this.color;
    }
}
