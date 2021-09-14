package ui.util;

import domain.*;
import org.junit.jupiter.api.Test;
import ui.component.*;
import ui.component.Component;
import ui.component.TextComponent;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ToComponentUtilTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas cv = new Canvas();
    FontMetrics fm = cv.getFontMetrics(font);

    @Test
    void toComponentLinkComponent() {
        Link link = new Link("test.html");
        Component c = ToComponentUtil.toComponent(0, 0, link, fm);
        assertTrue(c instanceof LinkComponent);
        assertEquals(link.getHref(), ((LinkComponent)c).getHref());
    }

    @Test
    void textWidthTest(){
        Text text = new Text("test");
        Component c = ToComponentUtil.toComponent(0, 0, text, fm);
        assertEquals(fm.stringWidth("test"),c.getWidth());
    }

    @Test
    void toComponentTextComponent() {
        Text text = new Text("test");
        Component c = ToComponentUtil.toComponent(0, 0, text, fm);
        assertTrue(c instanceof TextComponent);
        assertEquals(text.getText(), ((TextComponent)c).getText());
    }

    @Test
    void toComponentTextInputComponent() {
        TextInputField text = new TextInputField("name");
        Component c = ToComponentUtil.toComponent(0, 0, text, fm);
        assertTrue(c instanceof TextInputFieldComponent);
        assertEquals(text.getName(), ((TextInputFieldComponent)c).getName());
        assertEquals(30+20,c.getHeight());
        assertEquals(800,c.getWidth());
    }

    @Test
    void toComponentButtonTest(){
        Submit s = new Submit();
        Component c = ToComponentUtil.toComponent(0, 0, s, fm);
        assertTrue(c instanceof ButtonComponent);
        assertEquals("Submit", ((ButtonComponent)c).getButtonText());
    }

    @Test
    void toComponentFormComponentTest(){
        Form f = new Form("action",new Submit());
        Component c = ToComponentUtil.toComponent(0, 0, f, fm);
        assertTrue(c instanceof FormComponent);
        assertTrue(((FormComponent) c).getContent() instanceof ButtonComponent);
    }

    @Test
    void toComponentTableComponent(){
        Link tc1 = new Link("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html");
        Text tc2 = new Text("hi");
        TableRow r1 = new TableRow(java.util.List.of(tc1));
        TableRow r2 = new TableRow(java.util.List.of(tc2));
        Table table = new Table(List.of(r1, r2));

        Component c = ToComponentUtil.toComponent(0, 0, table, fm);
        assertTrue(c instanceof TableComponent);
        assertTrue(((TableComponent)c).getRows().get(0).getCells().get(0) instanceof LinkComponent);
        assertTrue(((TableComponent)c).getRows().get(1).getCells().get(0) instanceof TextComponent);

        assertEquals(tc1.getHref(), ((LinkComponent)((TableComponent)c).getRows().get(0).getCells().get(0)).getHref());
        assertEquals(tc2.getText(), ((TextComponent)((TableComponent)c).getRows().get(1).getCells().get(0)).getText());
    }

    @Test
    void toComponentNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            ToComponentUtil.toComponent(0, 0, null, fm);
        });
    }
}