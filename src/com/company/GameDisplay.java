package com.company;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameDisplay extends JFrame {
    private final int WIDTH = 900, HEIGHT = 1000;
    private final int ROWS = 30, COLS = 30;
    private final int BLOCK_SZ = 30;
    private final int FALL_DIST = 25;
    private final int PLATFORM_WIDTH = 10;
    private final Color PLATFORM_COLOR = Color.gray;
    private final Block PLATFORM_BLOCK = new Block(PLATFORM_COLOR, true);
    private final Color EMPTY_COLOR = Color.black;
    private final Color BACKGROUND_COLOR = Color.darkGray;
    private final int BLOCK_BORDER = 1;
    private final Pair NEXT_BLOCK_POS = new Pair(20, 4); // Top left coord
    private final Pair BLOCK_HOLD_POS = new Pair(20, 24); // Top left coord
    private final Pair BLOCK_START_POS = new Pair(23, 14); // Top left coord
    private final int PLATFORM_LEFT_COL = 9;
    private final int GAMEOVER_ROW = 20;
    private final Font SCORE_FONT = new Font("Monospace", Font.BOLD, 25);
    private final Color SCORE_LBL_BACKGROUND_COLOR = Color.white;

    private Block[][] grid;
    private Shape heldBlock;
    private Shape currentBlock;
    private Shape nextBlock;
    private JLabel scoreLbl;
    private JLabel highscoreLbl;

    public GameDisplay(int highscore) {
        super("Tetris");
        setSize(this.WIDTH, this.HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBackground(this.BACKGROUND_COLOR);
        setVisible(true);

        Container contentPane = this.getContentPane();
        contentPane.setBackground(this.BACKGROUND_COLOR);
        contentPane.setSize(WIDTH, HEIGHT);
        contentPane.setLayout(null);

        scoreLbl = new JLabel("Score: 0");
        scoreLbl.setForeground(Color.white);
        scoreLbl.setFont(this.SCORE_FONT);
        scoreLbl.setBounds(20, HEIGHT - 60, 300, 25);
        scoreLbl.setBackground(this.SCORE_LBL_BACKGROUND_COLOR);
        contentPane.add(scoreLbl);

        highscoreLbl = new JLabel("High Score: " + highscore);
        highscoreLbl.setForeground(Color.white);
        highscoreLbl.setFont(this.SCORE_FONT);
        highscoreLbl.setBounds(420, HEIGHT - 60, 300, 25);
        highscoreLbl.setBackground(this.SCORE_LBL_BACKGROUND_COLOR);
        contentPane.add(highscoreLbl);
        this.initScene();
    }

    public void paint(Graphics g) {
        if (this.grid == null) {
            return;
        }
        this.drawGridBlocks(g);
        this.drawActiveBlocks(g);
    }

    // Display score
    public void displayScore(int score, int highScore) {
        this.scoreLbl.setText("Score: " + score);
        if (score > highScore) {
            this.highscoreLbl.setText("High Score: " + score);
        }
    }

    // Draw blocks in grid
    private void drawGridBlocks(Graphics g) {
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                if (this.isActiveUnit(i, j)) { continue; }
                g.setColor(this.grid[i][j].getColor());
                drawBlock(g, i, j);
            }
        }
    }

    // Determines whether a block is active
    private boolean isActiveUnit(int row, int col) {
        for (Pair loc : this.currentBlock.getActiveBlocks()) {
            if (loc.first == row && loc.second == col) {
                return true;
            }
        }
        for (Pair loc : this.nextBlock.getActiveBlocks()) {
            if (loc.first == row && loc.second == col) {
                return true;
            }
        }
        if (this.heldBlock != null) {
            for (Pair loc : this.heldBlock.getActiveBlocks()) {
                if (loc.first == row && loc.second == col) {
                    return true;
                }
            }
        }
        return false;
    }

    // Draw blocks not yet in grid
    private void drawActiveBlocks(Graphics g) {
        g.setColor(this.currentBlock.getColor());
        for (Pair location : this.currentBlock.getActiveBlocks()) {
            int row = location.first;
            int col = location.second;
            this.drawBlock(g, row, col);
        }

        g.setColor(this.nextBlock.getColor());
        for (Pair location : this.nextBlock.getActiveBlocks()) {
            int row = location.first;
            int col = location.second;
            this.drawBlock(g, row, col);
        }

        if (this.heldBlock != null) {
            g.setColor(this.heldBlock.getColor());
            for (Pair location : this.heldBlock.getActiveBlocks()) {
                int row = location.first;
                int col = location.second;
                this.drawBlock(g, row, col);
            }
        }
    }

    // Creates game frame display
    private void initScene() {
        this.grid = new Block[this.ROWS][this.COLS];
        // Initialize all blocks to be empty background
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                this.grid[i][j] = new Block(this.EMPTY_COLOR, false);
            }
        }
        // Draw game pillars
        for (int i = 0; i < this.ROWS; i++) {
            grid[i][this.PLATFORM_LEFT_COL] = this.PLATFORM_BLOCK;
            grid[i][this.PLATFORM_LEFT_COL + PLATFORM_WIDTH + 1] = this.PLATFORM_BLOCK;
        }

        // Draw border around scene
        for (int i = 0; i < this.ROWS; i++) {
            grid[i][0] = this.PLATFORM_BLOCK;
            grid[i][this.COLS - 1] = this.PLATFORM_BLOCK;
        }

        for (int i = 0; i < this.COLS; i++) {
            grid[0][i] = this.PLATFORM_BLOCK;
            grid[this.ROWS - 1][i] = this.PLATFORM_BLOCK;
        }
    }

    private void drawBlock(Graphics g, int row, int col) {
        int x = this.colToX(col);
        int y = this.rowToY(row);
        g.fill3DRect(x + this.BLOCK_BORDER, y + this.BLOCK_BORDER, this.BLOCK_SZ - this.BLOCK_BORDER, this.BLOCK_SZ - this.BLOCK_BORDER, false);
//        g.fillRect(x + this.BLOCK_BORDER, y + this.BLOCK_BORDER, this.BLOCK_SZ - this.BLOCK_BORDER, this.BLOCK_SZ - this.BLOCK_BORDER);
//        g.fillOval(x + this.BLOCK_BORDER, y + this.BLOCK_BORDER, this.BLOCK_SZ - this.BLOCK_BORDER, this.BLOCK_SZ - this.BLOCK_BORDER);
    }

    private int rowToY(int row) {
        int y = this.BLOCK_SZ * (this.ROWS - row);
        return y;
    }

    private int colToX(int col) {
        int x = this.BLOCK_SZ * (this.COLS - 1 - col);
        return x;
    }

    // Removes rows and awards points
    public int removeRows(final int POINTS_PER_ROW) {
        int removed = 0;
        for (int i = 1; i < this.FALL_DIST; i++) {
            boolean filled = true;
            for (int j = this.PLATFORM_LEFT_COL + 1; j < this.PLATFORM_LEFT_COL + this.PLATFORM_WIDTH; j++) {
                if (!this.grid[i][j].isTaken()) {
                    filled = false;
                    break;
                }
            }
            if (filled) {
                for (int k = i; k < this.FALL_DIST - 1; k++) {
                    for (int j = this.PLATFORM_LEFT_COL + 1; j < this.PLATFORM_LEFT_COL + this.PLATFORM_WIDTH; j++) {
                        this.grid[k][j] = this.grid[k+1][j];
                    }
                }
                removed++;
                i--; // the next row has been moved down
            }
        }
        int points = removed * POINTS_PER_ROW;
        return points;
    }

    // Determines whether a block can be moved left
    public boolean canMoveLeft(ArrayList<Pair> activeBlocks) {
        for (Pair location : activeBlocks) {
            int row = location.first;
            int col = location.second;
            if (this.grid[row][col+1].isTaken()) {
                return false;
            }
        }
        return true;
    }

    // Determines whether a block can be moved right
    public boolean canMoveRight(ArrayList<Pair> activeBlocks) {
        for (Pair location : activeBlocks) {
            int row = location.first;
            int col = location.second;
            if (this.grid[row][col-1].isTaken()) {
                return false;
            }
        }
        return true;
    }

    // Determines whether a block has touch the ground
    public boolean onGround(ArrayList<Pair> activeBlocks) {
        for (Pair location : activeBlocks) {
            int row = location.first;
            int col = location.second;
            if (this.grid[row-1][col].isTaken()) {
                return true;
            }
        }
        return false;
    }

    // Determine whether a block is colliding
    public boolean colliding(ArrayList<Pair> activeBlocks) {
        for (Pair location : activeBlocks) {
            int row = location.first;
            int col = location.second;
            if (this.grid[row][col].isTaken()) {
                return true;
            }
        }
        return false;
    }

    // Makes grounded shapes turn into walls (returns false if game over)
    public boolean freezeBlocks(ArrayList<Pair> activeBlocks, Color color) {
        Block block = new Block(color, true);
        boolean gameover = false;
        for (Pair location : activeBlocks) {
            int row = location.first;
            int col = location.second;
            this.grid[row][col] = block;

            if (row >= this.GAMEOVER_ROW) {
                gameover = true;
            }
        }
        return gameover;
    }


    public void setNextBlock(Shape nextBlock) {
        this.nextBlock = nextBlock;
    }

    public void setCurrentBlock(Shape currentBlock) {
        this.currentBlock = currentBlock;
    }

    public void setHeldBlock(Shape heldBlock) {
        this.heldBlock = heldBlock;
    }


    public Pair getBlockHoldPosition() {
        return this.BLOCK_HOLD_POS.clone();
    }

    public Pair getNextBlockPos() {
        return this.NEXT_BLOCK_POS.clone();
    }

    public Pair getBlockStartPos() {
        return this.BLOCK_START_POS.clone();
    }
}