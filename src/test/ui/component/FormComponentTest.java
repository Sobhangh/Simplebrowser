package ui.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.listeners.EditListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FormComponentTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    TextInputFieldComponent t = new TextInputFieldComponent(1,1,30,metrics,"inpField");
    ButtonComponent b = new ButtonComponent("button",1,35 , 20,20);
    List<Component> l1 = new ArrayList<>();
    List<Component> l2 = new ArrayList<>();
    TableComponent table;
    FormComponent form;
    String url = "";

    @BeforeEach
    public void init(){
        l1.add(t);
        TableRowComponent r1 = new TableRowComponent(1,1,l1);
        l2.add(b);
        TableRowComponent r2 = new TableRowComponent(1,11,l2);
        List<TableRowComponent> rows = new ArrayList<>();
        rows.add(r1);
        rows.add(r2);
        this.table = new TableComponent(1,1,rows);
        this.form = new FormComponent(table,"action.php",1,1,1000,100);
    }

    @Test
    void inputTest() {
        this.form.handleMouseEvent(500,5,10,1,1,1024);
        this.form.handleKeyEvent(401, 65, 'a', 0);
        this.form.handleKeyEvent(401, 13, '\n', 0);
        String key = null;
        String value = null;
        for (Map.Entry<String, String> entry : form.inputs.entrySet()) {
            key= entry.getKey();
            value = entry.getValue();
        }
        assertEquals("inpField",key);
        assertEquals("a",value);
    }

    @Test
    void urlTest() {
        this.form.addLinkClickedListener((link) -> this.url = link);
        this.form.handleMouseEvent(500,5,10,1,1,1024);
        this.form.handleKeyEvent(401, 65, 'a', 0);
        this.form.handleKeyEvent(401, 13, '\n', 0);
        this.form.handleMouseEvent(MouseEvent.MOUSE_PRESSED,5,37,1,1,1024);
        this.form.handleMouseEvent(MouseEvent.MOUSE_RELEASED,5,37,1,1,1024);

        String name = null;
        name = URLEncoder.encode("inpField", StandardCharsets.UTF_8);
        String value = URLEncoder.encode("a", StandardCharsets.UTF_8);
        String newLink = "action.php" + "?" + name + "=" + value;
        assertEquals(newLink,this.url);
    }

    @Test
    void setContentTest(){
        TextComponent textComponent = new TextComponent(0, 0, 100, 100, "test");
        this.form.setContent(textComponent);
        assertEquals(textComponent, this.form.getContent());
    }

    @Test
    void setContentNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            form.setContent(null);
        });
    }

}