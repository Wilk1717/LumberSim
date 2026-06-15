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

/**
 * Klasa z interfejsem graficznym (GUI) symulacji.
 * Wykorzystuje bibliotekę Swing do wyświetlania okna z planszą
 * oraz panelem statystyk. Zarządza również główną pętlą czasową (Timer),
 * która napędza całą symulację.
 */
public class SimulationUI extends JFrame {
    private Simulation sim;
    private BoardPanel boardPanel;
    private JTextArea statsArea;
    private Timer timer;

    private StatisticsExporter exporter;

    /**
     * Inicjalizacja głównego okna symulacji.
     * Ustawia parametry okna (rozmiar, układ), tworzy panel rysowania planszy
     * i pole tekstowe ze statystykami. Konfiguruje również zegar (Timer)
     * odświeżający stan symulacji co określoną liczbę milisekund.
     * @param sim Referencja do głównego obiektu symulacji zawierającego logikę.
     */
    public SimulationUI(Simulation sim) {
        this.sim = sim;

        // Inicjalizacja eksportera zapisującego dane w tle.
        this.exporter = new StatisticsExporter("statystyki_symulacji.txt");

        // Konfiguracja głównego okna aplikacji.
        setTitle("Symulacja Lasu");
        setSize(1920, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Okno na pełnym ekranie
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Dodanie centralnego panelu, na którym rysowana będzie plansza.
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Konfiguracja dolnego panelu tekstowego ze statystykami na żywo.
        statsArea = new JTextArea(7, 50);
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.BOLD, 22));
        statsArea.setBackground(new Color(25, 25, 25));
        statsArea.setForeground(new Color(220, 220, 220));
        statsArea.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(statsArea, BorderLayout.SOUTH);

        // Główna pętla napędzająca symulację .
        timer = new Timer(250, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sim.step();             // Przeliczenie logiki wszystkich agentów i planszy
                boardPanel.repaint();   // Wymuszenie ponownego narysowania grafiki
                updateStats();          // Odświeżenie panelu tekstowego u dołu ekranu
            }
        });
    }

    /**
     * Oblicza i aktualizuje statystyki wyświetlane na dolnym panelu ekranu.
     * Zbiera dane o populacji drwali, ich średnim majątku oraz procencie zalesienia.
     * Na koniec formatuje tekst, wyświetla go i przekazuje do eksportera plikowego.
     */
    private void updateStats() {
        int ecoCount = 0;
        int greedyCount = 0;
        int ecoCapitalSum = 0;
        int greedyCapitalSum = 0;

        // Zbieranie danych o żyjących agentach.
        for (Agent agent : sim.getAgents()) {
            if (agent instanceof EcologicalLumberjack) {
                ecoCount++;
                ecoCapitalSum += ((EcologicalLumberjack) agent).getCapital();
            } else if (agent instanceof GreedyLumberjack) {
                greedyCount++;
                greedyCapitalSum += ((GreedyLumberjack) agent).getCapital();
            }
        }

        // Obliczanie średniego majątku (zabezpieczenie przed dzieleniem przez zero).
        int ecoAvg = (ecoCount > 0) ? (ecoCapitalSum / ecoCount) : 0;
        int greedyAvg = (greedyCount > 0) ? (greedyCapitalSum / greedyCount) : 0;

        Board board = sim.getBoard();
        int totalCells = board.getWidth() * board.getHeight();
        int treeCount = 0;

        // Zliczanie wyrośniętych drzew do wskaźnika zalesienia.
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (board.getCell(x, y).getState().equals("Tree")) {
                    treeCount++;
                }
            }
        }

        // Obliczenie procentu zalesienia mapy.
        int forestation = (int) (((double) treeCount / totalCells) * 100);

        // Zbudowanie gotowego stringa do wyświetlenia na ekranie.
        String text = String.format(
                "ECO DRWALE      Populacja: %d  |  Średni majątek: %d$\n\n" +
                        "CHCIWI DRWALE   Populacja: %d  |  Średni majątek: %d$\n\n" +
                        "POZIOM ZALESIENIA: %d%%\n\n" +
                        "TICK: %d",
                ecoCount, ecoAvg, greedyCount, greedyAvg, forestation, sim.getTick()
        );

        statsArea.setText(text);

        // Zapis danych do pliku w tle.
        exporter.logStats(sim.getTick(), ecoAvg, greedyAvg, forestation);
    }

    /**
     * Pokazuje główne okno programu i uruchamia zegar symulacji.
     */
    public void start() {
        setVisible(true);
        timer.start();
    }

    /**
     * Wewnętrzna klasa pełniąca rolę płótna do rysowania.
     * Odpowiada za renderowanie siatki planszy, komórek z drzewami oraz agentów.
     */
    private class BoardPanel extends JPanel {

        /**
         * Główna metoda rysująca biblioteki Swing. Wywoływana automatycznie
         * przy każdym odświeżeniu (repaint) panelu.
         * @param g Obiekt graficzny służący do rysowania kształtów i kolorów.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Board board = sim.getBoard();
            List<Agent> agents = sim.getAgents();

            if (board == null) return;

            // Dynamiczne obliczanie wielkości komórek, aby dopasować je do rozmiaru okna
            int cellWidth = getWidth() / board.getWidth();
            int cellHeight = getHeight() / board.getHeight();

            // Rysowanie planszy (tło i drzewa)
            for (int x = 0; x < board.getWidth(); x++) {
                for (int y = 0; y < board.getHeight(); y++) {
                    Cell cell = board.getCell(x, y);

                    if (cell.getState().equals("Tree")) {
                        // Tło dla pola z drzewem
                        g.setColor(new Color(144, 238, 144));
                        g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);

                        // Rysowanie korony drzewa
                        g.setColor(new Color(34, 139, 34));
                        g.fillOval(x * cellWidth + cellWidth/6, y * cellHeight + cellHeight/6, (int)(cellWidth/1.5), (int)(cellHeight/1.5));
                    } else {
                        // Tło dla pustego pola
                        g.setColor(new Color(144, 238, 144));
                        g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                    }

                    // siatka oddzielająca komórki
                    g.setColor(new Color(0, 100, 0, 40));
                    g.drawRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                }
            }

            // Rysowanie agentów na wierzchu planszy
            for (Agent agent : agents) {
                int ax = agent.getX() * cellWidth;
                int ay = agent.getY() * cellHeight;

                if (agent instanceof ForestRanger) {
                    g.setColor(Color.BLUE); // Strażnik leśny - Niebieski
                    g.fillOval(ax + cellWidth/4, ay + cellHeight/4, cellWidth/2, cellHeight/2);
                } else if (agent instanceof GreedyLumberjack) {
                    g.setColor(Color.RED); // Chciwy drwal - Czerwony
                    g.fillOval(ax + cellWidth/4, ay + cellHeight/4, cellWidth/2, cellHeight/2);
                } else if (agent instanceof EcologicalLumberjack) {
                    g.setColor(Color.YELLOW); // Ekologiczny drwal - Żółty
                    g.fillOval(ax + cellWidth/4, ay + cellHeight/4, cellWidth/2, cellHeight/2);
                }
            }
        }
    }
}