package ui.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RangeTest {
    Range range = new Range(0,0);

    @Test
    void getRange() {
        assertEquals(0, range.getLeft());
        assertEquals(0, range.getRight());
    }
    @Test
    void setRange() {
        range.setRight(1);
        assertEquals(1, range.getRight());
        range.setLeft(1);
        assertEquals(1, range.getLeft());
    }

}
