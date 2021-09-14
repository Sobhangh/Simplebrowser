package ui.component;

import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

class VerticalScrollBarTest {
    VerticalScrollBar scrollBar = new VerticalScrollBar(0, 0, 300);

    @Test
    void setYTest(){
        scrollBar.setY(100);
        assertEquals(100, scrollBar.getY());
        assertEquals(100, scrollBar.getYslider());
    }

    @Test
    void setHeightsliderPercentage() {
        scrollBar.setHeightsliderPercentage(0.5);
        assertEquals(150, scrollBar.getHeightslider());
        assertEquals(0.5, scrollBar.getPercentage());
    }

    @Test
    void copy() {
        VerticalScrollBar scrollBar_copy = (VerticalScrollBar) scrollBar.copy();
        assertEquals(scrollBar.getX(), scrollBar_copy.getX());
        assertEquals(scrollBar.getY(), scrollBar_copy.getY());
        assertEquals(scrollBar.getHeight(), scrollBar_copy.getHeight());
    }

    @Test
    void getHeightslider() {
        assertEquals(300, scrollBar.getHeightslider());
    }

    @Test
    void setHeightslider() {
        scrollBar.setHeightslider(100);
        assertEquals(100, scrollBar.getHeightslider());
    }

    @Test
    void getYslider() {
        assertEquals(0, scrollBar.getYslider());
    }

    @Test
    void setYslider() {
        scrollBar.setYslider(10);
        assertEquals(10, scrollBar.getYslider());
    }

    @Test
    void getDeltaY() {
        assertEquals(0, scrollBar.getDeltaY());
    }

    @Test
    void setDeltaY() {
        scrollBar.setDeltaY(5);
        assertEquals(5, scrollBar.getDeltaY());
    }

    @Test
    void getPreviousY() {
        assertEquals(0, scrollBar.getPreviousY());
    }

    @Test
    void setPreviousY() {
        scrollBar.setPreviousY(25);
        assertEquals(25, scrollBar.getPreviousY());
    }

    @Test
    void getPercentage() {
        assertEquals(1.0, this.scrollBar.getPercentage());
    }

    @Test
    void setPercentage() {
        scrollBar.setPercentage(0.65);
        assertEquals(0.65, scrollBar.getPercentage());
    }

    @Test
    void dragDownTest(){
        scrollBar.setHeightsliderPercentage(0.5);
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 1, 0, 0);
        assertTrue(scrollBar.isMouseDown());
        assertEquals(0, scrollBar.getPreviousY());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, 0, 10, 1, 0, 0);
        assertEquals(10, scrollBar.getPreviousY());
        assertEquals(10, scrollBar.getDeltaY());
        assertEquals(10, scrollBar.getYslider());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_RELEASED, 0, 10, 1, 0, 0);
        assertFalse(scrollBar.isMouseDown());
    }

    @Test
    void dragUpTest(){
        scrollBar.setYslider(10);
        scrollBar.setHeightsliderPercentage(0.5);
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_PRESSED, 0, 10, 1, 0, 0);
        assertTrue(scrollBar.isMouseDown());
        assertEquals(10, scrollBar.getPreviousY());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, 0, 0, 1, 0, 0);
        assertEquals(0, scrollBar.getPreviousY());
        assertEquals(-10, scrollBar.getDeltaY());
        assertEquals(0, scrollBar.getYslider());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_RELEASED, 0, 0, 1, 0, 0);
        assertFalse(scrollBar.isMouseDown());
    }
}