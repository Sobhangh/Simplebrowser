package ui.window.panes;

import domain.DocumentController;
import domain.Text;
import org.junit.jupiter.api.Test;
import ui.component.DocumentComponent;
import ui.component.TextComponent;
import ui.window.screens.BrowsrScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NonLeafPaneTest {

    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrScreen b = new BrowsrScreen(600,600,metrics,controller,null);
    DocumentComponent doc = new DocumentComponent(0,0,600,600);
    NonLeafPane pane = new NonLeafPane(0,0,600,600,b,null,null,0);
    LeafPane left = new LeafPane(0,0,300,600,doc,pane,b);
    LeafPane right = new LeafPane(300,0,300,600,null,pane,b);


    @Test
    void getParent() {
        assertEquals(null,pane.getParent());
    }


    @Test
    void setPanes() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        assertEquals(l,pane.getPanes());
    }


    @Test
    void replacePanes() {
        pane.addPane(left);
        // There should be 2 childeren.
        assertThrows(IndexOutOfBoundsException.class ,()->pane.replacePanes(left,right) );
        pane.addPane(left);
        pane.replacePanes(left,right);
        assertEquals(right,pane.getPanes().get(0));
    }

    @Test
    void removePane() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.removePane(left);
        assertEquals(right,pane.getPanes().get(0));
    }


    @Test
    void getUrl() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        assertEquals(null,pane.getUrl());
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        assertEquals("https://",pane.getUrl());
    }

    @Test
    void setUrl() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.setUrl("a");
        assertEquals(null,pane.getUrl());
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        pane.setUrl("a");
        assertEquals("a",pane.getUrl());
    }

    @Test
    void update() {
        Text t =new Text("hi");
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        pane.update(t,metrics);
        assertTrue(((LeafPane)pane.getPanes().get(0)).getDoc().getComponent() instanceof TextComponent);
    }


    @Test
    void addPane() {
        pane.addPane(left);
        assertEquals(left,pane.getPanes().get(0));
    }



    @Test
    void getSplitType() {
        assertEquals(0,pane.getSplitType());
    }


    @Test
    void handleMouseEvent() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.handleMouseEvent(501, 700, 154, 1, 1, 1024);
        assertEquals(false,pane.isFocused());
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        assertEquals(true,pane.isFocused());
        assertEquals(true,pane.getPanes().get(0).isFocused());
        assertEquals(false,pane.getPanes().get(1).isFocused());
    }

    @Test
    void handleKeyEvent() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.handleMouseEvent(501, 100, 154, 1, 1, 1024);
        pane.handleKeyEvent(401,17,' ',128);
        assertTrue(((LeafPane)pane.getPanes().get(0)).isCtrlPressed());
    }

    @Test
    void setWidth() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.setWidth(1000);
        assertEquals(1000,pane.getWidth());
        assertEquals(500,pane.getPanes().get(0).getWidth());
        assertEquals(500,pane.getPanes().get(1).getWidth());
        assertEquals(500,pane.getPanes().get(1).getX());
    }

    @Test
    void setHeight() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.setHeight(1000);
        assertEquals(1000,pane.getHeight());
        assertEquals(1000,pane.getPanes().get(0).getHeight());
        assertEquals(1000,pane.getPanes().get(1).getHeight());
    }

    @Test
    void setX() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.setX(100);
        assertEquals(100,pane.getX());
        assertEquals(100,pane.getPanes().get(0).getX());
        assertEquals(400,pane.getPanes().get(1).getX());
    }

    @Test
    void setY() {
        List<Pane> l =new ArrayList<>();
        l.add(left);
        l.add(right);
        pane.setPanes(l);
        pane.setY(100);
        assertEquals(100,pane.getY());
        assertEquals(100,pane.getPanes().get(0).getY());
        assertEquals(100,pane.getPanes().get(1).getY());
    }
}