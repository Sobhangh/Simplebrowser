package ui.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class HorizontalScrollBarTest {
    HorizontalScrollBar scrollBar = new HorizontalScrollBar(0, 0, 300);

    @Test
    void setXTest(){
        scrollBar.setX(100);
        assertEquals(100, scrollBar.getX());
        assertEquals(100, scrollBar.getXslider());
    }

    @Test
    void setSliderXTest(){
        scrollBar.setXslider(10);
        assertEquals(10, scrollBar.getXslider());
    }

    @Test
    void getSliderXTest(){
        assertEquals(0, scrollBar.getXslider());
    }

    @Test
    void getSliderWidthTest(){
        assertEquals(300, scrollBar.getWidthslider());
    }

    @Test
    void setSliderWidthTest(){
        scrollBar.setWidthslider(100);
        assertEquals(100, scrollBar.getWidthslider());
    }

    @Test
    void setSliderWidthByPercentageTest(){
        scrollBar.setWidthsliderPercentage(0.5);
        assertEquals(150, scrollBar.getWidthslider());
        assertEquals(0.5, scrollBar.getPercentage());
    }

    @Test
    void getPercentageTest() {
        assertEquals(1.0, this.scrollBar.getPercentage());
    }

    @Test
    void setPercentageTest(){
        scrollBar.setPercentage(0.65);
        assertEquals(0.65, scrollBar.getPercentage());
    }

    @Test
    void getPreviousXTest(){
        assertEquals(0, scrollBar.getPreviousX());
    }

    @Test
    void setPreviousXTest(){
        scrollBar.setPreviousX(25);
        assertEquals(25, scrollBar.getPreviousX());
    }

    @Test
    void getDeltaXTest(){
        assertEquals(0, scrollBar.getDeltaX());
    }

    @Test
    void setDeltaXTest(){
        scrollBar.setDeltaX(5);
        assertEquals(5, scrollBar.getDeltaX());
    }

    @Test
    void copyTest(){
        HorizontalScrollBar scrollBar_copy = (HorizontalScrollBar) scrollBar.copy();
        assertEquals(scrollBar.getX(), scrollBar_copy.getX());
        assertEquals(scrollBar.getY(), scrollBar_copy.getY());
        assertEquals(scrollBar.getWidth(), scrollBar_copy.getWidth());
    }

    @Test
    void dragToRightTest(){
        scrollBar.setWidthsliderPercentage(0.5);
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_PRESSED, 0, 0, 1, 0, 0);
        assertTrue(scrollBar.isMouseDown());
        assertEquals(0, scrollBar.getPreviousX());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, 10, 0, 1, 0, 0);
        assertEquals(10, scrollBar.getPreviousX());
        assertEquals(10, scrollBar.getDeltaX());
        assertEquals(10, scrollBar.getXslider());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_RELEASED, 10, 0, 1, 0, 0);
        assertFalse(scrollBar.isMouseDown());
    }

    @Test
    void dragToLeftTest(){
        scrollBar.setXslider(10);
        scrollBar.setWidthsliderPercentage(0.5);
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_PRESSED, 10, 0, 1, 0, 0);
        assertTrue(scrollBar.isMouseDown());
        assertEquals(10, scrollBar.getPreviousX());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_DRAGGED, 0, 0, 1, 0, 0);
        assertEquals(0, scrollBar.getPreviousX());
        assertEquals(-10, scrollBar.getDeltaX());
        assertEquals(0, scrollBar.getXslider());
        scrollBar.handleMouseEvent(MouseEvent.MOUSE_RELEASED, 0, 0, 1, 0, 0);
        assertFalse(scrollBar.isMouseDown());
    }

}
