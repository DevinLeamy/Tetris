package com.company;
import java.util.ArrayList;
import java.awt.Color;

public class Shape {
    private int stateCnt;
    private ArrayList<boolean[][]> states;
    private int currentState = 0;
    private Pair topLeft;
    private Color color;
    public Shape(int stateCnt, ArrayList<boolean[][]> states, Pair topLeft) {
        this.states = states;
        this.stateCnt = stateCnt;
        this.topLeft = topLeft;
    }

    public void rotateLeft() {
        this.currentState = (currentState + 1) % this.stateCnt;
    }

    public void rotateRight() {
        this.currentState = currentState - 1;
        if (this.currentState == -1) {
            this.currentState = this.stateCnt - 1;
        }
    }

    public ArrayList<Pair> getActiveBlocks() {
        ArrayList<Pair> activeBlocks = new ArrayList<Pair>();
        boolean[][] currentState = this.states.get(this.currentState);
        for (int i = 0; i < currentState.length; i++) {
            for (int j = 0; j < currentState[0].length; j++) {
                if (currentState[i][j]) {
                    activeBlocks.add(new Pair(topLeft.first + i, topLeft.second + j));
                }
            }
        }
        return activeBlocks;
    }

    public void shiftLeft() {
        this.topLeft.second += 1;
    }

    public void shiftRight() {
        this.topLeft.second -= 1;
    }

    public void shiftDown() {
        this.topLeft.first -= 1;
    }

    public Shape getClone() {
        Shape cloned = new Shape(this.stateCnt, this.states, this.topLeft.clone());
        cloned.currentState = currentState;
        if (this.color != null) {
            cloned.setColor(this.color);
        }
        return cloned;
    }

    public void setTopLeft(Pair newTopLeft) {
        this.topLeft = newTopLeft;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public int getHighestBlock() {
        ArrayList<Pair> activeBlocks = this.getActiveBlocks();
        int largest = -1;
        for (Pair block : activeBlocks) {
            if (largest < block.first) {
                largest = block.first;
            }
        }
        return largest;
    }

    public void setCurrentState(int state) {
        state %= this.stateCnt;
        this.currentState = state;
    }
}
