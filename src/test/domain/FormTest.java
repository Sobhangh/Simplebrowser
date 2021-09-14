package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormTest {
    Text text = new Text("hi");
    Form form = new Form("test.php", text);

    @Test
    void getActionTest(){
        assertEquals("test.php", form.getAction());
    }

    @Test
    void getContentTest(){
        assertEquals(text, form.getContent());
    }

    @Test
    void setActionTest() {
        form.setAction("action.php");
        assertEquals("action.php", form.getAction());
    }

    @Test
    void setActionNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            form.setAction(null);
        });
    }

    @Test
    void setContentTest() {
        Link link = new Link("link");
        form.setContent(link);
        assertEquals(link, form.getContent());
    }

    @Test
    void setContentNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            form.setContent(null);
        });
    }

    @Test
    void getHtmlStringTest(){
        assertEquals("<form action=\"test.php\">hi</form>", form.getHtmlString());
    }

}
