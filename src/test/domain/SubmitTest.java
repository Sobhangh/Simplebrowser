package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitTest {
    Submit submit = new Submit();

    @Test
    void getHtmlStringTest(){
        assertEquals("<input type=\"submit\">", submit.getHtmlString());
    }
}
