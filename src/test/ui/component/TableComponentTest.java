package ui.component;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TableComponentTest {
    TextComponent t1 = new TextComponent(0, 0, 6, 5, "Text 1");
    TextComponent t2 = new TextComponent(5, 0, 5, 5, "Text 2");
    TableRowComponent row1 = new TableRowComponent(0, 0, List.of(t1, t2));

    LinkComponent l1 = new LinkComponent(0, 6, 5, 5, "link.html");
    LinkComponent l2 = new LinkComponent(5, 6, 6, 5, "link.html");
    TableRowComponent row2 = new TableRowComponent(0, 0, List.of(l1, l2));
    TableComponent table = new TableComponent(0, 0, List.of(row1, row2));

    @Test
    void getRows() {
        assertEquals(List.of(row1, row2), table.getRows());
    }

    @Test
    void setRows() {
        TextComponent t_ = new TextComponent(0, 0, 5, 5, "Text 1");
        TableRowComponent row1 = new TableRowComponent(0, 0, List.of(t_));
        LinkComponent l_ = new LinkComponent(0, 0, 5, 5, "Link 1");
        TableRowComponent row2 = new TableRowComponent(0, 0, List.of(l_));
        this.table.setRows(List.of(row1, row2));
        assertEquals(List.of(row1, row2), table.getRows());
    }

    @Test
    void setRowsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.table.setRows(null);
        });
    }

    @Test
    void setColumnWidths() {
        this.table.setColumnWidths();
        assertEquals(6, t2.getX());
        assertEquals(6, t2.getWidth());

        assertEquals(6, l2.getX());
        assertEquals(6, l2.getWidth());
    }

    @Test
    void setWidthByRows() {
        this.table.setWidthByRows();
        assertEquals(12, this.table.getWidth());
    }

    @Test
    void setHeightByRows() {
        this.table.setHeightByRows();
        assertEquals(10, this.table.getHeight());
    }

    @Test
    void handleMouseEvent(){
        this.table.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        this.table.handleKeyEvent(401, 8, '\u0000', 0);
    }

    @Test
    void VerticalScrollEventTest() {
        table.setY(10);
        int y_before = table.getY();
        table.handleVerticalScroll(-1, 15, 100);
        assertEquals(15, table.getLowerLimitY());
        assertEquals(100, table.getUpperLimitY());
        int y_after = table.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        table.setX(10);
        int x_before = table.getX();
        table.handleHorizontalScroll(1, 0, 15);
        assertEquals(0, table.getLowerLimitX());
        assertEquals(15, table.getUpperLimitX());
        int x_after = table.getX();
        assertEquals(x_before - 1, x_after);
    }

}