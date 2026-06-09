package com.forest.simulation.core;

import com.forest.simulation.agents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    private Board board;
    private List<Agent> agents;
    private int tick;
    private SimulationParameters params;

    //Konstruktor symulacji
    public Simulation(int width, int height, SimulationParameters params) {
        this.board = new Board(width, height);
        this.agents = new ArrayList<>();
        this.tick = 0;
        this.params = params;
    }

    //Rozpoczęcie symulacji
    public void setup() {
        Random rand = new Random();

        //Las
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                String initialState = rand.nextInt(100) < params.getForestDensity() ? "Tree" : "Empty";

                Cell cell = new Cell(x, y, initialState, 0);
                board.setCell(x, y, cell);
            }
        }

        //Ekologiczny drwal
        for (int i = 0; i < params.getNumEcologicalLumberjacks(); i++) {
            agents.add(new EcologicalLumberjack(
                    rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()), board,
                    params.getAgentVisionRange(), params.getInitialCapital(),
                    params.getRegrowthTime(), params.getTreeValue(), params.getLivingCost(), 0
            ));
        }

        //Chciwy drwal
        for (int i = 0; i < params.getNumGreedyLumberjacks(); i++) {
            agents.add(new GreedyLumberjack(
                    rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()), board,
                    params.getAgentVisionRange(), params.getInitialCapital(),
                    params.getRegrowthTime(), params.getTreeValue(), params.getLivingCost(),
                    params.getGreedyCuttingRange()
            ));
        }

        //Strażnik
        for (int i = 0; i < params.getNumForestRangers(); i++) {
            agents.add(new ForestRanger(
                    rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()), board,
                    params.getRangerPatrolRange(), params.getFineAmount(), params.getPenaltyCooldown(), agents
            ));
        }
    }

    //Wykonanie jednego ticku symulacji
    public void step() {
        for (Agent agent : agents) {
            agent.findTarget();
        }

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

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                board.getCell(x, y).grow();
            }
        }
        tick++;
    }

    //Testowy wydruk planszy w konsoli
    public void printBoard() {
        System.out.println("Tick " + tick);

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {

                Agent foundAgent = null;
                for (Agent agent : agents) {
                    if (agent.getX() == x && agent.getY() == y) {
                        foundAgent = agent;
                        break;
                    }
                }

                if (foundAgent instanceof ForestRanger) {
                    System.out.print("# ");
                } else if (foundAgent instanceof GreedyLumberjack) {
                    System.out.print("@ ");
                } else if (foundAgent instanceof EcologicalLumberjack){
                    System.out.print("$ ");
                } else {
                    Cell cell = board.getCell(x, y);
                    if (cell.getState().equals("Tree")) {
                        System.out.print("T ");
                    } else {
                        System.out.print(". ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    public Board getBoard() {
        return this.board;
    }

    public List<Agent> getAgents() {
        return this.agents;
    }


    public static void main(String[] args) {
        SimulationParameters defaultParams = new SimulationParameters();

        Simulation sim = new Simulation(96, 54, defaultParams);
        sim.setup();

        javax.swing.SwingUtilities.invokeLater(() -> {
            SimulationUI ui = new SimulationUI(sim);
            ui.start();
        });
    }
}