package ui.component;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableRowComponentTest {
    TextComponent t1 = new TextComponent(0, 0, 5, 5, "Text 1");
    TextComponent t2 = new TextComponent(5, 0, 5, 6, "Text 2");
    TextComponent t3 = new TextComponent(10, 0, 5, 7, "Text 3");
    TableRowComponent row = new TableRowComponent(0, 0, List.of(t1, t2, t3));

    @Test
    void getCells() {
        assertEquals(List.of(t1, t2, t3), row.getCells());
    }

    @Test
    void setCells() {
        LinkComponent l = new LinkComponent(0, 0, 5, 5, "test.html");
        this.row.setCells(List.of(l));
        assertEquals(List.of(l), this.row.getCells());
    }

    @Test
    void setCellsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.row.setCells(null);
        });
    }

    @Test
    void setWidthByCells() {
        this.row.setWidthByCells();
        assertEquals(15, this.row.getWidth());
    }

    @Test
    void setHeightByCells() {
        this.row.setHeightByCells();
        assertEquals(7, this.row.getHeight());
    }

    @Test
    void setXCoordinatesOfCells() {
        LinkComponent l1 = new LinkComponent(0, 0, 5, 5, "test.html");
        LinkComponent l2 = new LinkComponent(0, 0, 5, 5, "test2.html");
        TableRowComponent row2 = new TableRowComponent(0, 0, 10, 5, List.of(l1, l2));
        row2.setXCoordinatesOfCells();
        assertEquals(0, l1.getX());
        assertEquals(5, l2.getX());
    }

    @Test
    void handleMouseEvent(){
        this.row.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        this.row.handleKeyEvent(401, 8, '\u0000', 0);
    }

    @Test
    void VerticalScrollEventTest() {
        row.setY(10);
        int y_before = row.getY();
        row.handleVerticalScroll(-1, 15, 100);
        assertEquals(15, row.getLowerLimitY());
        assertEquals(100, row.getUpperLimitY());
        int y_after = row.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        row.setX(10);
        int x_before = row.getX();
        row.handleHorizontalScroll(1, 0, 15);
        assertEquals(0, row.getLowerLimitX());
        assertEquals(15, row.getUpperLimitX());
        int x_after = row.getX();
        assertEquals(x_before - 1, x_after);
    }
}