package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.Random;

public abstract class Agent {
    protected int x;
    protected int y;
    protected Board board;
    protected Random random;

    public Agent(int startX, int startY, Board board) {
        this.x = startX;
        this.y = startY;
        this.board = board;
        this.random = new Random();
    }

    public abstract void findTarget();

    public void moveRandomly() {
        int dx, dy;
        do {
            dx = random.nextInt(3) - 1;
            dy = random.nextInt(3) - 1;
        } while (dx == 0 && dy == 0); // Zapobiega staniu w miejscu

        this.x = board.wrapCoordinate(this.x + dx, board.getWidth());
        this.y = board.wrapCoordinate(this.y + dy, board.getHeight());
    }

    public void moveToTarget(Cell target) {
        if (target == null) return;

        int dx = target.getX() - this.x;
        int dy = target.getY() - this.y;


        if (Math.abs(dx) > board.getWidth() / 2) {
            dx = (dx > 0) ? dx - board.getWidth() : dx + board.getWidth();
        }
        if (Math.abs(dy) > board.getHeight() / 2) {
            dy = (dy > 0) ? dy - board.getHeight() : dy + board.getHeight();
        }


        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);

        this.x = board.wrapCoordinate(this.x + stepX, board.getWidth());
        this.y = board.wrapCoordinate(this.y + stepY, board.getHeight());
    }

    public int getX() { return x; }
    public int getY() { return y; }
}