package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import java.util.List;

/**
 * Klasa strażnika leśnego.
 * Strażnik patroluje mapę i ściga chciwych drwali.
 * Kiedy dogoni swój cel, nakłada na niego karę finansową oraz czasowo blokuje możliwość ścinania drzew.
 */
public class ForestRanger extends Agent {
    private int patrolRange;
    private int fineAmount;
    private int penaltyCooldown;
    private List<Agent> allAgents;
    private GreedyLumberjack lockedTarget;

    /**
     * Inicjalizacja parametrów strażnika leśnego.
     * @param startX Początkowa współrzędna X.
     * @param startY Początkowa współrzędna Y.
     * @param board Referencja do planszy symulacji.
     * @param patrolRange Zasięg, w którym strażnik jest w stanie wykryć chciwego drwala.
     * @param fineAmount Kwota mandatu (odejmowana od kapitału złapanego drwala).
     * @param penaltyCooldown Czas trwania kary (liczba ticków), na jaki drwal zostaje zamrożony.
     * @param allAgents Lista wszystkich agentów na planszy, niezbędna do wyszukiwania celów.
     */
    public ForestRanger(int startX, int startY, Board board, int patrolRange, int fineAmount, int penaltyCooldown, List<Agent> allAgents) {
        super(startX, startY, board);
        this.patrolRange = patrolRange;
        this.fineAmount = fineAmount;
        this.penaltyCooldown = penaltyCooldown;
        this.allAgents = allAgents;
        this.lockedTarget = null;
    }

    /**
     * Mechanika patrolowania, ścigania i karania.
     * W pierwszej kolejności strażnik weryfikuje swój obecny cel. Jeśli zgubił cel lub
     * cel odbywa już karę, szuka najbliższego chciwego drwala w swoim promieniu patrolu.
     * Strażnik porusza się szybciej niż inni agenci (do dwóch pól w jednej turze).
     * Gdy znajdzie się w bezpośrednim sąsiedztwie celu, wystawia mandat i nakłada karę czasową.
     */
    @Override
    public void findTarget() {

        // Resetowanie celu, jeśli zniknął z mapy (zbankrutował) lub już odbywa karę.
        if (lockedTarget != null && (!allAgents.contains(lockedTarget) || lockedTarget.isOnCooldown())) {
            lockedTarget = null;
        }

        // Poszukiwanie nowego, najbliższego chciwego drwala w promieniu patrolu.
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

        // Ruch w stronę namierzonego celu.
        if (lockedTarget != null) {
            // Strażnik wykonuje 2 ruchy na tick (jest szybszy, żeby dogonić drwala).
            for (int step = 0; step < 2; step++) {
                Cell targetCell = board.getCell(lockedTarget.getX(), lockedTarget.getY());
                moveToTarget(targetCell);

                int newDistance = board.calculateDistance(this.x, this.y, lockedTarget.getX(), lockedTarget.getY());

                // Jeśli strażnik jest tuż obok celu, wlepia mandat.
                if (newDistance <= 1) {
                    lockedTarget.capital -= this.fineAmount;
                    lockedTarget.setCooldown(this.penaltyCooldown);
                    System.out.println("Mandat");
                    lockedTarget = null;
                    break;
                }
            }
        } else {
            // Jeśli nie ma celu w zasięgu, wykonuje zwykły, losowy patrol.
            moveRandomly();
        }
    }
}