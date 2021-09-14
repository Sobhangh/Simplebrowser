package ui.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;

public class BookmarkComponentTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    BookmarkComponent bookmark = new BookmarkComponent(1, 30, 600, 30, metrics, "test", "https://www.test.com");

    @Test
    void initialTest() {
        assertEquals("test", bookmark.getName());
        assertEquals("https://www.test.com", bookmark.getLink().getHref());
        assertEquals(1, bookmark.getX());
        assertEquals(30, bookmark.getY());
        assertEquals(600, bookmark.getWidth());
        assertEquals(30, bookmark.getHeight());
    }

    @Test
    void changeBookmark() {
        bookmark.setName("changed");
        assertEquals("changed", bookmark.getName());
        bookmark.setLink("https://changed.com");
        assertEquals("https://changed.com", bookmark.getLink().getHref());
    }

    @Test
    void handleMouseEvent(){
        bookmark.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        bookmark.handleKeyEvent(401, 8, '\u0000', 0);
    }
}