package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableRowTest {
    Text t1 = new Text("Text1");
    Text t2 = new Text("Text2");
    TableRow row = new TableRow(List.of(t1, t2));

    @Test
    void getElements() {
        assertEquals(List.of(t1, t2), row.getElements());
    }

    @Test
    void setElements() {
        Link l1 = new Link("Link1");
        row.setElements(List.of(t1, l1, t2));
        assertEquals(List.of(t1, l1, t2), row.getElements());
    }

    @Test
    void setElementsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            row.setElements(null);
        });
    }

    @Test
    void getHtmlStringTest(){
        assertEquals("<tr><td>Text1<td>Text2", row.getHtmlString());
    }
}