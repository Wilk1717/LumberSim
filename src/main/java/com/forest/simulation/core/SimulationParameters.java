package com.forest.simulation.core;

/**
 * Klasa przechowująca wszystkie parametry symulacji.
 */
public class SimulationParameters {
    private static SimulationParameters instance;

    private int forestDensity = 50;
    private int regrowthTime = 8;
    private int penaltyCooldown = 15;
    private int numEcologicalLumberjacks = 6;
    private int numGreedyLumberjacks = 12;
    private int numForestRangers = 6;
    private int agentVisionRange = 3;
    private int livingCost = 6;
    private int fineAmount = 60;
    private int initialCapital = 100;
    private int treeValue = 9;
    private int greedyCuttingRange = 1;
    private int rangerPatrolRange = 8;

    /**
     * Prywatny konstruktor.
     */
    private SimulationParameters() {}

    /**
     * Punkt dostępu do parametrów symulacji.
     * @return Statyczna instancja tej klasy.
     */
    public static SimulationParameters getInstance() {
        if (instance == null) {
            instance = new SimulationParameters();
        }
        return instance;
    }

    /**
     * @return Procent zalesienia planszy (od 0 do 100).
     */
    public int getForestDensity() { return forestDensity; }

    /**
     * @return Czas (w tickach) potrzebny do odrośnięcia drzewa.
     */
    public int getRegrowthTime() { return regrowthTime; }

    /**
     * @return Czas trwania cooldownu (w tickach), nakładany na drwala po złapaniu przez strażnika.
     */
    public int getPenaltyCooldown() { return penaltyCooldown; }

    /**
     * @return Liczba drwali ekologicznych na planszy.
     */
    public int getNumEcologicalLumberjacks() { return numEcologicalLumberjacks; }

    /**
     * @return Liczba drwali chciwych na planszy.
     */
    public int getNumGreedyLumberjacks() { return numGreedyLumberjacks; }

    /**
     * @return Liczba strażników leśnych na planszy.
     */
    public int getNumForestRangers() { return numForestRangers; }

    /**
     * @return Zasięg widzenia drwali (w kratkach), w którym poszukują oni drzew do ścięcia.
     */
    public int getAgentVisionRange() { return agentVisionRange; }

    /**
     * @return Koszt życia pobierany od każdego drwala w każdym ticku symulacji.
     */
    public int getLivingCost() { return livingCost; }

    /**
     * @return Wysokość kary finansowej nakładanej na chciwego drwala przez strażnika.
     */
    public int getFineAmount() { return fineAmount; }

    /**
     * @return Początkowy kapitał przydzielany drwalom na starcie symulacji.
     */
    public int getInitialCapital() { return initialCapital; }

    /**
     * @return Zysk dla drwala za ścięcie jednego drzewa.
     */
    public int getTreeValue() { return treeValue; }

    /**
     * @return Promień obszaru ścinania drzew przez chciwego drwala.
     */
    public int getGreedyCuttingRange() { return greedyCuttingRange; }

    /**
     * @return Zasięg widzenia strażników leśnych, w którym wykrywają oni chciwych drwali.
     */
    public int getRangerPatrolRange() { return rangerPatrolRange; }
}
