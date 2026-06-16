package com.forest.simulation.agents;

import com.forest.simulation.core.Board;
import com.forest.simulation.core.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LumberjackTest {

    @Test
    public void testBankruptcyMechanic() {
        //Tworzymy planszę i drwala z początkowym kapitałem 10 i kosztem życia 6
        Board board = new Board(10, 10);
        EcologicalLumberjack lumberjack = new EcologicalLumberjack(0, 0, board, 3, 10, 5, 10, 6, 0);

        // Na początku drwal ma 10, więc nie jest bankrutem
        assertFalse(lumberjack.checkBankruptcy(), "Drwal z dodatnim kapitałem nie powinien być bankrutem");

        // Drwal płaci za życie (10 - 6 = 4)
        lumberjack.payCosts();

        //  Zostało mu 4, nadal gra
        assertFalse(lumberjack.checkBankruptcy(), "Drwal po pierwszej opłacie ma 4$, nadal nie jest bankrutem");

        // Drwal znowu płaci za życie (4 - 6 = -2)
        lumberjack.payCosts();

        // Kapitał spadł poniżej zera, powinien być bankrutem
        assertTrue(lumberjack.checkBankruptcy(), "Drwal z ujemnym kapitałem musi zostać uznany za bankruta");
    }
    @Test
    public void testHarvestingTreeIncreasesCapital() {
        // Tworzymy plansze
        Board board = new Board(10, 10);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                board.setCell(x, y, new Cell(x, y, "Empty", 0));
            }
        }
        // Drwala z kapitałem 10 i drzewo warte 15
        EcologicalLumberjack lumberjack = new EcologicalLumberjack(0, 0, board, 3, 10, 5, 15, 2, 0);
        Cell treeCell = new Cell(0, 0, "Tree", 0);

        // Drwal ścina drzewo
        lumberjack.harvest(treeCell);

        // Kapitał drwala powinien wzrosnąć o 15 (z 10 na 25)
        assertEquals(25, lumberjack.getCapital(), "Po ścięciu drzewa kapitał powinien wzrosnąć o jego wartość");

        // Upewniamy się, że przy okazji samo drzewo zniknęło
        assertEquals("Empty", treeCell.getState(), "Drzewo po ścięciu powinno zmienić stan na 'Empty'");
    }
}