package ui.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkComponentTest {
    LinkComponent linkComponent = new LinkComponent(0, 0, 10, 10, "a.html");

    @Test
    void getHref() {
        assertEquals("a.html", this.linkComponent.getHref());
    }

    @Test
    void setHref() {
        this.linkComponent.setHref("test.html");
        assertEquals("test.html", this.linkComponent.getHref());
    }

    @Test
    void setHrefNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.linkComponent.setHref(null);
        });
    }

    @Test
    void setName() {
        this.linkComponent.setName("test");
        assertEquals("test", this.linkComponent.getName());
    }

    @Test
    void setNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.linkComponent.setName(null);
        });
    }

    @Test
    void handleMouseEvent(){
        this.linkComponent.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        this.linkComponent.handleKeyEvent(401, 8, '\u0000', 0);
    }

    @Test
    void VerticalScrollEventTest() {
        this.linkComponent.setY(10);
        int y_before = this.linkComponent.getY();
        this.linkComponent.handleVerticalScroll(-1, 15, 100);
        assertEquals(15, linkComponent.getLowerLimitY());
        assertEquals(100, linkComponent.getUpperLimitY());
        int y_after = this.linkComponent.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        this.linkComponent.setX(10);
        int x_before = this.linkComponent.getX();
        this.linkComponent.handleHorizontalScroll(1, 0, 15);
        assertEquals(0, linkComponent.getLowerLimitX());
        assertEquals(15, linkComponent.getUpperLimitX());
        int x_after = this.linkComponent.getX();
        assertEquals(x_before - 1, x_after);
    }

}