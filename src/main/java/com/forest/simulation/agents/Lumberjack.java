package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;

public abstract class Lumberjack extends Agent {
    protected int capital;
    protected int livingCost;
    protected int cuttingRange;
    protected int visionRange;
    protected int regrowthTime;
    protected int treeValue;


    public Lumberjack(int startX, int startY, Board board, int visionRange, int initialCapital, int regrowthTime, int treeValue) {
        super(startX, startY, board);
        this.visionRange = visionRange;
        this.capital = initialCapital;
        this.regrowthTime = regrowthTime;
        this.treeValue = treeValue;
    }

    public void harvest(Cell targetCell) {

        targetCell.chopDown(this.regrowthTime);
    }

    public void payCosts() { }
    public void checkBankruptcy() { }
}