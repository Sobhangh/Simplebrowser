package ui.component;

import org.junit.jupiter.api.Test;
import ui.listeners.AddBookmarkListener;
import ui.listeners.ScrollListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScrollBarTest {
    HorizontalScrollBar scrollBar = new HorizontalScrollBar(0, 0, 300);

    @Test
    void isMouseDown() {
        assertFalse(scrollBar.isMouseDown());
    }

    @Test
    void setMouseDown() {
        scrollBar.setMouseDown(true);
        assertTrue(scrollBar.isMouseDown());
    }

    @Test
    void addScrollListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            scrollBar.addScrollListener(null);
        });
    }

    @Test
    void addScrollListener(){
        ScrollListener cl = (delta) -> System.out.println("Test: Add ScrollListener");
        scrollBar.addScrollListener(cl);
        assertEquals(List.of(cl), scrollBar.getScrollListeners());
    }
}