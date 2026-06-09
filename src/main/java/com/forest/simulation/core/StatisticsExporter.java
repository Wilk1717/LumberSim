package com.forest.simulation.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsExporter {
    private String fileName;

    public StatisticsExporter(String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("Tick\tSredni Majatek Eco\tSredni Majatek Chciwy\tZalesienie (%)\n");
        } catch (IOException e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }

    public void logStats(int tick, int ecoAvg, int greedyAvg, int forestation) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(tick + "\t" + ecoAvg + "\t\t" + greedyAvg + "\t\t" + forestation + "%\n");
        } catch (IOException e) {
            System.out.println("Blad: " + e.getMessage());
        }
    }
}