package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;

/**
 * Abstrakcyjna klasa wszystkich drwali.
 */
public abstract class Lumberjack extends Agent {
    protected int capital;
    protected int livingCost;
    protected int cuttingRange;
    protected int visionRange;
    protected int regrowthTime;
    protected int treeValue;

    /**
     * Inicjalizacja parametrów drwala.
     * @param startX Początkowa współrzędna X.
     * @param startY Początkowa współrzędna Y.
     * @param board Referencja do planszy symulacji.
     * @param visionRange Zasięg widzenia, w którym drwal poszukuje drzewa do ścięcia.
     * @param initialCapital Początkowy stan kapitału drwala.
     * @param regrowthTime Czas potrzebny na odrośnięcie ściętego przez niego drzewa.
     * @param treeValue Zysk dla drwala za ścięcie jednego drzewa.
     * @param livingCost Koszt życia pobierany od każdego drwala w każdym ticku symulacji.
     * @param cuttingRange Zasięg cięcia.
     */
    public Lumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int livingCost,  int cuttingRange) {
        super(startX, startY, board);
        this.visionRange = visionRange;
        this.capital = initialCapital;
        this.regrowthTime = regrowthTime;
        this.treeValue = treeValue;
        this.livingCost = livingCost;
        this.cuttingRange = cuttingRange;
    }

    /**
     * Mechanika wycinki drzewa i zarabiania pieniędzy.
     * Zawiera logikę odrastania drzewa tylko, gdy w pobliżu znajduje się dorosłe drzewo.
     * @param target Komórka planszy, na której znajduje się drzewo do ścięcia.
     */
    public void harvest(Cell target) {
        //Zysk za ścięcie drzewa.
        this.capital += this.treeValue;

        //Usunięcie drzewa z planszy.
        target.setState("Empty");

        //Sprawdzenie warunku sąsiedztwa dorosłego drzewa.
        if (board.hasAdultTreeNeighbor(target.getX(), target.getY())) {
            target.chopDown(this.regrowthTime);
        } else {
            target.chopDown(0);
        }
    }

    /**
     * Pobranie codziennego kosztu życia od drwala.
     */
    public void payCosts() {
        this.capital -= this.livingCost;
    }

    /**
     * Sprawdzenie, czy kapitał drwala spadł poniżej 0.
     * @return True, jeśli drwal zbankrutował. False w przeciwnym wypadku.
     */
    public boolean checkBankruptcy() {
        return this.capital < 0;
    }

    /**
     * @return Aktualny stan kapitału drwala.
     */
    public int getCapital() {
        return this.capital;
    }
}