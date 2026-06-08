package com.forest.simulation.core;

public class SimulationParameters {
    private int forestDensity = 70;
    private int regrowthTime = 8;
    private int penaltyCooldown = 15;
    private int numEcologicalLumberjacks = 6;
    private int numGreedyLumberjacks = 12;
    private int numForestRangers = 6;
    private int agentVisionRange = 2;
    private int livingCost = 6;
    private int fineAmount = 60;
    private int initialCapital = 100;
    private int treeValue = 9;
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
