package com.company;

public class Main {
    public static void main(String[] args) {
        Game tetris = new Game();
        tetris.start();
        while (true) {
            boolean gameover = tetris.update();
            if (gameover) {
                int score = tetris.quit();
                System.out.println("GAME OVER");
                System.out.println("Final Score: " + score);
                break;
            }
        }
    }
}
