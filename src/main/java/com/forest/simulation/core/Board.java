package com.forest.simulation.core;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int width;
    private int height;
    private Cell[][] grid;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
    }

    public int wrapCoordinate(int value, int max) {
        return ((value % max) + max) % max;
    }

    public Cell getCell(int x, int y) {
        int wrappedX = wrapCoordinate(x, width);
        int wrappedY = wrapCoordinate(y, height);
        return grid[wrappedX][wrappedY];
    }

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

    public void setCell(int x, int y, Cell cell) {
        int wrappedX = wrapCoordinate(x, width);
        int wrappedY = wrapCoordinate(y, height);
        grid[wrappedX][wrappedY] = cell;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}