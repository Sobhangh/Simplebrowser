package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DTextTest {
    Text text = new Text("Test");

    @Test
    void setHrefTest() {
        text.setText("Test Setter");
        assertEquals("Test Setter", text.getText());
    }

    @Test
    void setHrefNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            text.setText(null);
        });
    }

    @Test
    void getHref() {
        assertEquals("Test", text.getText());
    }

    @Test
    void getHtmlStringTest(){
        assertEquals("Test", text.getHtmlString());
    }
}
