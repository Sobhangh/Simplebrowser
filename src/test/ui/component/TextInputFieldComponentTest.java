package ui.component;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TextInputFieldComponentTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    TextInputFieldComponent inputFieldComponent = new TextInputFieldComponent(0, 0, 40, 30, metrics, "Test");

    @Test
    void getName() {
        assertEquals("Test", inputFieldComponent.getName());
    }

    @Test
    void setName() {
        inputFieldComponent.setName("Test setter");
        assertEquals("Test setter", inputFieldComponent.getName());
    }

    @Test
    void setNameNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            inputFieldComponent.setName(null);
        });
    }
}