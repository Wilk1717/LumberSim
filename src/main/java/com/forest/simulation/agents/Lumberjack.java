package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;

public abstract class Lumberjack extends Agent {
    //Parametry drwala
    protected int capital;
    protected int livingCost;
    protected int cuttingRange;
    protected int visionRange;
    protected int regrowthTime;
    protected int treeValue;

    //Konstruktor drwala
    public Lumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue, int livingCost,  int cuttingRange) {
        super(startX, startY, board);
        this.visionRange = visionRange;
        this.capital = initialCapital;
        this.regrowthTime = regrowthTime;
        this.treeValue = treeValue;
        this.livingCost = livingCost;
        this.cuttingRange = cuttingRange;
    }

    //Ścięcie drzewa
    public void harvest(Cell targetCell) {
        targetCell.chopDown(this.regrowthTime);
        this.capital += this.treeValue;
    }

    //Koszty codziennego utrzymania
    public void payCosts() {
        this.capital -= this.livingCost;
    }

    //Sprawdzenie, czy kapitał drwala nie spadł poniżej 0
    public boolean checkBankruptcy() {
        return this.capital < 0;
    }
}