package com.company;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class BlockManager {
    ArrayList<Shape> shapes;
    Color[] COLORS = new Color[] {
            Color.orange,
            Color.green,
            Color.red,
            Color.blue,
            Color.yellow
    };

    public BlockManager() {
        ArrayList<boolean[][]> lStates = new ArrayList<boolean[][]>();
        ArrayList<boolean[][]> iStates = new ArrayList<boolean[][]>();
        ArrayList<boolean[][]> tStates = new ArrayList<boolean[][]>();
        ArrayList<boolean[][]> jStates = new ArrayList<boolean[][]>();
        ArrayList<boolean[][]> sStates = new ArrayList<boolean[][]>();
        ArrayList<boolean[][]> zStates = new ArrayList<boolean[][]>();
        ArrayList<boolean[][]> oStates = new ArrayList<boolean[][]>();

        lStates.add(new boolean[][] {
                { false, false, false },
                { true, true, true },
                { true, false, false }
        });
        lStates.add(new boolean[][] {
                { true, true, false },
                { false, true, false },
                { false, true, false }
        });
        lStates.add(new boolean[][] {
                { false, false, false },
                { false, false, true },
                { true, true, true }
        });
        lStates.add(new boolean[][] {
                { false, true, false },
                { false, true, false },
                { false, true, true }
        });

        iStates.add(new boolean[][] {
                { false, false, false, false },
                { true, true, true, true },
                { false, false, false, false },
                { false, false, false, false }
        });
        iStates.add(new boolean[][] {
                { false, false, true, false },
                { false, false, true, false },
                { false, false, true, false },
                { false, false, true, false },
        });

        tStates.add(new boolean[][] {
                { false, false, false },
                { true, true, true },
                { false, true, false }
        });
        tStates.add(new boolean[][] {
                { false, true, false },
                { true, true, false },
                { false, true, false }
        });
        tStates.add(new boolean[][] {
                { false, false, false },
                { false, true, false },
                { true, true, true }
        });
        tStates.add(new boolean[][] {
                { false, true, false },
                { false, true, true },
                { false, true, false }
        });

        jStates.add(new boolean[][] {
                { false, false, false },
                { true, true, true },
                { false, false, true }
        });
        jStates.add(new boolean[][] {
                { false, true, false },
                { false, true, false },
                { true, true, false }
        });
        jStates.add(new boolean[][] {
                { false, false, false },
                { true, false, false },
                { true, true, true }
        });
        jStates.add(new boolean[][] {
                { false, true, true },
                { false, true, false },
                { false, true, false }
        });

        sStates.add(new boolean[][] {
                { false, false, false },
                { false, true, true },
                { true, true, false }
        });
        sStates.add(new boolean[][] {
                { true, false, false },
                { true, true, false },
                { false, true, false }
        });

        zStates.add(new boolean[][] {
                { false, false, false },
                { true, true, false },
                { false, true, true }
        });
        zStates.add(new boolean[][] {
                { false, false, true },
                { false, true, true },
                { false, true, false }
        });

        oStates.add(new boolean[][] {
                { true, true },
                { true, true }
        });

        this.shapes = new ArrayList<Shape>();

        shapes.add(new Shape(lStates.size(), lStates, new Pair(-1, -1)));
        shapes.add(new Shape(iStates.size(), iStates, new Pair(-1, -1)));
        shapes.add(new Shape(tStates.size(), tStates, new Pair(-1, -1)));
        shapes.add(new Shape(jStates.size(), jStates, new Pair(-1, -1)));
        shapes.add(new Shape(sStates.size(), sStates, new Pair(-1, -1)));
        shapes.add(new Shape(zStates.size(), zStates, new Pair(-1, -1)));
        shapes.add(new Shape(oStates.size(), oStates, new Pair(-1, -1)));
    }

    public Shape getRandomShape() {
        int index = new Random().nextInt(this.shapes.size());
        Shape shape = this.shapes.get(index).getClone();
        shape.setColor(this.getRandomColor());
        return shape;
    }

    private Color getRandomColor() {
        int index = new Random().nextInt(this.COLORS.length);
        Color color = this.COLORS[index];
        return color;
    }

}
