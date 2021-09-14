package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {
    Text t1 = new Text("Text1");
    Text t2 = new Text("Text2");
    TableRow row1 = new TableRow(List.of(t1, t2));
    Link l1 = new Link("Link1");
    Link l2 = new Link("Link2");
    TableRow row2 = new TableRow(List.of(t1, t2));
    Table table = new Table(List.of(row1, row2));

    @Test
    void getRows() {
        assertEquals(List.of(row1, row2),table.getRows());
    }

    @Test
    void setRows() {
        Text t3 = new Text("Text3");
        TableRow row3 = new TableRow(List.of(t3));
        table.setRows(List.of(row1, row2, row3));

        assertEquals(List.of(row1, row2, row3), table.getRows());
    }

    @Test
    void setRowsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            table.setRows(null);
        });
    }

    @Test
    void getHtmlStringTest(){
        assertEquals("<table><tr><td>Text1<td>Text2<tr><td>Text1<td>Text2</table>", table.getHtmlString());
    }
}