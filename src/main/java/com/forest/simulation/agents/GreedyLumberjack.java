package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

public class GreedyLumberjack extends Lumberjack {

    private int cooldown = 0;

    public GreedyLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int livingCost, int cuttingRange) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue, livingCost, cuttingRange);
    }

    public boolean isOnCooldown() {
        return cooldown > 0;
    }

    public void setCooldown(int ticks) {
        this.cooldown = ticks;
    }

    @Override
    public void findTarget() {

        if (cooldown > 0) {
            cooldown--;
            moveRandomly();
            return;
        }

        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        Cell bestTree = null;
        int maxClusterSize = -1;
        int minDistance = Integer.MAX_VALUE;

        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {

                int distanceToNeighbor = board.calculateDistance(this.x, this.y, neighbor.getX(), neighbor.getY());

                List<Cell> clusterCheck = board.getNeighbors(neighbor.getX(), neighbor.getY(), 1);
                int clusterSize = 0;

                for (Cell clusterCell : clusterCheck) {
                    if (clusterCell.getState().equals("Tree")) {

                        int distanceToLumberjack = board.calculateDistance(this.x, this.y, clusterCell.getX(), clusterCell.getY());

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

        if (bestTree != null) {
            moveToTarget(bestTree);
        } else {
            moveRandomly();
        }

        Cell currentCell = board.getCell(this.x, this.y);
        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }

        List<Cell> cuttingArea = board.getNeighbors(this.x, this.y, this.cuttingRange);
        for (Cell target : cuttingArea) {
            if (target.getState().equals("Tree")) {
                harvest(target);
            }
        }
    }
}