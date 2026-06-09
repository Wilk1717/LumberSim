package com.forest.simulation.core;

public class SimulationParameters {
    private int forestDensity = 50;
    private int regrowthTime = 40;
    private int penaltyCooldown = 15;
    private int numEcologicalLumberjacks = 10;
    private int numGreedyLumberjacks = 30;
    private int numForestRangers = 10;
    private int agentVisionRange = 3;
    private int livingCost = 5;
    private int fineAmount = 50;
    private int initialCapital = 100;
    private int treeValue = 10;
    private int greedyCuttingRange = 1;
    private int rangerPatrolRange = 8;

    public SimulationParameters() {}

    public int getForestDensity() { return forestDensity; }
    public int getRegrowthTime() { return regrowthTime; }
    public int getPenaltyCooldown() { return penaltyCooldown; }
    public int getNumEcologicalLumberjacks() { return numEcologicalLumberjacks; }
    public int getNumGreedyLumberjacks() { return numGreedyLumberjacks; }
    public int getNumForestRangers() { return numForestRangers; }
    public int getAgentVisionRange() { return agentVisionRange; }
    public int getLivingCost() { return livingCost; }
    public int getFineAmount() { return fineAmount; }
    public int getInitialCapital() { return initialCapital; }
    public int getTreeValue() { return treeValue; }
    public int getGreedyCuttingRange() { return greedyCuttingRange; }
    public int getRangerPatrolRange() { return rangerPatrolRange; }
}
