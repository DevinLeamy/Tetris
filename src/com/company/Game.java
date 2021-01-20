package com.company;
import java.io.IOException;
import java.util.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Game implements KeyListener {
    private GameDisplay GRID;
    private final BlockManager BLOCK_MG = new BlockManager();
    private final int POINTS_PER_LANDING = 3;
    private final int POINTS_PER_ROW = 10;


    private int framesPerSecond = 3;
    private long minimumDeltaTime = 1000 / framesPerSecond;	// MILLISECONDS

    private boolean gameover = false;
    private Shape heldBlock = null; // no block is held
    private Shape currentBlock;
    private Shape nextBlock;
    private int score = 0;
    private int highScore = 0;

    public Game() {
        this.GRID = new GameDisplay(this.getHighScore());
        this.GRID.addKeyListener(this);
    }


    public boolean update() {
        if (gameover) {
            System.out.println("GAME OVER");
            return true;
        }
        this.updateMinimumDeltaTime();
        int points = 0;
        long currentTime = System.currentTimeMillis();
        long nextRefreshTime = currentTime + this.minimumDeltaTime;

        while (currentTime < nextRefreshTime) {
            Thread.yield();

            try { Thread.sleep(1); }
            catch (Exception e) {}

            currentTime = System.currentTimeMillis();
        }
        ArrayList<Pair> activeBlocks = this.currentBlock.getActiveBlocks();

        if (!this.GRID.onGround(activeBlocks)) {
            this.currentBlock.shiftDown();
            activeBlocks = this.currentBlock.getActiveBlocks();
        }

        if (this.GRID.onGround(activeBlocks)) {
            this.gameover = this.GRID.freezeBlocks(activeBlocks, this.currentBlock.getColor());
            points += this.POINTS_PER_LANDING;
            points += this.GRID.removeRows(this.POINTS_PER_ROW);
            this.setNextBlock();
        }
        this.setGridBlocks();
        score += points;
        this.GRID.displayScore(this.score, this.highScore);

        // Re-draws the scene
        this.GRID.repaint();

        return gameover;
    }

    private void updateMinimumDeltaTime() {
        this.framesPerSecond = Integer.min(20, 3 + this.score / 50);
        this.minimumDeltaTime = 1000 / this.framesPerSecond;
    }

    private int getHighScore() {
        try {
            File file = new File("High_Score.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            this.highScore = Integer.parseInt(reader.readLine());
        } catch (IOException e) {}
        return this.highScore;
    }

    private void setHighScore() {
        try {
            FileWriter fileWriter = new FileWriter("High_Score.txt");
            fileWriter.write(Integer.max(this.score, this.highScore) + "");
            fileWriter.close();
        } catch (IOException e) {}

    }

    public void start() {
        this.nextBlock = this.BLOCK_MG.getRandomShape();
        this.nextBlock.setTopLeft(this.GRID.getNextBlockPos());
        this.currentBlock = this.BLOCK_MG.getRandomShape();
        this.currentBlock.setTopLeft(this.GRID.getBlockStartPos());
        this.setGridBlocks();
    }

    private void setGridBlocks() {
        this.GRID.setCurrentBlock(this.currentBlock);
        this.GRID.setHeldBlock(this.heldBlock);
        this.GRID.setNextBlock(this.nextBlock);
    }

    private void setNextBlock() {
        this.currentBlock = this.nextBlock;
        this.currentBlock.setTopLeft(this.GRID.getBlockStartPos());
        this.nextBlock = this.BLOCK_MG.getRandomShape();
        this.nextBlock.setTopLeft(this.GRID.getNextBlockPos());
    }

    public int quit() {
        this.GRID.dispose();
        this.setHighScore();
        return this.score;
    }

    public void keyPressed(KeyEvent e) {
        if (gameover) {
            return;
        }
        int keyCode = e.getKeyCode();
        char key = KeyEvent.getKeyText(keyCode).charAt(0);
        ArrayList<Pair> activeBlocks;
        Shape clone;
        switch (key) {
            case 'J':
                // Rotate left
                clone = this.currentBlock.getClone();
                clone.rotateLeft();
                activeBlocks = clone.getActiveBlocks();
                if (!this.GRID.colliding(activeBlocks)) {
                    this.currentBlock.rotateLeft();
                }
                break;
            case 'L':
                // Rotate right
                clone = this.currentBlock.getClone();
                clone.rotateRight();
                activeBlocks = clone.getActiveBlocks();
                if (!this.GRID.colliding(activeBlocks)) {
                    this.currentBlock.rotateRight();
                }
                break;
            case 'A':
                activeBlocks = this.currentBlock.getActiveBlocks();
                if (this.GRID.canMoveLeft(activeBlocks)) {
                    this.currentBlock.shiftLeft();
                }
                break;
            case 'F':
                activeBlocks = this.currentBlock.getActiveBlocks();
                if (this.GRID.canMoveRight(activeBlocks)) {
                    this.currentBlock.shiftRight();
                }
                break;
            case 'Q':
                if (this.heldBlock == null) {
                    this.heldBlock = this.currentBlock;
                    this.heldBlock.setTopLeft(this.GRID.getBlockHoldPosition());
                    this.setNextBlock();
                } else {
                    int highestRow = this.currentBlock.getHighestBlock();
                    clone = this.heldBlock.getClone();
                    clone.setTopLeft(new Pair(highestRow - 1, this.GRID.getBlockStartPos().second));
                    activeBlocks = clone.getActiveBlocks();
                    if (!this.GRID.colliding(activeBlocks)) {
                        this.heldBlock = this.currentBlock;
                        this.heldBlock.setTopLeft(this.GRID.getBlockHoldPosition());
                        this.currentBlock = clone;
                    }
                }
                this.setGridBlocks();
                break;
            case '␣':
                int points = 0;
                activeBlocks = this.currentBlock.getActiveBlocks();
                while (!this.GRID.onGround(activeBlocks)) {
                    this.currentBlock.shiftDown();
                    activeBlocks = this.currentBlock.getActiveBlocks();
                }
                this.gameover = this.GRID.freezeBlocks(activeBlocks, this.currentBlock.getColor());
                points += this.POINTS_PER_LANDING;
                points += this.GRID.removeRows(this.POINTS_PER_ROW);
                this.setNextBlock();

                score += points;
                this.GRID.displayScore(this.score, this.highScore);
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}
}
