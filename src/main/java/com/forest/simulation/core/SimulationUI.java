package com.forest.simulation.core;

import com.forest.simulation.agents.Agent;
import com.forest.simulation.agents.EcologicalLumberjack;
import com.forest.simulation.agents.ForestRanger;
import com.forest.simulation.agents.GreedyLumberjack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SimulationUI extends JFrame {
    private Simulation sim;
    private BoardPanel boardPanel;
    private JTextArea statsArea;
    private Timer timer;

    private StatisticsExporter exporter;

    public SimulationUI(Simulation sim) {
        this.sim = sim;

        this.exporter = new StatisticsExporter("statystyki_symulacji.txt");

        setTitle("Symulacja Lasu");
        setSize(1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        statsArea = new JTextArea(7, 50);
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.BOLD, 22));
        statsArea.setBackground(new Color(25, 25, 25));
        statsArea.setForeground(new Color(220, 220, 220));
        statsArea.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(statsArea, BorderLayout.SOUTH);

        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.step();
                boardPanel.repaint();
                updateStats();
            }
        });
    }

    private void updateStats() {
        int ecoCount = 0;
        int greedyCount = 0;
        int ecoCapitalSum = 0;
        int greedyCapitalSum = 0;

        for (Agent agent : sim.getAgents()) {
            if (agent instanceof EcologicalLumberjack) {
                ecoCount++;
                ecoCapitalSum += ((EcologicalLumberjack) agent).getCapital();
            } else if (agent instanceof GreedyLumberjack) {
                greedyCount++;
                greedyCapitalSum += ((GreedyLumberjack) agent).getCapital();
            }
        }

        int ecoAvg = (ecoCount > 0) ? (ecoCapitalSum / ecoCount) : 0;
        int greedyAvg = (greedyCount > 0) ? (greedyCapitalSum / greedyCount) : 0;

        Board board = sim.getBoard();
        int totalCells = board.getWidth() * board.getHeight();
        int treeCount = 0;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (board.getCell(x, y).getState().equals("Tree")) {
                    treeCount++;
                }
            }
        }

        int forestation = (int) (((double) treeCount / totalCells) * 100);

        String text = String.format(
                "ECO DRWALE      Populacja: %d  |  Średni majątek: %d$\n\n" +
                        "CHCIWI DRWALE   Populacja: %d  |  Średni majątek: %d$\n\n" +
                        "POZIOM ZALESIENIA: %d%%\n\n" +
                        "TICK: %d",
                ecoCount, ecoAvg, greedyCount, greedyAvg, forestation, sim.getTick()
        );

        statsArea.setText(text);

        exporter.logStats(sim.getTick(), ecoAvg, greedyAvg, forestation);
    }

    public void start() {
        setVisible(true);
        timer.start();
    }

    private class BoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Board board = sim.getBoard();
            List<Agent> agents = sim.getAgents();

            if (board == null) return;

            int cellWidth = getWidth() / board.getWidth();
            int cellHeight = getHeight() / board.getHeight();

            for (int x = 0; x < board.getWidth(); x++) {
                for (int y = 0; y < board.getHeight(); y++) {
                    Cell cell = board.getCell(x, y);

                    if (cell.getState().equals("Tree")) {
                        g.setColor(new Color(144, 238, 144));
                        g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);

                        g.setColor(new Color(34, 139, 34));
                        g.fillOval(x * cellWidth + cellWidth/6, y * cellHeight + cellHeight/6, (int)(cellWidth/1.5), (int)(cellHeight/1.5));
                    } else {
                        g.setColor(new Color(144, 238, 144));
                        g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    }

                    g.setColor(new Color(0, 100, 0, 40));
                    g.drawRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                }
            }

            for (Agent agent : agents) {
                int ax = agent.getX() * cellWidth;
                int ay = agent.getY() * cellHeight;

                if (agent instanceof ForestRanger) {
                    g.setColor(Color.BLUE);
                    g.fillOval(ax + cellWidth/4, ay + cellHeight/4, cellWidth/2, cellHeight/2);
                } else if (agent instanceof GreedyLumberjack) {
                    g.setColor(Color.RED);
                    g.fillOval(ax + cellWidth/4, ay + cellHeight/4, cellWidth/2, cellHeight/2);
                } else if (agent instanceof EcologicalLumberjack) {
                    g.setColor(Color.YELLOW);
                    g.fillOval(ax + cellWidth/4, ay + cellHeight/4, cellWidth/2, cellHeight/2);
                }
            }
        }
    }
}