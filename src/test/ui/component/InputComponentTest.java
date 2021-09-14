package ui.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class InputComponentTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    InputComponent input = new InputComponent(0, 0, 30, metrics);

    @BeforeEach
    public void init(){
        input.setInput("https://");
    }

    @Test
    void initialTest() {
        assertEquals(false, input.getEditMode());
        assertEquals("https://", input.getInput());
        assertEquals(0, input.getSelectedRangeLeft());
        assertEquals(0, input.getSelectedRangeRight());
        assertEquals(2, input.getOffset());
        assertEquals(c.getFontMetrics(font), input.getMetrics());
        assertEquals(false, input.getShiftDown());
    }
    @Test
    void paint() {
    }

    @Test
    void handleMouseEvent() {
        input.handleMouseEvent(500, 10, 10, 1, 1, 0);
        assertEquals(true, input.getEditMode());
        assertEquals(0, input.getSelectedRangeLeft());
        assertEquals(input.getInput().length(), input.getSelectedRangeRight());
        assertEquals(false, input.isSelectedEmpty());
        assertEquals("https://", input.getInputAtFocus());
    }

    @Test
    void handleMouseEvent2() {
        input.handleMouseEvent(500, 10, input.getHeight() + 10, 1, 1, 0);
        assertEquals(false, input.getEditMode());
        assertEquals(0, input.getSelectedRangeLeft());
        assertEquals(0, input.getSelectedRangeRight());
        assertEquals(true, input.isSelectedEmpty());
    }

    @Test
    void handleKeyEvent() {
        input.setEditMode(true);
        input.setCursorPosition(8);
        assertEquals(true, input.getEditMode());
        String url = input.getInput();
        input.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(url + "a", input.getInput());
    }

    @Test
    void handleKeyEvent2() {
        input.setEditMode(false);
        assertEquals(false, input.getEditMode());
        String url = input.getInput();
        input.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(url, input.getInput());
    }

    @Test
    void handleShiftKeyEvent() {
        //Shift keys will behave as they do for adressbar ???
        input.setEditMode(true);
        String url = input.getInput();
        input.handleKeyEvent(401, 16, '\u0000', 64);
        assertEquals(true, input.getShiftDown());
        input.handleKeyEvent(402, 16, '\u0000', 64);
        assertEquals(false, input.getShiftDown());
    }

    @Test
    void handleArrowKeyEvent() {
        input.setEditMode(true);
        input.setCursorPosition(1);
        int CursorPosition = input.getCursorPosition();
        input.handleKeyEvent(401, 37, '\u0000', 64);
        assertEquals(0, input.getCursorPosition());
        input.handleKeyEvent(401, 39, '\u0000', 64);
        assertEquals(1, input.getCursorPosition());
    }

    @Test
    void handleRemoveKeyEvent() {
        input.setEditMode(true);
        input.setCursorPosition(8);
        input.handleKeyEvent(401, 8, '\u0000', 0);
        assertEquals("https:/", input.getInput());
    }

    @Test
    void handleDeleteKeyEvent() {
        input.setEditMode(true);
        input.setCursorPosition(0);
        input.handleKeyEvent(401, 46, '\u0000', 0);
        assertEquals("ttps://", input.getInput());
    }

    @Test
    void handleEscapeKeyEvent() {
        input.setEditMode(true);
        input.setInputAtFocus("test");
        input.handleKeyEvent(401, 65, 'a', 0);
        input.handleKeyEvent(401, 27, '\u0000', 0);
        assertEquals("test", input.getInput());
        assertEquals(false, input.getEditMode());
        assertEquals(0, input.getSelectedRangeLeft());
        assertEquals(0, input.getSelectedRangeRight());
        assertEquals(true, input.isSelectedEmpty());
    }

    @Test
    void handleHomeKeyEvent() {
        input.setEditMode(true);
        input.handleKeyEvent(401, 36, '\u0000', 0);
        assertEquals(0, input.getCursorPosition());
    }

    @Test
    void handleEndKeyEvent() {
        input.setEditMode(true);
        input.setCursorPosition(0);
        input.handleKeyEvent(401, 35, '\u0000', 0);
        assertEquals(input.getInput().length(), input.getCursorPosition());
    }

    @Test
    void VerticalScrollEventTest() {
        input.setY(10);
        int y_before = input.getY();
        input.handleVerticalScroll(-1, 15, 100);
        assertEquals(15, input.getLowerLimitY());
        assertEquals(100, input.getUpperLimitY());
        int y_after = input.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        input.setX(10);
        int x_before = input.getX();
        input.handleHorizontalScroll(1, 0, 100);
        assertEquals(0, input.getLowerLimitX());
        assertEquals(100, input.getUpperLimitX());
        int x_after = input.getX();
        assertEquals(x_before - 1, x_after);
    }


    @Test
    void getCursorPosition() {
        input.handleMouseEvent(500, 100, 10, 1, 1, 0);
        assertEquals(8, input.getCursorPosition());
    }

    @Test
    void setCursorPosition() {
        input.setCursorPosition(0);
        assertEquals(0, input.getCursorPosition());
        //user should not be able to set CursorPosition at a position greater than the length of the url
        input.setCursorPosition(input.getInput().length() + 10);
        assertEquals(0, input.getCursorPosition());
        //user should not be able to set CursorPosition at a position smaller than zero
        input.setCursorPosition(-1);
        assertEquals(0, input.getCursorPosition());
    }

    @Test
    void moveCursorPosition() {
        input.handleMouseEvent(500, 100, 10, 1, 1, 0);
        input.moveCursorLeft();
        assertEquals(7, input.getCursorPosition());
        input.moveCursorRight();
        assertEquals(8, input.getCursorPosition());
    }

    @Test
    void moveCursorToEnd() {
        input.moveCursorToEndOfInput();
        assertEquals(input.getInput().length(), input.getCursorPosition());
    }

    @Test
    void setSelectedRange() {
        input.setSelectedRange(1,2);
        assertEquals(1, input.getSelectedRangeLeft());
        assertEquals(2, input.getSelectedRangeRight());
        //user should not be able to set SelectedRange at a position greater than the length of the url or smaller than zero.
        input.setSelectedRange(-1,input.getInput().length() + 10);
        assertEquals(1, input.getSelectedRangeLeft());
        assertEquals(2, input.getSelectedRangeRight());
    }

    @Test
    void resetSelectedRange() {
        input.setSelectedRange(1,1);
        input.resetSelectedRange();
        assertEquals(0, input.getSelectedRangeRight());
        assertEquals(0, input.getSelectedRangeLeft());
    }

    @Test
    void setInputAtFocus() {
        input.setInputAtFocus("test");
        assertEquals("test", input.getInputAtFocus());
    }

    @Test
    void setMetricsTest() {
        Font font2 = new Font(Font.MONOSPACED, Font.BOLD, 22);
        FontMetrics metrics2 = c.getFontMetrics(font2);
        input.setMetrics(metrics2);
        assertEquals(metrics2, input.getMetrics());
    }

    @Test
    void setInputTest(){
        this.input.setInput("test");
        assertEquals("test", this.input.getInput());
    }

    @Test
    void setInputNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            input.setInput(null);
        });
    }
}