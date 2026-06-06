package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

public class EcologicalLumberjack extends Lumberjack {

    //Konstruktor ekologicznego drwala
    public EcologicalLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int livingCost, int cuttingRange) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue,  livingCost, cuttingRange);
    }

    //Cykl pozwalający ściąć drzewo, namierzyć kolejne i wykonać krok
    @Override
    public void findTarget() {
        Cell currentCell = board.getCell(this.x, this.y);

        //Jeśli drwal stoi na drzewie, ścina je
        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }

        //Skanowanie otoczenia w poszukiwaniu kolejnych drzew w polu widzenia
        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        Cell closestTree = null;
        int minDistance = Integer.MAX_VALUE;

        //Filtrowanie wszystkich widocznych komórek w celu znalezienia najbliższego drzewa
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

        //Jeśli nie ma drzew w polu widzenia, wykonanie losowego kroku
        moveRandomly();
    }
}