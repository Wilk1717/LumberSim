package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
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
        } while (dx == 0 && dy == 0);

        this.x = board.wrapCoordinate(this.x + dx, board.getWidth());
        this.y = board.wrapCoordinate(this.y + dy, board.getHeight());
    }

    public void moveToTarget() {
    }

    public int getX() { return x; }
    public int getY() { return y; }
}