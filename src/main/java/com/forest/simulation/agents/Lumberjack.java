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
    public Lumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue) {
        super(startX, startY, board);
        this.visionRange = visionRange;
        this.capital = initialCapital;
        this.regrowthTime = regrowthTime;
        this.treeValue = treeValue;
    }

    //Ścięcie drzewa
    public void harvest(Cell targetCell) {
        targetCell.chopDown(this.regrowthTime);
    }

    //Koszty codziennego utrzymania
    public void payCosts() { }                                                                                          //TODO: napisać tą metodę

    //Sprawdzenie, czy kapitał drwala nie spadł poniżej 0
    public void checkBankruptcy() { }                                                                                   //TODO: napisać tą metodę
}