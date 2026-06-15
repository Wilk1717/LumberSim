package com.forest.simulation.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Klasa odpowiadająca za eksportowanie statystyk symulacji do pliku tekstowego.
 */
public class StatisticsExporter {
    private String fileName;

    /**
     * Inicjalizacja eksportera statystyk.
     * Przygotowuje plik do zapisu. Jeśli plik o podanej nazwie z poprzedniej
     * symulacji już istnieje, zostaje usunięty, aby zrobić miejsce na nowe dane.
     * Na samej górze pliku tworzone są nagłówki kolumn.
     * @param fileName Nazwa pliku docelowego (np. "statystyki_symulacji.txt").
     */
    public StatisticsExporter(String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);

        // Usunięcie starego pliku, aby każda nowa symulacja zaczynała z czystą kartą.
        if (file.exists()) {
            file.delete();
        }

        // Otwarcie pliku w trybie nadpisywania (false) i zapisanie nagłówków.
        try (FileWriter writer = new FileWriter(file, false)) {
            // Używamy tabulatorów (\t) do oddzielenia kolumn, co ułatwia czytanie pliku w notatniku.
            writer.write("Tick\tSredni Majatek Eco\tSredni Majatek Chciwy\tZalesienie (%)\n");
        } catch (IOException e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    /**
     * Zapisuje bieżące statystyki z pojedynczej tury (ticku) do pliku.
     * Metoda dopisuje nowy wiersz z danymi na samym końcu dokumentu.
     * @param tick Aktualna tura (czas) symulacji.
     * @param ecoAvg Średni kapitał wszystkich drwali ekologicznych.
     * @param greedyAvg Średni kapitał wszystkich chciwych drwali.
     * @param forestation Aktualny procent zalesienia planszy.
     */
    public void logStats(int tick, int ecoAvg, int greedyAvg, int forestation) {

        // Otwarcie pliku w trybie dopisywania (true), aby nie skasować poprzednich tur.
        try (FileWriter writer = new FileWriter(fileName, true)) {
            // Sklejenie danych w jeden wiersz oddzielony tabulatorami i dodanie znaku nowej linii (\n).
            writer.write(tick + "\t" + ecoAvg + "\t" + greedyAvg + "\t" + forestation + "%\n");
        } catch (IOException e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }
}