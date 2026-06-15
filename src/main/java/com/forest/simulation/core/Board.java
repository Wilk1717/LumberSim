package com.forest.simulation.core;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa głównej planszy symulacji.
 * Plansza ma charakter torusowy, agenci przekraczając krawędź planszy pojawiają się po przeciwej stronie.
*/

public class Board {
    private int width;
    private int height;
    private Cell[][] grid;

    /**
     * Stworzenie planszy o określonych wymiarach.
     * @param width Szerokość planszy.
     * @param height Wysokość planszy.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
    }

    /**
     * Zabezpieczenie współrzędnych przed wyjściem poza tablicę (logika torus).
     * @param value Oś do sprawdzenia.
     * @param max Maksymalny wymiar dla danej osi.
     * @return Skorygowana współrzędna mieszcząca się w granicach tablicy.
     */
    public int wrapCoordinate(int value, int max) {
        return ((value % max) + max) % max;
    }

    /**
     * Pobiera komórkę z podanych współrzędnych.
     * @param x Współrzędna X
     * @param y Współrzędna Y
     * @return Obiekt komórki znajdujący się na podanym polu.
     */
    public Cell getCell(int x, int y) {
        int wrappedX = wrapCoordinate(x, width);
        int wrappedY = wrapCoordinate(y, height);
        return grid[wrappedX][wrappedY];
    }

    /**
     * Zwraca listę wszystkich komórek w kwadratowym obszarze wokół podanego punktu.
     * @param x Współrzędna X centrum obszaru.
     * @param y Współrzędna Y centrum obszaru.
     * @param range Zasięg promienia.
     * @return Lista komórek w zasięgu.
     */
    public List<Cell> getNeighbors(int x, int y, int range) {
        List<Cell> visibleCells = new ArrayList<>();

        for (int i = x - range; i <= x + range; i++) {
            for (int j = y - range; j <= y + range; j++) {

                int wrappedX = wrapCoordinate(i, width);
                int wrappedY = wrapCoordinate(j, height);

                visibleCells.add(grid[wrappedX][wrappedY]);
            }
        }
        return visibleCells;
    }

    /**
     * Nadpisanie pola na mapie.
     * @param x Współrzędna X
     * @param y Współrzędna Y
     * @param cell Nowy obiekt komórki do wstawienia.
     */
    public void setCell(int x, int y, Cell cell) {
        int wrappedX = wrapCoordinate(x, width);
        int wrappedY = wrapCoordinate(y, height);
        grid[wrappedX][wrappedY] = cell;
    }

    /**
     * Sprawdzenie czy w bezpośrednim sąsiedztwie znajduje się w pełni wyrośnięte drzewo.
     * @param x Współrzędna X
     * @param y Współrzędna Y
     * @return True, jeśli w pobliżu jest co najmniej jedno dorosłe drzewo. False w przeciwnym wypadku.
     */
    public boolean hasAdultTreeNeighbor(int x, int y) {
        List<Cell> neighbors = getNeighbors(x, y, 1);
        for (Cell neighbor : neighbors) {
            if (neighbor.getState().equals("Tree")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Oblicza najkrótszy dystans między dwoma punktami.
     * @param x1 Współrzędna X pierwszego punktu.
     * @param y1 Współrzędna Y pierwszego punktu.
     * @param x2 Współrzędna X drugiego punktu.
     * @param y2 Współrzędna Y drugiego punktu.
     * @return Najkrótsza liczba kroków potrzebna do dotarcia do celu.
     */
    public int calculateDistance(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        if (dx > this.width / 2) dx = this.width - dx;

        int dy = Math.abs(y2 - y1);
        if (dy > this.height / 2) dy = this.height - dy;

        return Math.max(dx, dy);
    }

    /**
     * @return Szerokość planszy.
     */
    public int getWidth() { return width; }

    /**
     * @return Wysokość planszy.
     */
    public int getHeight() { return height; }
}