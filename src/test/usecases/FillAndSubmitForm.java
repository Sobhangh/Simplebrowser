package usecases;

import domain.DocumentController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.component.*;
import ui.component.TextComponent;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.screens.BrowsrScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class FillAndSubmitForm {

    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);
    TableComponent t;
    TextInputFieldComponent character;
    TextInputFieldComponent num;
    ButtonComponent b;
    DocumentComponent doc;
    BrowsrScreen s;
    String url = "https://people.cs.kuleuven.be/~bart.jacobs/swop/browsrformtest.html";

    @BeforeEach
    void init(){
        browsr.setMetrics(metrics);
        s = new BrowsrScreen(2000, 2000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setActive(true);
        //browsr.backToBrowsr();
        browsr.setCurrent(s);
        browsr.setPanel();
        //browsr.handleMouseEvent(501, 900, 900, 1, 1, 1024);
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        s.handleEnteredUrl(url);
        doc = left.getDoc();
        t = (TableComponent) ((FormComponent) doc.getComponent()).getContent();
        TableComponent tIn = (TableComponent) t.getRows().get(1).getCells().get(0);
        character = (TextInputFieldComponent) tIn.getRows().get(0).getCells().get(1);
        num = (TextInputFieldComponent) tIn.getRows().get(1).getCells().get(1);
        b = (ButtonComponent) t.getRows().get(2).getCells().get(0);
    }

    @Test
    void FillandSubmitTest() {
        assertEquals(url, controller.getUrl());
        assertEquals(url, s.getUrl());

        browsr.setActive(true);
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,470,122,1,1,1024);
        assertTrue(character.getEditMode());
        browsr.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(character.getInput(),"a");
        browsr.handleKeyEvent(401, 13, '\n', 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,470,190,1,1,1024);
        assertTrue(num.getEditMode());
        browsr.handleKeyEvent(401, 49, '1', 0);
        assertEquals(num.getInput(),"1");
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,465,300,1,1,1024);

        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED,470,240,1, 1, 1024);
        assertTrue(b.isClicked());
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED,470,240,1, 1, 1024);
        assertFalse(b.isClicked());

        String urlform = "https://people.cs.kuleuven.be/~bart.jacobs/swop/browsrformactiontest.php?max_nb_results=1&starts_with=a";
        assertEquals(urlform, controller.getUrl());
        assertEquals(urlform, s.getAddressbar().getUrl());

        assertTrue(doc.getComponent() instanceof TableComponent);
        TextComponent txt = (TextComponent) ((TableComponent) doc.getComponent()).getRows().get(0).getCells().get(0);
        assertEquals("a",txt.getText());

    }

}
