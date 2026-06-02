package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

public class EcologicalLumberjack extends Lumberjack {
    //Konstruktor ekologicznego drwala
    public EcologicalLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue);
    }

    //Cykl drwala pozwalający ściąć drzewo, następnie namierzyć kolejne i zrobić krok w jego stronę w jednym ticku
    @Override
    public void findTarget() {
        Cell currentCell = board.getCell(this.x, this.y);

        //Ścięcie drzewa, jeśli drwal na nim stoi
        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }

        //Skanowanie otoczenia w poszukiwaniu kolejnego drzewa
        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        Cell closestTree = null;
        int minDistance = Integer.MAX_VALUE;

        //Szukanie najbliższego możliwego drzewa
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

        //Wykonanie kroku w stronę drzewa
        if (closestTree != null) {
            moveToTarget(closestTree);
            return;
        }

        //W przypadku braku drzew w zasięgu wzroku, wykonanie losowego kroku
        moveRandomly();
    }
}