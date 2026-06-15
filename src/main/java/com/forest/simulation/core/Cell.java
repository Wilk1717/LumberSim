package com.forest.simulation.core;

/**
 * Pojedyncza komórka na planszy symulacji.
 * Komórka przechowuje swoje współrzędne oraz aktualny stan środowiskowy.
 */

public class Cell {
    private int x;
    private int y;
    private String state;
    private int regrowthCountdown;

    /**
     * Stworzenie nowego obiektu komórki o podanych parametrach.
     * @param x Współrzędna X
     * @param y Współrzędna Y
     * @param state Stan komórki ("Tree" lub "Empty").
     * @param regrowthCountdown Wartość licznika odrastania.
     */
    public Cell(int x, int y, String state, int regrowthCountdown) {
        this.x = x;
        this.y = y;
        this.state = state;
        this.regrowthCountdown = regrowthCountdown;
    }

    /**
     * Zmiana stanu komórki na pustą (ścięcie drzewa) i rozpoczęcie odliczanie do odrośnięcia.
     * @param regrowthTime Czas potrzebny do ponownego odrośnięcia drzewa.
     */
    public void chopDown(int regrowthTime) {
        this.state = "Empty";
        this.regrowthCountdown = regrowthTime;
    }

    /**
     * Mechanika odrastania drzewa.
     * Jeśli na polu znajdował się pniak (licznik > 0), metoda zmniejsza wartość licznika.
     * Gdy licznik osiągnie wartość zero, komórka zmienia stan z powrotem na "Tree".
     */
    public void grow() {
        if (this.regrowthCountdown > 0) {
            this.regrowthCountdown--;

            if (this.regrowthCountdown == 0) {
                this.state = "Tree";
            }
        }
    }

    /**
     * Zmiana stanu komórki.
     * @param state Nowy stan do ustawienia ("Tree" lub "Empty").
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return Współrzędna X komórki.
     */
    public int getX() { return x; }

    /**
     * @return Współrzędna Y komórki.
     */
    public int getY() { return y; }

    /**
     * @return Obecny stan środowiskowy komórki ("Tree" lub "Empty").
     */
    public String getState() { return state; }
}