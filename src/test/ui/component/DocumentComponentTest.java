package ui.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class DocumentComponentTest {
    DocumentComponent documentComponent = new DocumentComponent(0, 0, 10, 10);

    @BeforeEach
    void init(){
        this.documentComponent.setComponent(new TextComponent(0, 0, 10, 10, "test"));
    }

    @Test
    void setComponent() {
        this.documentComponent.setComponent(new LinkComponent(0, 0, 10, 10, "test.html"));
        assertTrue(this.documentComponent.getComponent() instanceof LinkComponent);
        assertEquals("test.html", ((LinkComponent)this.documentComponent.getComponent()).getHref());
    }

    @Test
    void setComponentNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            this.documentComponent.setComponent(null);
        });
    }

    @Test
    void handleMouseEvent(){
        this.documentComponent.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        this.documentComponent.handleKeyEvent(401, 8, '\u0000', 0);
    }

    @Test
    void paint(){
    }
}