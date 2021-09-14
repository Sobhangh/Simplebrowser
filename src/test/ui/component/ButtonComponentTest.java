package ui.component;

import org.junit.jupiter.api.Test;

import java.awt.event.MouseEvent;

import static java.awt.event.MouseEvent.*;
import static org.junit.jupiter.api.Assertions.*;

class ButtonComponentTest {

    ButtonComponent button = new ButtonComponent("test" , 0,0 ,10 , 20);
    @Test
    void getText() {
        assertEquals("test", button.getButtonText());
    }

    @Test
    void setText() {
        this.button.setButtonText("test2");
        assertEquals("test2", button.getButtonText());
    }

    @Test
    void setTextNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            button.setButtonText(null);
        });
    }

    @Test
    void  buttonClicked() {
        button.handleMouseEvent(MouseEvent.MOUSE_PRESSED,1,1,1, 1, 1024);
        assertEquals(true,button.isClicked());
    }

    @Test
    void  buttonClickedOutside() {
        button.handleMouseEvent(MouseEvent.MOUSE_PRESSED,30,30,1, 1, 1024);
        assertEquals(false,button.isClicked());
    }

    @Test
    void  buttonReleased() {
        button.handleMouseEvent(MouseEvent.MOUSE_PRESSED,1,1,1, 1, 1024);
        button.handleMouseEvent(MouseEvent.MOUSE_RELEASED,1,1,1, 1, 1024);
        assertEquals(false,button.isClicked());
    }

    @Test
    void  buttonClicked2() {
        button.handleMouseEvent(MouseEvent.MOUSE_PRESSED,1,1,1, 1, 1024, "name", "url");
        assertEquals(true,button.isClicked());
    }

    @Test
    void  buttonClickedOutside2() {
        button.handleMouseEvent(MouseEvent.MOUSE_PRESSED,30,30,1, 1, 1024, "name", "url");
        assertEquals(false,button.isClicked());
    }

    @Test
    void  buttonReleased2() {
        button.handleMouseEvent(MouseEvent.MOUSE_PRESSED,1,1,1, 1, 1024, "name", "url");
        button.handleMouseEvent(MouseEvent.MOUSE_RELEASED,1,1,1, 1, 1024, "name", "url");
        assertEquals(false,button.isClicked());
    }

    @Test
    void VerticalScrollEventTest() {
        button.setY(10);
        int y_before = button.getY();
        button.handleVerticalScroll(-1, y_before + 5, y_before + 10);
        assertEquals(y_before + 5, button.getLowerLimitY());
        assertEquals(y_before + 10, button.getUpperLimitY());
        int y_after = button.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        button.setX(10);
        int x_before = button.getX();
        button.handleHorizontalScroll(1, x_before, x_before + 5);
        assertEquals(x_before, button.getLowerLimitX());
        assertEquals(x_before + 5, button.getUpperLimitX());
        int x_after = button.getX();
        assertEquals(x_before - 1, x_after);
    }

}