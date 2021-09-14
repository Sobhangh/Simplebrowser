package ui.component;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

public class AddressBarComponentTest {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    AddressBarComponent addressbar = new AddressBarComponent(0, 0, 600, 30, metrics, "https://");

    @Test
    void initialTest() {
        assertEquals(false, addressbar.getUrlField().getEditMode());
        assertEquals("https://", addressbar.getUrl());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeLeft());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeRight());
        assertEquals(2, addressbar.getUrlField().getOffset());
        assertEquals(c.getFontMetrics(font), addressbar.getUrlField().getMetrics());
        assertEquals(false, addressbar.getUrlField().getShiftDown());
    }
    @Test
    void paint() {
    }

    @Test
    void handleMouseEvent() {
        addressbar.handleMouseEvent(500, 10, 10, 1, 1, 0);
        assertEquals(true, addressbar.getUrlField().getEditMode());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeLeft());
        assertEquals(addressbar.getUrl().length(), addressbar.getUrlField().getSelectedRangeRight());
        assertEquals(false, addressbar.getUrlField().isSelectedEmpty());
        assertEquals("https://", addressbar.getUrlField().getInputAtFocus());
    }

    @Test
    void handleMouseEvent2() {
        addressbar.handleMouseEvent(500, 10, addressbar.getHeight() + 10, 1, 1, 0);
        assertEquals(false, addressbar.getUrlField().getEditMode());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeLeft());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeRight());
        assertEquals(true, addressbar.getUrlField().isSelectedEmpty());
    }

    @Test
    void handleKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.getUrlField().setCursorPosition(8);
        assertEquals(true, addressbar.getUrlField().getEditMode());
        String url = addressbar.getUrl();
        addressbar.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(url + "a", addressbar.getUrl());
    }

    @Test
    void handleKeyEvent2() {
        addressbar.getUrlField().setEditMode(false);
        assertEquals(false, addressbar.getUrlField().getEditMode());
        String url = addressbar.getUrl();
        addressbar.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(url, addressbar.getUrl());
    }

    @Test
    void handleShiftKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        String url = addressbar.getUrl();
        addressbar.handleKeyEvent(401, 16, '\u0000', 64);
        assertEquals(true, addressbar.getUrlField().getShiftDown());
        addressbar.handleKeyEvent(402, 16, '\u0000', 64);
        assertEquals(false, addressbar.getUrlField().getShiftDown());
    }

    @Test
    void handleArrowKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.getUrlField().setCursorPosition(1);
        int CursorPosition = addressbar.getUrlField().getCursorPosition();
        addressbar.handleKeyEvent(401, 37, '\u0000', 64);
        assertEquals(0, addressbar.getUrlField().getCursorPosition());
        addressbar.handleKeyEvent(401, 39, '\u0000', 64);
        assertEquals(1, addressbar.getUrlField().getCursorPosition());
    }

    @Test
    void handleRemoveKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.getUrlField().setCursorPosition(8);
        addressbar.handleKeyEvent(401, 8, '\u0000', 0);
        assertEquals("https:/", addressbar.getUrl());
    }

    @Test
    void handleDeleteKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.getUrlField().setCursorPosition(0);
        addressbar.handleKeyEvent(401, 46, '\u0000', 0);
        assertEquals("ttps://", addressbar.getUrl());
    }

    @Test
    void handleEscapeKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.getUrlField().setInputAtFocus("test");
        addressbar.handleKeyEvent(401, 65, 'a', 0);
        addressbar.handleKeyEvent(401, 27, '\u0000', 0);
        assertEquals("test", addressbar.getUrl());
        assertEquals(false, addressbar.getUrlField().getEditMode());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeLeft());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeRight());
        assertEquals(true, addressbar.getUrlField().isSelectedEmpty());
    }

    @Test
    void handleHomeKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.handleKeyEvent(401, 36, '\u0000', 0);
        assertEquals(0, addressbar.getUrlField().getCursorPosition());
    }

    @Test
    void handleEndKeyEvent() {
        addressbar.getUrlField().setEditMode(true);
        addressbar.getUrlField().setCursorPosition(0);
        addressbar.handleKeyEvent(401, 35, '\u0000', 0);
        assertEquals(addressbar.getUrl().length(), addressbar.getUrlField().getCursorPosition());
    }


    @Test
    void getCursorPosition() {
        addressbar.handleMouseEvent(500, 100, 10, 1, 1, 0);
        assertEquals(8, addressbar.getUrlField().getCursorPosition());
    }

    @Test
    void setCursorPosition() {
        addressbar.getUrlField().setCursorPosition(0);
        assertEquals(0, addressbar.getUrlField().getCursorPosition());
        //user should not be able to set CursorPosition at a position greater than the length of the url
        addressbar.getUrlField().setCursorPosition(addressbar.getUrl().length() + 10);
        assertEquals(0, addressbar.getUrlField().getCursorPosition());
        //user should not be able to set CursorPosition at a position smaller than zero
        addressbar.getUrlField().setCursorPosition(-1);
        assertEquals(0, addressbar.getUrlField().getCursorPosition());
    }

    @Test
    void moveCursorPosition() {
        addressbar.handleMouseEvent(500, 100, 10, 1, 1, 0);
        addressbar.getUrlField().moveCursorLeft();
        assertEquals(7, addressbar.getUrlField().getCursorPosition());
        addressbar.getUrlField().moveCursorRight();
        assertEquals(8, addressbar.getUrlField().getCursorPosition());
    }

    @Test
    void moveCursorToEnd() {
        addressbar.getUrlField().moveCursorToEndOfInput();
        assertEquals(addressbar.getUrl().length(), addressbar.getUrlField().getCursorPosition());
    }

    @Test
    void setSelectedRange() {
        addressbar.getUrlField().setSelectedRange(1,2);
        assertEquals(1, addressbar.getUrlField().getSelectedRangeLeft());
        assertEquals(2, addressbar.getUrlField().getSelectedRangeRight());
        //user should not be able to set SelectedRange at a position greater than the length of the url or smaller than zero.
        addressbar.getUrlField().setSelectedRange(-1,addressbar.getUrl().length() + 10);
        assertEquals(1, addressbar.getUrlField().getSelectedRangeLeft());
        assertEquals(2, addressbar.getUrlField().getSelectedRangeRight());
    }

    @Test
    void resetSelectedRange() {
        addressbar.getUrlField().setSelectedRange(1,1);
        addressbar.getUrlField().resetSelectedRange();
        assertEquals(0, addressbar.getUrlField().getSelectedRangeRight());
        assertEquals(0, addressbar.getUrlField().getSelectedRangeLeft());
    }

    @Test
    void setUrlAtFocus() {
        addressbar.getUrlField().setInputAtFocus("test");
        assertEquals("test", addressbar.getUrlField().getInputAtFocus());
    }

    @Test
    void setMetricsTest() {
        Font font2 = new Font(Font.MONOSPACED, Font.BOLD, 22);
        FontMetrics metrics2 = c.getFontMetrics(font2);
        addressbar.getUrlField().setMetrics(metrics2);
        assertEquals(metrics2, addressbar.getUrlField().getMetrics());
    }

    @Test
    void setUrl(){
        String url = "http://test.com";
        addressbar.setUrl(url);
        assertEquals(url, addressbar.getUrlField().getInput());
    }

    @Test
    void setNullUrl(){
        assertThrows(IllegalArgumentException.class, () -> {
            addressbar.setUrl(null);
        });
    }

    @Test
    void setUrlField(){
        InputComponent url = new InputComponent(0, 0, 600, 30, metrics, "https://");
        addressbar.setUrlField(url);
        assertEquals(url, addressbar.getUrlField());
    }

    @Test
    void setNullUrlField(){
        assertThrows(IllegalArgumentException.class, () -> {
            addressbar.setUrlField(null);
        });
    }

}
