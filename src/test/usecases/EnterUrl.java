package usecases;

import domain.DocumentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.window.BrowsrView;
import ui.component.LinkComponent;
import ui.component.TableComponent;
import ui.component.TextComponent;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.screens.BrowsrScreen;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnterUrl {
    private Canvas cv = new Canvas();
    private FontMetrics metrics = cv.getFontMetrics(new Font(Font.MONOSPACED, Font.PLAIN, 22));
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);
    BrowsrScreen s;

    @BeforeEach
    public void init(){
        browsr.setMetrics(metrics);
        s = new BrowsrScreen(1000, 1000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    public void enterUrlTest() {
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        // Test if the main page is shown correctly
        assertTrue(left.getDoc().getComponent() instanceof TableComponent);
        assertEquals(5, ((TableComponent)left.getDoc().getComponent()).getRows().size());
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(0).getCells().get(0) instanceof TextComponent);
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(1).getCells().get(0) instanceof TextComponent);
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(2).getCells().get(0) instanceof LinkComponent);

        // insert a valid url -> for success scenario
        String validURL = "https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html";
        s.getAddressbar().setUrl(validURL);

        browsr.setActive(true);
        // 1. The user clicks the Address bar.
        browsr.handleMouseEvent(500, 400, 10, 1, 1, 0);

        //2. The system indicates that the Address bar has focus, and shows the
        //entire contents of the Address bar as selected.
        assertEquals(true, s.getAddressbar().getUrlField().getEditMode());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeLeft());
        assertEquals(s.getAddressbar().getUrl().length(), s.getAddressbar().getUrlField().getSelectedRangeRight());
        assertEquals(false, s.getAddressbar().getUrlField().isSelectedEmpty());
        assertEquals("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html", s.getAddressbar().getUrlField().getInputAtFocus());

        //3. The user edits the contents of the Address bar by pressing Backspace,
        //Delete, Left, Right, Home, End, Shift-Left, Shift-Right, Shift-Home,
        //and Shift-End to move the insertion point and select text, and character
        //keys to replace the selection with the entered character or insert the
        //character at the insertion point.
        //4. The system shows the updated contents as they are being edited.

        //Illustrated with: pressing Backspace & re-adding last letter -> all the other cases are unit tested
        s.getAddressbar().handleMouseEvent(500, 400, 10, 1, 1, 0);
        s.getAddressbar().handleKeyEvent(401, 8, '\u0000', 0);
        String url = "https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.htm";
        assertEquals(url, s.getAddressbar().getUrl());

        s.getAddressbar().handleKeyEvent(401, 76, 'l', 0);
        assertEquals(url + "l", s.getAddressbar().getUrl());

        //5. The user presses Enter or clicks outside the Address bar to finish editing
        //the contents.
        s.getAddressbar().handleMouseEvent(500, 10, s.getAddressbar().getHeight() + 10, 1, 1, 0);
        assertEquals(false, s.getAddressbar().getUrlField().getEditMode());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeLeft());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeRight());
        assertEquals(true, s.getAddressbar().getUrlField().isSelectedEmpty());
        s.handleEnteredUrl(validURL);

        //6. The system loads the document and shows it in the document area.
        assertEquals("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html", controller.getUrl());

        // It loads the document and shows it in the document area.
        assertTrue(left.getDoc().getComponent() instanceof TableComponent);
        assertEquals(2, ((TableComponent)left.getDoc().getComponent()).getRows().size());
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(0).getCells().get(0) instanceof TextComponent);
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(1).getCells().get(0) instanceof TableComponent);
        TableComponent table = (TableComponent) ((TableComponent)left.getDoc().getComponent()).getRows().get(1).getCells().get(0);
        assertTrue(table.getRows().get(0).getCells().get(0) instanceof LinkComponent);
        assertTrue(table.getRows().get(0).getCells().get(1) instanceof TextComponent);
        assertTrue(table.getRows().get(1).getCells().get(0) instanceof LinkComponent);
        assertTrue(table.getRows().get(1).getCells().get(1) instanceof TextComponent);
        assertTrue(table.getRows().get(2).getCells().get(0) instanceof LinkComponent);
        assertTrue(table.getRows().get(2).getCells().get(1) instanceof TextComponent);
        assertTrue(table.getRows().get(3).getCells().get(0) instanceof LinkComponent);
        assertTrue(table.getRows().get(3).getCells().get(1) instanceof TextComponent);
    }

    @Test
    public void enterUrlTestEscape() {
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        // 1. The user clicks the Address bar.
        s.getAddressbar().handleMouseEvent(500, 300, 10, 1, 1, 0);

        //2. The system indicates that the Address bar has focus, and shows the
        //entire contents of the Address bar as selected.
        assertEquals(true, s.getAddressbar().getUrlField().getEditMode());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeLeft());
        assertEquals(s.getAddressbar().getUrl().length(), s.getAddressbar().getUrlField().getSelectedRangeRight());
        assertEquals(false, s.getAddressbar().getUrlField().isSelectedEmpty());
        assertEquals("https://", s.getAddressbar().getUrlField().getInputAtFocus());

        //3. The user edits the contents of the Address bar by pressing Backspace,
        //Delete, Left, Right, Home, End, Shift-Left, Shift-Right, Shift-Home,
        //and Shift-End to move the insertion point and select text, and character
        //keys to replace the selection with the entered character or insert the
        //character at the insertion point.
        //4. The system shows the updated contents as they are being edited.

        //Illustrated with: pressing Backspace
        s.getAddressbar().handleMouseEvent(500, 400, 10, 1, 1, 0);
        s.getAddressbar().handleKeyEvent(401, 8, '\u0000', 0);
        String url = "https:/";
        assertEquals(url, s.getAddressbar().getUrl());

        //5a. The user presses Escape to cancel editing the contents of the Address
        //bar
        //The contents are changed back to the value that was active when
        //the Address bar received focus.
        s.getAddressbar().handleKeyEvent(401, 27, '\u0000', 0);
        assertEquals(url + "/", s.getAddressbar().getUrl());
        assertEquals(false, s.getAddressbar().getUrlField().getEditMode());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeLeft());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeRight());
        assertEquals(true, s.getAddressbar().getUrlField().isSelectedEmpty());
    }

    @Test
    public void enterUrlTestLoadingFails() {
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        // 1. The user clicks the Address bar.
        s.getAddressbar().handleMouseEvent(500, 300, 10, 1, 1, 0);

        //2. The system indicates that the Address bar has focus, and shows the
        //entire contents of the Address bar as selected.
        assertEquals(true, s.getAddressbar().getUrlField().getEditMode());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeLeft());
        assertEquals(s.getAddressbar().getUrl().length(), s.getAddressbar().getUrlField().getSelectedRangeRight());
        assertEquals(false, s.getAddressbar().getUrlField().isSelectedEmpty());
        assertEquals("https://", s.getAddressbar().getUrlField().getInputAtFocus());

        //3. The user edits the contents of the Address bar by pressing Backspace,
        //Delete, Left, Right, Home, End, Shift-Left, Shift-Right, Shift-Home,
        //and Shift-End to move the insertion point and select text, and character
        //keys to replace the selection with the entered character or insert the
        //character at the insertion point.
        //4. The system shows the updated contents as they are being edited.

        //Illustrated with: pressing Backspace
        s.getAddressbar().handleMouseEvent(500, 400, 10, 1, 1, 0);
        s.getAddressbar().handleKeyEvent(401, 8, '\u0000', 0);
        String url = "https:/";
        assertEquals(url, s.getAddressbar().getUrl());

        //5. The user presses Enter or clicks outside the Address bar to finish editing
        //the contents.
        s.getAddressbar().handleMouseEvent(500, 10, s.getAddressbar().getHeight() + 10, 1, 1, 0);
        assertEquals(false, s.getAddressbar().getUrlField().getEditMode());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeLeft());
        assertEquals(0, s.getAddressbar().getUrlField().getSelectedRangeRight());
        assertEquals(true, s.getAddressbar().getUrlField().isSelectedEmpty());
        s.handleEnteredUrl("https:/");

        //6a. The URL is malformed, loading the document fails, or parsing the
        //document fails.
        //The system shows an error document in the document area.
        assertTrue(left.getDoc().getComponent() instanceof TextComponent);
        assertTrue(((TextComponent)left.getDoc().getComponent()).getText().contains("Exception"));
}
}



