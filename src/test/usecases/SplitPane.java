package usecases;
import domain.DocumentController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.component.TableComponent;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.panes.Pane;
import ui.window.screens.BrowsrScreen;
import ui.window.screens.SaveAsScreen;
import ui.window.screens.Screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
public class SplitPane {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);

    @BeforeEach
    void init(){
        browsr.setMetrics(metrics);
        BrowsrScreen s = new BrowsrScreen(1000, 1000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    void splitpaneHorizontalTest(){
        int h =browsr.getBrowsrScreen().getRootpane().getHeight();
        int y = browsr.getBrowsrScreen().getRootpane().getY();
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);

        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        Pane left = ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        assertTrue(left instanceof LeafPane);
        assertEquals(h,left.getHeight());
        assertEquals(499,left.getWidth());
        assertEquals(y,left.getY());
        assertEquals(true,left.isFocused());
        assertTrue(((LeafPane) left).getDoc().getComponent() instanceof TableComponent);
        Pane right = ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(1);
        assertTrue(right instanceof LeafPane);
        assertEquals(h,right.getHeight());
        assertEquals(499,right.getWidth());
        assertEquals(499,right.getX());
        assertEquals(y,right.getY());
        assertTrue(((LeafPane) right).getDoc().getComponent() instanceof TableComponent);
        assertEquals(499,((LeafPane) right).getDoc().getComponent().getX());
        assertEquals(false,right.isFocused());
    }
    @Test
    void splitpaneVerticalTest(){
        int h =browsr.getBrowsrScreen().getRootpane().getHeight();
        int y = browsr.getBrowsrScreen().getRootpane().getY();
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);

        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        Pane up = ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        assertTrue(up instanceof LeafPane);
        assertEquals(h/2,up.getHeight());
        assertEquals(1000-2,up.getWidth());
        assertEquals(y,up.getY());
        assertEquals(true,up.isFocused());
        assertTrue(((LeafPane) up).getDoc().getComponent() instanceof TableComponent);
        Pane down = ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(1);
        assertTrue(down instanceof LeafPane);
        assertEquals(h/2,down.getHeight());
        assertEquals(1000-2,down.getWidth());
        assertEquals(y+h/2,down.getY());
        assertTrue(((LeafPane) down).getDoc().getComponent() instanceof TableComponent);
        assertEquals(y+h/2,((LeafPane) down).getDoc().getComponent().getY());
        assertEquals(false,down.isFocused());
    }


}
