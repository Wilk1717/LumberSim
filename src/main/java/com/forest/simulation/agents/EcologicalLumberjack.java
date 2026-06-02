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
            return;
        }


        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {
                moveToTarget(neighbor);
                return;
            }
        }

        moveRandomly();
    }
}
