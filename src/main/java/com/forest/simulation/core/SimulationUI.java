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
    private Timer timer;

    public SimulationUI(Simulation sim) {
        this.sim = sim;

        setTitle("Symulacja Lasu");
        setSize(1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boardPanel = new BoardPanel();
        add(boardPanel);

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.step();
                boardPanel.repaint();
            }
        });
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