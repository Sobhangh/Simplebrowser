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


public class ActivateHyperlink {
    private Canvas cv = new Canvas();
    private FontMetrics metrics = cv.getFontMetrics(new Font(Font.MONOSPACED, Font.PLAIN, 22));
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);
    BrowsrScreen s;

    @BeforeEach
    void init(){
        browsr.setMetrics(metrics);
        s = new BrowsrScreen(1000, 1000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    public void activateHyperLinkTest(){
        // Test if the startpage is shown correctly
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        assertTrue(left.getDoc().getComponent() instanceof TableComponent);
        assertEquals(5, ((TableComponent)left.getDoc().getComponent()).getRows().size());
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(0).getCells().get(0) instanceof TextComponent);
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(1).getCells().get(0) instanceof TextComponent);
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(2).getCells().get(0) instanceof LinkComponent);

        // 1. The user clicks a hyperlink in the document area
        browsr.setActive(true);
        browsr.handleMouseEvent(501, 121, 154, 1, 1, 1024);
        // 2.
        //The system composes the hyperlink’s href attribute value with the
        //referring document’s URL to obtain the full URL for the document to
        //be loaded.
        assertEquals("https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html", s.getAddressbar().getUrl());

        // It loads the document and shows it in the document area.
        assertTrue(left.getDoc().getComponent() instanceof TableComponent);
        assertEquals(2, ((TableComponent)left.getDoc().getComponent()).getRows().size());
        assertTrue(((TableComponent)left.getDoc().getComponent()).getRows().get(0).getCells().get(0) instanceof TextComponent);
        //assertTrue(((TableComponent)browsrWindow.getDocViewer().getComponent()).getRows().get(1).getCells().get(0) instanceof TableComponent);
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
    public void activateHyperLinkTestLoadingFails(){
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        browsr.setActive(true);
        // 1. The user clicks a hyperlink in the document area
        left.getDoc().handleMouseEvent(501, 238, 156, 1, 1, 1024);
        // Click on a link that leads to a fail while loading
        left.getDoc().handleMouseEvent(501, 29, 128, 1, 1, 1024);

        // 2.a The URL is malformed, loading the document fails, or parsing the
        //document fails
        // The system shows an error document in the document area.
        //assertTrue(browsrWindow.getDocViewer().getComponent() instanceof TextComponent);
        assertTrue(((TextComponent)left.getDoc().getComponent()).getText().contains("Exception"));

    }

}
