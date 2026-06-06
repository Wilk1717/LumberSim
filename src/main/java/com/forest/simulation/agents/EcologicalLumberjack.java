package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

public class EcologicalLumberjack extends Lumberjack {

    public EcologicalLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue);
    }

    @Override
    public void findTarget() {
        Cell currentCell = board.getCell(this.x, this.y);

        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }

        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        Cell closestTree = null;
        int minDistance = Integer.MAX_VALUE;


        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {
                int dx = Math.abs(neighbor.getX() - this.x);
                if (dx > board.getWidth() / 2) dx = board.getWidth() - dx;

                int dy = Math.abs(neighbor.getY() - this.y);
                if (dy > board.getHeight() / 2) dy = board.getHeight() - dy;

                int distance = Math.max(Math.abs(dx), Math.abs(dy));


                if (distance < minDistance) {
                    minDistance = distance;
                    closestTree = neighbor;
                }
            }
        }

        if (closestTree != null) {
            moveToTarget(closestTree);
            return;
        }

        moveRandomly();
    }
}