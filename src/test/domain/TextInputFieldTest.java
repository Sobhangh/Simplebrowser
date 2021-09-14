package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextInputFieldTest {
    TextInputField input = new TextInputField("Test");

    @Test
    void setHrefTest() {
        input.setName("Test Setter");
        assertEquals("Test Setter", input.getName());
    }

    @Test
    void setHrefNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            input.setName(null);
        });
    }

    @Test
    void getHref() {
        assertEquals("Test", input.getName());
    }

    @Test
    void getHtmlStringTest(){
        assertEquals("<input type=\"text\" name=\"Test\">", input.getHtmlString());
    }
}
