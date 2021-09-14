package ui.window.panes;

import domain.Contentspan;
import domain.DocumentController;
import domain.Text;
import org.junit.jupiter.api.Test;
import ui.component.DocumentComponent;
import ui.component.TextComponent;
import ui.window.screens.BrowsrScreen;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class LeafPaneTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrScreen b = new BrowsrScreen(600,600,metrics,controller,null);
    //NonLeafPane parent = new NonLeafPane(0,0,600,600,b,null,null,0);
    //LeafPane left = new LeafPane(0,0,300,600,null,parent,b);
    //LeafPane right = new LeafPane(300,0,300,600,null,null,b);
    DocumentComponent doc = new DocumentComponent(0,0,600,600);
    LeafPane pane = new LeafPane(0,0,600,600,doc,null,b);

    @Test
    void getParent() {
        assertEquals(null,pane.getParent());
    }



    @Test
    void setDoc() {
        assertNull(pane.getContentspan());
        DocumentComponent d = new DocumentComponent(300,50, 5,10);
        pane.setDoc(d);
        assertEquals(d,pane.getDoc());
        assertEquals(600,d.getHeight());
        assertEquals(600,d.getWidth());
        assertEquals(0,d.getY());
        assertEquals(0,d.getX());
    }


    @Test
    void handleMouseEvent() {
        pane.handleMouseEvent(501, 700, 154, 1, 1, 1024);
        assertEquals(false,pane.isFocused());
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        assertEquals(true,pane.isFocused());

    }

    @Test
    void handleKeyEvent() {
        pane.handleKeyEvent(401,17,' ',128);
        assertEquals((false),pane.isCtrlPressed());
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        pane.handleKeyEvent(401,17,' ',128);
        assertEquals((true),pane.isCtrlPressed());
    }

    @Test
    void getDoc() {
        assertEquals(doc,pane.getDoc());
    }


    @Test
    void getUrl() {
        assertEquals("https://",pane.getUrl());
    }

    @Test
    void setUrl() {
        pane.setUrl("a");
        assertEquals("a",pane.getUrl());
    }

    @Test
    void update() {
        Text t =new Text("hi");
        pane.update(t,metrics);
        assertTrue(pane.getDoc().getComponent() instanceof TextComponent);
        assertEquals(t.getText(),((TextComponent) pane.getDoc().getComponent()).getText());
        assertEquals(0,pane.getDoc().getComponent().getX());
        assertEquals(0,pane.getDoc().getComponent().getY());
    }

    @Test
    void setX() {
        Text t =new Text("hi");
        pane.update(t,metrics);
        pane.setX(20);
        assertEquals(20,pane.getX());
        assertEquals(20,pane.getDoc().getX());
        assertEquals((20),pane.getDoc().getComponent().getX());
    }

    @Test
    void setY() {
        Text t =new Text("hi");
        pane.update(t,metrics);
        pane.setY(20);
        assertEquals(20,pane.getY());
        assertEquals(20,pane.getDoc().getY());
        assertEquals((20),pane.getDoc().getComponent().getY());
    }

    @Test
    void setWidth() {
        pane.setWidth(100);
        assertEquals(100,pane.getWidth());
        assertEquals(100,pane.getDoc().getWidth());
    }

    @Test
    void setHeight() {
        pane.setHeight(100);
        assertEquals(100,pane.getHeight());
        assertEquals(100,pane.getDoc().getHeight());
    }
}