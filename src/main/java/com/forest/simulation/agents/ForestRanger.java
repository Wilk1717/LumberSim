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

                    int dx = Math.abs(greedy.getX() - this.x);
                    if (dx > board.getWidth() / 2) dx = board.getWidth() - dx;

                    int dy = Math.abs(greedy.getY() - this.y);
                    if (dy > board.getHeight() / 2) dy = board.getHeight() - dy;

                    int distance = Math.max(dx, dy);

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

                int newDx = Math.abs(lockedTarget.getX() - this.x);
                if (newDx > board.getWidth() / 2) newDx = board.getWidth() - newDx;

                int newDy = Math.abs(lockedTarget.getY() - this.y);
                if (newDy > board.getHeight() / 2) newDy = board.getHeight() - newDy;

                int newDistance = Math.max(newDx, newDy);

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