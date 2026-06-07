package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

public class ForestRanger extends Agent {
    private int patrolRange;
    private int fineAmount;
    private int penaltyCooldown;
    private List<Agent> allAgents;
    private GreedyLumberjack lockedTarget;

    public ForestRanger(int startX, int startY, Board board, int patrolRange, int fineAmount, int penaltyCooldown, List<Agent> allAgents) {
        super(startX, startY, board);
        this.patrolRange = patrolRange;
        this.fineAmount = fineAmount;
        this.penaltyCooldown = penaltyCooldown;
        this.allAgents = allAgents;
        this.lockedTarget = null;
    }

    @Override
    public void findTarget() {
        if (lockedTarget != null && (!allAgents.contains(lockedTarget) || lockedTarget.isOnCooldown())) {
            lockedTarget = null;
        }

        if (lockedTarget == null) {
            int minDistance = Integer.MAX_VALUE;

            for (Agent agent : allAgents) {
                if (agent instanceof GreedyLumberjack) {
                    GreedyLumberjack greedy = (GreedyLumberjack) agent;

                    if (greedy.isOnCooldown()) continue;

                    int distance = board.calculateDistance(this.x, this.y, greedy.getX(), greedy.getY());

                    if (distance <= this.patrolRange && distance < minDistance) {
                        minDistance = distance;
                        lockedTarget = greedy;
                    }
                }
            }
        }

        if (lockedTarget != null) {
            for (int step = 0; step < 2; step++) {
                Cell targetCell = board.getCell(lockedTarget.getX(), lockedTarget.getY());
                moveToTarget(targetCell);

                int newDistance = board.calculateDistance(this.x, this.y, lockedTarget.getX(), lockedTarget.getY());

                if (newDistance <= 1) {
                    lockedTarget.capital -= this.fineAmount;
                    lockedTarget.setCooldown(this.penaltyCooldown);
                    System.out.println("Mandat");
                    lockedTarget = null;
                    break;
                }
            }
        } else {
            moveRandomly();
        }
    }
}