package com.forest.simulation.core;

import com.forest.simulation.core.Cell;

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

    public List<Cell> getNeighbors(int x, int y, int radius) {                          //TODO: napisać ta funkcję
        return null;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}