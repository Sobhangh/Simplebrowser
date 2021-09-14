package ui.component;

import org.junit.jupiter.api.Test;

import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookmarkBarComponentTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    BookmarkBarComponent bookmarkbar = new BookmarkBarComponent(0, 30, 600, 30, metrics);

    @Test
    void initialTest() {
        assertEquals(metrics, bookmarkbar.getMetrics());
        assertEquals(0, bookmarkbar.getX());
        assertEquals(30, bookmarkbar.getY());
        assertEquals(600, bookmarkbar.getWidth());
        assertEquals(30, bookmarkbar.getHeight());
        assertEquals(0, bookmarkbar.getAmountOfBookmarks());
    }

    @Test
    void addBookmarkTest() {
        bookmarkbar.addBookmark("test", "https://www.test.com");
        assertEquals(1, bookmarkbar.getAmountOfBookmarks());
        assertEquals("https://www.test.com", bookmarkbar.getBookmarkAtIndex(0).getLink().getHref());
        assertEquals("test", bookmarkbar.getBookmarkAtIndex(0).getName());
    }

    @Test
    void handleMouseEvent(){
        bookmarkbar.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        bookmarkbar.handleKeyEvent(401, 8, '\u0000', 0);
    }
}
