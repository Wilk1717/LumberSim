package com.forest.simulation.core;

import com.forest.simulation.agents.Agent;
import com.forest.simulation.agents.EcologicalLumberjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    //Parametry symulacji
    private int forestDensity;
    private int regrowthTime;
    private int fineAmount;
    private int initialCapital;
    private int livingCostParameter;
    private int cooldownParameter;
    private int lumberjackVisionParameter;
    private int rangerPatrolParameter;

    private Board board;
    private List<Agent> agents;
    private int tick;

    //Konstruktor symulacji
    public Simulation(int width, int height) {
        this.board = new Board(width, height);
        this.agents = new ArrayList<>();
        this.tick = 0;

        this.forestDensity = 50;
        this.regrowthTime = 6;
    }

    //Rozpoczęcie symulacji
    public void setup() {
        Random rand = new Random();

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                String initialState = rand.nextInt(100) < this.forestDensity ? "Tree" : "Empty";

                Cell cell = new Cell(x, y, initialState, 0);
                board.setCell(x, y, cell);
            }
        }

        int startX = board.getWidth() / 2;
        int startY = board.getHeight() / 2;
        agents.add(new EcologicalLumberjack(startX, startY, board, 3, 0, this.regrowthTime, 0));
    }

    //Wykonanie jednego ticku symulacji
    public void step() {
        for (Agent agent : agents) {
            agent.findTarget();
        }

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

                boolean isAgentHere = false;
                for (Agent agent : agents) {
                    if (agent.getX() == x && agent.getY() == y) {
                        isAgentHere = true;
                        break;
                    }
                }

                if (isAgentHere) {
                    System.out.print("@ ");
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

    //Main
    public static void main(String[] args) {
        Simulation sim = new Simulation(15, 15);
        sim.setup();
        sim.printBoard();

        for(int i = 0; i < 100; i++) {
            sim.step();
            sim.printBoard();

            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                System.out.println("Symulacja przerwana ");
            }
        }
    }
}