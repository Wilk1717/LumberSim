package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.Collections;
import java.util.List;

/**
 * Klasa ekologicznego drwala.
 * Ścina drzewa tylko na polu, na którym stoi.
 */
public class EcologicalLumberjack extends Lumberjack {

    /**
     * Stworzenie nowego ekonomicznego drwala.
     * @param startX Początkowa współrzędna X.
     * @param startY Początkowa współrzędna Y.
     * @param board Referencja do planszy symulacji.
     * @param visionRange Zasięg widzenia, w którym drwal poszukuje drzewa do ścięcia.
     * @param initialCapital Początkowy stan kapitału drwala.
     * @param regrowthTime Czas potrzebny na odrośnięcie ściętego przez niego drzewa.
     * @param treeValue Zysk dla drwala za ścięcie jednego drzewa.
     * @param livingCost Koszt życia pobierany od każdego drwala w każdym ticku symulacji.
     * @param cuttingRange Zasięg cięcia (dla drwala ekologicznego domyślnie wynosi 0).
     */
    public EcologicalLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int livingCost, int cuttingRange) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue,  livingCost, cuttingRange);
    }

    /**
     * Logika decyzyjna ekologicznego drwala.
     * Agent skaunuje otoczenie w poszukiwaniu najbliższego drzewa.
     * Podchodzi do niego o jeden krok, jeżeli na nim stoi, ścina je.
     */
    @Override
    public void findTarget() {

        //Skanowanie otoczenia w poszukiwaniu kolejnych drzew w polu widzenia.
        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);
        //Przetasowanie listy drzew w celu uniknięcia powtarzającego się wzorca ruchu.
        Collections.shuffle(neighbors);

        Cell closestTree = null;
        int minDistance = Integer.MAX_VALUE;

        //Filtrowanie wszystkich widocznych komórek w celu znalezienia najbliższego drzewa.
        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {

                int distance = board.calculateDistance(this.x, this.y, neighbor.getX(), neighbor.getY());

                if (distance < minDistance) {
                    minDistance = distance;
                    closestTree = neighbor;
                }
            }
        }

        //Wykonanie kroku w stronę drzewa.
        if (closestTree != null) {
            moveToTarget(closestTree);
        } else {
            //Jeśli nie ma drzew w polu widzenia, wykonanie losowego kroku.
            moveRandomly();
        }

        //Jeśli drwal stoi na drzewie, ścina je
        Cell currentCell = board.getCell(this.x, this.y);
        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }
    }
}