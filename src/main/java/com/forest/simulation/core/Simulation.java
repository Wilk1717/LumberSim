package com.forest.simulation.core;

import com.forest.simulation.agents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Główna klasa symulacji.
 * Odpowiada za inicjalizację środowiska, rozmieszczenie agentów i zarządzanie główną pętlą czasu.
 */
public class Simulation {
    private Board board;
    private List<Agent> agents;
    private int tick;
    private SimulationParameters params;

    /**
     * Inicjalizacja pustej symulacji o podanych wymiarach i pobranie globalnych ustawień.
     * @param width Szerokość planszy.
     * @param height Wysokość planszy.
     */
    public Simulation(int width, int height) {
        this.board = new Board(width, height);
        this.agents = new ArrayList<>();
        this.tick = 0;
        this.params = SimulationParameters.getInstance();
    }

    /**
     * Przygotowanie początkowego stanu symulacji.
     * Zalesienie planszy i losowe rozmieszczenie agentów na planszy.
     */
    public void setup() {
        Random rand = new Random();

        //Wygenerowanie lasu
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                String initialState = rand.nextInt(100) < params.getForestDensity() ? "Tree" : "Empty";

                Cell cell = new Cell(x, y, initialState, 0);
                board.setCell(x, y, cell);
            }
        }

        //Rozmieszczenie ekologicznych drwali
        for (int i = 0; i < params.getNumEcologicalLumberjacks(); i++) {
            agents.add(new EcologicalLumberjack(
                    rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()), board,
                    params.getAgentVisionRange(), params.getInitialCapital(),
                    params.getRegrowthTime(), params.getTreeValue(), params.getLivingCost(), 0
            ));
        }

        //Rozmieszczenie chciwych drwali
        for (int i = 0; i < params.getNumGreedyLumberjacks(); i++) {
            agents.add(new GreedyLumberjack(
                    rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()), board,
                    params.getAgentVisionRange(), params.getInitialCapital(),
                    params.getRegrowthTime(), params.getTreeValue(), params.getLivingCost(),
                    params.getGreedyCuttingRange()
            ));
        }

        //Rozmieszczenie strażników leśnych
        for (int i = 0; i < params.getNumForestRangers(); i++) {
            agents.add(new ForestRanger(
                    rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()), board,
                    params.getRangerPatrolRange(), params.getFineAmount(), params.getPenaltyCooldown(), agents
            ));
        }
    }

    /**
     * Wykonanie jednego ticku symulacji.
     * Wykonanie akcji agentów, zaktualizowanie ekonomii i środowiska.
     */
    public void step() {
        //Podjęcie akcji przez agenta
        for (Agent agent : agents) {
            agent.findTarget();
        }

        //Pobranie kosztów życia i usunięcie zbankrutowanych drwali
        agents.removeIf(agent -> {
            if (agent instanceof Lumberjack) {
                Lumberjack lumberjack = (Lumberjack) agent;
                lumberjack.payCosts();

                if (lumberjack.checkBankruptcy()) {
                    System.out.println("Bankructwo drwala");
                    return true;
                }
            }
            return false;
        });

        //Odrastanie drzew
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                board.getCell(x, y).grow();
            }
        }

        //Aktualizacja czasu symulacji
        tick++;
    }

    /**
     * @return Aktualny czas symulacji (w tickach).
     */
    public int getTick() {
        return this.tick;
    }

    /**
     * @return Referencja do głównej planszy.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * @return Lista wszystkich aktywnych agentów na planszy.
     */
    public List<Agent> getAgents() {
        return this.agents;
    }

    /**
     * Punkt wejścia programu.
     * Stworzenie silnika, przygotowanie środowiska, przekazanie kontroli do GUI.
     * @param args Argument wiersza poleceń.
     */
    public static void main(String[] args) {
        Simulation sim = new Simulation(100, 50);
        sim.setup();

        javax.swing.SwingUtilities.invokeLater(() -> {
            SimulationUI ui = new SimulationUI(sim);
            ui.start();
        });
    }
}