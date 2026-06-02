package com.forest.simulation.core;

public class Cell {
    //Parametry komórki
    private int x;
    private int y;
    private String state;
    private int regrowthCountdown;

    //Konstruktor komórki
    public Cell(int x, int y, String state, int regrowthCountdown) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.regrowthCountdown = regrowthCountdown;
    }

    //Zmiana stanu komórki po ścięciu drzewa
    public void chopDown(int regrowthTime) {
        this.state = "Empty";
        this.regrowthCountdown = regrowthTime;
    }

    //Sprawdzenie obecności dorosłego drzewa w sąsiedztwie komórki
    public boolean hasAdultTreeNeighbor(Board board) {
        return false;                                                                                                   //TODO: napisać tą metodę
    }

    //Licznik odrastania ściętego drzewa
    public void grow() {
        if (this.regrowthCountdown > 0) {
            this.regrowthCountdown--;

            if (this.regrowthCountdown == 0) {
                this.state = "Tree";
            }
        }
    }

    //Gettery
    public int getX() { return x; }
    public int getY() { return y; }
    public String getState() { return state; }
    public int getRegrowthCountdown() { return regrowthCountdown; }
}

