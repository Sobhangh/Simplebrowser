package ui.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextComponentTest {
    TextComponent textComponent = new TextComponent(0, 0, 10, 10, "Text test");

    @Test
    void getText() {
        assertEquals("Text test", textComponent.getText());
    }

    @Test
    void setText() {
        this.textComponent.setText("Text Test Setter");
        assertEquals("Text Test Setter", this.textComponent.getText());
    }

    @Test
    void setTextNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.setText(null);
        });
    }

    @Test
    void handleMouseEvent(){
        this.textComponent.handleMouseEvent(501, 238, 138, 1, 1, 1024);
    }

    @Test
    void handleKeyEvent(){
        this.textComponent.handleKeyEvent(401, 8, '\u0000', 0);
    }

    @Test
    void VerticalScrollEventTest() {
        textComponent.setY(10);
        int y_before = textComponent.getY();
        textComponent.handleVerticalScroll(-1, 15, 100);
        assertEquals(15, textComponent.getLowerLimitY());
        assertEquals(100, textComponent.getUpperLimitY());
        int y_after = textComponent.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        textComponent.setX(10);
        int x_before = textComponent.getX();
        textComponent.handleHorizontalScroll(1, 0, 15);
        assertEquals(0, textComponent.getLowerLimitX());
        assertEquals(15, textComponent.getUpperLimitX());
        int x_after = textComponent.getX();
        assertEquals(x_before - 1, x_after);
    }

}