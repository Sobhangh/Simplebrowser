package usecases;

import domain.DocumentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.panes.Pane;
import ui.window.screens.BrowsrScreen;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class DragScrollBar {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);

    @BeforeEach
    void init(){
        browsr.setMetrics(metrics);
        BrowsrScreen s = new BrowsrScreen(700, 150, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    void dragHorizontal(){
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);

        // DRAG HORIZONTAL SCROLLER TO THE RIGHT
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED, left.getDoc().getX() + 5, 140, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, left.getDoc().getX() + 10, 140, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED, left.getDoc().getX() + 10, 140, 1, 0, 0);

        // X-COORDINATE OF COMPONENT GETS DECREASED BY DELTA RELATIVE TO ITS OWN WIDTH
        assertEquals(5, left.getDoc().getHorizontalScrollBar().getDeltaX());
        double new_x = Math.round(-1 * 5/left.getDoc().getPercentageWidth());
        assertEquals(new_x, left.getDoc().getComponent().getX());

        // DRAG HORIZONTAL SCROLLER TO THE LEFT
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED, left.getDoc().getX() + 10, 140, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, left.getDoc().getX() + 5, 140, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED, left.getDoc().getX() + 5, 140, 1, 0, 0);

        // X-COORDINATE OF COMPONENT GETS INCREASED BY DELTA RELATIVE TO ITS OWN WIDTH
        assertEquals(-5, left.getDoc().getHorizontalScrollBar().getDeltaX());
        new_x += Math.round(5/left.getDoc().getPercentageWidth());
        assertEquals(new_x, left.getDoc().getComponent().getX());
    }

    @Test
    void dragVertical(){
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);

        // DRAG VERTICAL SCROLLER TO THE DOWN
        int comp_y = left.getDoc().getComponent().getY();
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED, 335, left.getDoc().getY() + 5, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, 335, left.getDoc().getY() + 10, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED,335, left.getDoc().getY() + 10, 1, 0, 0);

        // Y-COORDINATE OF COMPONENT GETS DECREASED BY DELTA RELATIVE TO ITS OWN HEIGHT
        assertEquals(5, left.getDoc().getVerticalScrollBar().getDeltaY());
        comp_y -= Math.round(5/left.getDoc().getVerticalScrollBar().getPercentage());
        assertEquals(comp_y, left.getDoc().getComponent().getY());

        // DRAG VERTICAL SCROLLER TO THE UP
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED, 335, left.getDoc().getY() + 10, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, 335, left.getDoc().getY() + 5, 1, 0, 0);
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED, 335, left.getDoc().getY() + 5, 1, 0, 0);

        // Y-COORDINATE OF COMPONENT GETS INCREASED BY DELTA RELATIVE TO ITS OWN HEIGHT
        assertEquals(-5, left.getDoc().getVerticalScrollBar().getDeltaY());
        comp_y += Math.round(5/left.getDoc().getVerticalScrollBar().getPercentage());
        assertEquals(comp_y, left.getDoc().getComponent().getY());
    }

}
