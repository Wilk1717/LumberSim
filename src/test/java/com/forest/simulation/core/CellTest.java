package com.forest.simulation.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    public void testTreeChopDown() {
        //  Tworzymy komórkę z drzewem
        Cell cell = new Cell(0, 0, "Tree", 0);

        // Ścinamy drzewo z czasem odrastania równym 5 ticków
        cell.chopDown(5);

        // Czy stan zmienił się na "Empty"?
        assertEquals("Empty", cell.getState());
    }

    @Test
    public void testTreeRegrowthMechanics() {
        // Tworzymy komórkę, która właśnie została ścięta (Empty)
        Cell cell = new Cell(1, 1, "Empty", 2);

        // Pierwszy tick odrastania
        cell.grow();
        // Drzewo jeszcze nie powinno odrosnąć
        assertEquals("Empty", cell.getState());

        // Drugi tick odrastania (licznik spada do 0)
        cell.grow();
        // Drzewo powinno wrócić do stanu "Tree"
        assertEquals("Tree", cell.getState());
    }
}