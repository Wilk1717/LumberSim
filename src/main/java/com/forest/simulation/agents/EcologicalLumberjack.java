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

        //Skanowanie otoczenia w poszukiwaniu kolejnych drzew w polu widzenia
        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);
        Cell closestTree = null;
        int minDistance = Integer.MAX_VALUE;

        //Filtrowanie wszystkich widocznych komórek w celu znalezienia najbliższego drzewa
        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {
                
                int distance = board.calculateDistance(this.x, this.y, neighbor.getX(), neighbor.getY());

                if (distance < minDistance) {
                    minDistance = distance;
                    closestTree = neighbor;
                }
            }
        }

        //Wykonanie kroku w stronę drzewa
        if (closestTree != null) {
            moveToTarget(closestTree);
        } else {
            //Jeśli nie ma drzew w polu widzenia, wykonanie losowego kroku
            moveRandomly();
        }

        //Jeśli drwal stoi na drzewie, ścina je
        Cell currentCell = board.getCell(this.x, this.y);
        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }
    }
}