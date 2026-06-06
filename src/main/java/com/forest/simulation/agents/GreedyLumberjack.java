package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

public class GreedyLumberjack extends Lumberjack {

    public GreedyLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int  livingCost, int cuttingRange) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue,  livingCost, cuttingRange);
    }

    @Override
    public void findTarget() {
        Cell currentCell = board.getCell(this.x, this.y);

        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }

        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        Cell bestTree = null;
        int maxClusterSize = -1;
        int minDistance = Integer.MAX_VALUE;

        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {

                int distToNeighborX = Math.abs(neighbor.getX() - this.x);
                if (distToNeighborX > board.getWidth() / 2) distToNeighborX = board.getWidth() - distToNeighborX;

                int distToNeighborY = Math.abs(neighbor.getY() - this.y);
                if (distToNeighborY > board.getHeight() / 2) distToNeighborY = board.getHeight() - distToNeighborY;

                int distanceToNeighbor = Math.max(distToNeighborX, distToNeighborY);

                List<Cell> clusterCheck = board.getNeighbors(neighbor.getX(), neighbor.getY(), 1);
                int clusterSize = 0;

                for (Cell clusterCell : clusterCheck) {
                    if (clusterCell.getState().equals("Tree")) {

                        int dx = Math.abs(clusterCell.getX() - this.x);
                        if (dx > board.getWidth() / 2) dx = board.getWidth() - dx;

                        int dy = Math.abs(clusterCell.getY() - this.y);
                        if (dy > board.getHeight() / 2) dy = board.getHeight() - dy;

                        int distanceToLumberjack = Math.max(dx, dy);

                        if (distanceToLumberjack <= this.visionRange) {
                            clusterSize++;
                        }
                    }
                }

                if (clusterSize > maxClusterSize || (clusterSize == maxClusterSize && distanceToNeighbor < minDistance)) {
                    maxClusterSize = clusterSize;
                    minDistance = distanceToNeighbor;
                    bestTree = neighbor;
                }
            }
        }



        moveRandomly();
    }
}