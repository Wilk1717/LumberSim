package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

/**
 * Klasa chciwego drwala.
 * Chciwy drwal szuka największych skupisk drzew i wycina je obszarowo.
 * Może zostać ukarany przez strażnika leśnego, co skutkuje czasową blokadą ścinania.
 */
public class GreedyLumberjack extends Lumberjack {

    private int cooldown = 0;

    /**
     * Inicjalizacja parametrów chciwego drwala.
     * @param startX Początkowa współrzędna X.
     * @param startY Początkowa współrzędna Y.
     * @param board Referencja do planszy symulacji.
     * @param visionRange Zasięg widzenia, w którym drwal poszukuje skupisk drzew do ścięcia.
     * @param initialCapital Początkowy stan kapitału drwala.
     * @param regrowthTime Czas potrzebny na odrośnięcie ściętego przez niego drzewa.
     * @param treeValue Zysk dla drwala za ścięcie jednego drzewa.
     * @param livingCost Koszt życia pobierany od każdego drwala w każdym ticku symulacji.
     * @param cuttingRange Zasięg cięcia obszarowego.
     */
    public GreedyLumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int livingCost, int cuttingRange) {
        super(startX, startY, board, visionRange, initialCapital, regrowthTime, treeValue, livingCost, cuttingRange);
    }

    /**
     * Sprawdzenie, czy drwal aktualnie odbywa karę.
     * @return True, jeśli drwal ma aktywną karę (cooldown > 0). False w przeciwnym wypadku.
     */
    public boolean isOnCooldown() {
        return cooldown > 0;
    }

    /**
     * Nałożenie kary na drwala, uniemożliwiającej mu ścinanie drzew na określony czas.
     * @param ticks Liczba ticków symulacji, przez które drwal będzie zablokowany.
     */
    public void setCooldown(int ticks) {
        this.cooldown = ticks;
    }

    /**
     * Mechanika poszukiwania i wycinania drzew.
     * Jeśli drwal odbywa karę, porusza się losowo i zmniejsza wartość licznika.
     * W przeciwnym razie drwal skanuje otoczenie w poszukiwaniu największego zagęszczenia drzew,
     * przemieszcza się w jego stronę, a następnie wycina drzewo, na którym stoi, oraz
     * wszystkie okoliczne drzewa w promieniu swojego zasięgu cięcia.
     */
    @Override
    public void findTarget() {

        // Jeśli drwal ma nałożoną karę (mandat), zmniejsza licznik i porusza się losowo, nie ścinając drzew.
        if (cooldown > 0) {
            cooldown--;
            moveRandomly();
            return;
        }

        // Skanowanie otoczenia w poszukiwaniu drzew w polu widzenia.
        List<Cell> neighbors = board.getNeighbors(this.x, this.y, this.visionRange);

        Cell bestTree = null;
        int maxClusterSize = -1;
        int minDistance = Integer.MAX_VALUE;

        // Przeszukiwanie widocznych drzew w celu znalezienia tego, wokół którego jest największe skupisko (klaster).
        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {

                int distanceToNeighbor = board.calculateDistance(this.x, this.y, neighbor.getX(), neighbor.getY());

                // Sprawdzanie bezpośredniego sąsiedztwa badanego drzewa (szukanie klastra).
                List<Cell> clusterCheck = board.getNeighbors(neighbor.getX(), neighbor.getY(), 1);
                int clusterSize = 0;

                for (Cell clusterCell : clusterCheck) {
                    if (clusterCell.getState().equals("Tree")) {

                        int distanceToLumberjack = board.calculateDistance(this.x, this.y, clusterCell.getX(), clusterCell.getY());

                        // Upewnienie się, że drwal jest w stanie zobaczyć ten klaster.
                        if (distanceToLumberjack <= this.visionRange) {
                            clusterSize++;
                        }
                    }
                }

                // Wybór drzewa z największym klastrem (lub bliższego, jeśli klastry są równe).
                if (clusterSize > maxClusterSize || (clusterSize == maxClusterSize && distanceToNeighbor < minDistance)) {
                    maxClusterSize = clusterSize;
                    minDistance = distanceToNeighbor;
                    bestTree = neighbor;
                }
            }
        }

        // Wykonanie kroku w stronę wybranego, najbardziej opłacalnego drzewa.
        if (bestTree != null) {
            moveToTarget(bestTree);
        } else {
            // Jeśli nie ma drzew w polu widzenia, wykonanie losowego kroku.
            moveRandomly();
        }

        // Jeśli po ruchu drwal stoi na drzewie, ścina je (główny cel).
        Cell currentCell = board.getCell(this.x, this.y);
        if (currentCell.getState().equals("Tree")) {
            harvest(currentCell);
        }

        // Chciwe ścięcie wszystkich pozostałych drzew w promieniu zasięgu cięcia.
        List<Cell> cuttingArea = board.getNeighbors(this.x, this.y, this.cuttingRange);
        for (Cell target : cuttingArea) {
            if (target.getState().equals("Tree")) {
                harvest(target);
            }
        }
    }
}