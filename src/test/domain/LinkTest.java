package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
    Link link = new Link("Test");

    @Test
    void setHrefTest() {
        link.setHref("Test Setter");
        assertEquals(link.getHref(), "Test Setter");
    }

    @Test
    void setHrefNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            link.setHref(null);
        });
    }

    @Test
    void getHref() {
        assertEquals("Test", link.getHref());
    }

    @Test
    void getHtmlStringTest(){
        assertEquals("<a href=\"Test\">Test</a>", link.getHtmlString());
    }
}