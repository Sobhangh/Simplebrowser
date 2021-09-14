package usecases;

import domain.Contentspan;
import domain.DocumentController;
import domain.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.component.DocumentComponent;
import ui.component.TableComponent;
import ui.component.TextComponent;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.panes.Pane;
import ui.window.screens.BrowsrScreen;

import java.awt.*;
import java.nio.channels.NoConnectionPendingException;

import static org.junit.jupiter.api.Assertions.*;

public class DeletePanes {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);

    @BeforeEach
    void init() {
        browsr.setMetrics(metrics);
        BrowsrScreen s = new BrowsrScreen(1000, 1000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    public void replaceRootPaneTest() {
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof LeafPane);
        LeafPane rootPane = (LeafPane) browsr.getBrowsrScreen().getRootpane();
        rootPane = new LeafPane(rootPane.getX(), rootPane.getY(), rootPane.getWidth(),rootPane.getHeight(),
                rootPane.getDoc(), rootPane.getParent(), rootPane.getScreen());
        assertNull(rootPane.getParent());
        Contentspan homepage = controller.getstartpage();
        browsr.handleMouseEvent(501, 120, 150, 1, 1, 1024);
        assertNotEquals(rootPane.getContentspan(), homepage);
        // Delete pane and replace with new pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        assertNotEquals(rootPane, browsr.getBrowsrScreen().getRootpane());
        assertEquals("Welcome to Browsr!", ((TextComponent) ((TableComponent) ((LeafPane) browsr.getBrowsrScreen().getRootpane()).getDoc()
         .getComponent()).getRows().get(0).getCells().get(0)).getText());
    }

    @Test
    public void simpleDeleteTest() {
        //split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        rootPane = new NonLeafPane(rootPane.getX(), rootPane.getY(), rootPane.getWidth(), rootPane.getHeight(),
                rootPane.getScreen(), rootPane.getPanes(), rootPane.getParent(), rootPane.getSplitType());
        assertTrue(rootPane.getPanes().get(0).isFocused());
        // delete one pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof LeafPane);
        assertNull(((LeafPane) browsr.getBrowsrScreen().getRootpane()).getParent());
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertEquals(rootPane.getPanes().size(), 1);
        // test get contentspan
        rootPane.getPanes().remove(0);
        assertNull(rootPane.getContentspan());
    }

    @Test
    public void mediumDeleteTest() {
        //split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        //split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);

        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertEquals(rootPane.getSplitType(), 0);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        LeafPane oldLeaf = (LeafPane) rootPane.getPanes().get(1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        rootPane.getPanes().get(1).setFocused(true);
        ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0).setFocused(false);

        // delete the oldest leafpane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        assertFalse(rootPane.getPanes().contains(oldLeaf));
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        //the new rootpane is now a resized version of oldNonLeaf.
        assertNotEquals(rootPane, browsr.getBrowsrScreen().getRootpane());
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(0) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        //check if position is right
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getPanes().get(0).getY() + rootPane.getPanes().get(0).getHeight(),
                rootPane.getPanes().get(1).getY());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight() / 2, rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());
    }

    @Test
    // Tests a few cases of the shiftpanes method.
    public void complexDeleteTest() {
        //split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);
        //split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        //split pane some more
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);
        //split pane even more
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);

        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        // check if all the panes have split correctly first.
        assertEquals(rootPane.getSplitType(), 0);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getSplitType(), 0);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(0))
                .getSplitType(), 1);

        // Youngest nonleafpane.
        NonLeafPane nonLeafPane1 = ((NonLeafPane) ((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0))
                .getPanes().get(0));
        // His parent.
        NonLeafPane nonLeafPane2 = (NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0);
        assertSame(nonLeafPane1.getParent(), nonLeafPane2);

        // Now delete one of the youngest leafpanes
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        assertEquals(nonLeafPane1.getPanes().size(), 1);
        // Nonleafpane 2 should have two leafpanes as child now.
        assertFalse(nonLeafPane2.getPanes().contains(nonLeafPane1));
        assertTrue(nonLeafPane2.getPanes().get(0) instanceof LeafPane);
        assertTrue(nonLeafPane2.getPanes().get(1) instanceof LeafPane);

        // Check positioning
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(0).getX());
        assertEquals(nonLeafPane2.getPanes().get(1).getX() - nonLeafPane2.getPanes().get(1).getWidth(), nonLeafPane2.getX());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(0).getY());
        assertEquals(nonLeafPane2.getPanes().get(1).getY(), nonLeafPane2.getY());
        assertEquals(nonLeafPane2.getWidth() / 2, nonLeafPane2.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane2.getPanes().get(1).getWidth(), nonLeafPane2.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane2.getHeight(), nonLeafPane2.getPanes().get(0).getHeight());
        assertEquals(nonLeafPane2.getPanes().get(1).getHeight(), nonLeafPane2.getPanes().get(0).getHeight());

        // Now delete oldest leafpane and shift everything.
        // Change focus first.
        browsr.handleMouseEvent(501, 750, 150, 1, 1, 1024);

        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane should have changed.
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // Dimension should still be the same
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();

        // Check pane hierarchy.
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 0);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);

        // Check positioning of oldest panes.
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY() - rootPane.getPanes().get(1).getHeight());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight() / 2, rootPane.getPanes().get(0).getHeight());
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((NonLeafPane) rootPane.getPanes().get(0)).getParent(), ((LeafPane) rootPane.getPanes().get(1)).getParent());
        assertSame(rootPane, ((LeafPane) rootPane.getPanes().get(1)).getParent());

        // Check positioning of younger panes.
        NonLeafPane nonLeafPane3 = (NonLeafPane) rootPane.getPanes().get(0);
        assertEquals(nonLeafPane3.getX(), nonLeafPane3.getPanes().get(0).getX());
        assertEquals(nonLeafPane3.getX(), nonLeafPane3.getPanes().get(1).getX() - nonLeafPane3.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane3.getY(), nonLeafPane3.getPanes().get(0).getY());
        assertEquals(nonLeafPane3.getY(), nonLeafPane3.getPanes().get(1).getY());
        assertEquals(nonLeafPane3.getWidth() / 2 - 1, nonLeafPane3.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane3.getPanes().get(0).getWidth(), nonLeafPane3.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane3.getHeight(), nonLeafPane3.getPanes().get(0).getHeight());
        assertEquals(nonLeafPane3.getPanes().get(0).getHeight(), nonLeafPane3.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) nonLeafPane3.getPanes().get(0)).getParent(), ((LeafPane) nonLeafPane3.getPanes().get(1)).getParent());
        assertSame(nonLeafPane3, ((LeafPane) nonLeafPane3.getPanes().get(1)).getParent());
    }

    @Test
    // Tests some other cases of the shiftpanes method.
    public void complexDeleteTestTwo() {
        // Split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane some more
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Change focus
        browsr.handleMouseEvent(501,
                ((NonLeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0)).getPanes().get(1).getX() + 25,
                ((NonLeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0)).getPanes().get(1).getY() + 25,
                1, 1, 1024);
        // Split pane even more
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Change focus
        browsr.handleMouseEvent(501,
                ((NonLeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0)).getPanes().get(1).getX() + 25,
                ((NonLeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0)).getPanes().get(1).getY() + 625,
                1, 1, 1024);
        // Split pane one last time
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Check if pane hierarchy is correct
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertEquals(rootPane.getSplitType(), 0);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 0);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof NonLeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getSplitType(), 1);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getSplitType(), 1);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(1)).getSplitType(), 1);
        assertTrue(((NonLeafPane) ((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Now delete oldest leafpane and see what happens
        // First change focus
        browsr.handleMouseEvent(501, rootPane.getPanes().get(1).getX() + 25, 150, 1, 1, 1024);
        // Delete pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane should have changed
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // They should still have the same dimensions though
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);

        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        // Check new hierarchy
        assertEquals(rootPane.getSplitType(), 0);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 1);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(1)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1)).getSplitType(), 1);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Check position of oldest panes
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX() - rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth() * 2 + 2);
        assertEquals(rootPane.getPanes().get(0).getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight(), rootPane.getPanes().get(0).getHeight());
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check parents
        assertNull(rootPane.getParent());
        assertSame(rootPane, ((NonLeafPane) rootPane.getPanes().get(0)).getParent());
        assertSame(rootPane, ((NonLeafPane) rootPane.getPanes().get(1)).getParent());

        // Check position of younger panes, if it is correct then the panes in the middle will be as well.
        NonLeafPane nonLeafPane1 = (NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1);
        assertEquals(nonLeafPane1.getX(), nonLeafPane1.getPanes().get(0).getX());
        assertEquals(nonLeafPane1.getX(), nonLeafPane1.getPanes().get(1).getX());
        assertEquals(nonLeafPane1.getY(), nonLeafPane1.getPanes().get(0).getY());
        assertEquals(nonLeafPane1.getY(), nonLeafPane1.getPanes().get(1).getY() - nonLeafPane1.getPanes().get(1).getHeight());
        assertEquals(nonLeafPane1.getWidth(), nonLeafPane1.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane1.getPanes().get(0).getWidth(), nonLeafPane1.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane1.getHeight(), nonLeafPane1.getPanes().get(0).getHeight() * 2 + 1);
        assertEquals(nonLeafPane1.getPanes().get(0).getHeight(), nonLeafPane1.getPanes().get(1).getHeight());

        // Check parents
        assertSame(nonLeafPane1.getParent(), rootPane.getPanes().get(1));
        assertSame(nonLeafPane1, ((LeafPane) nonLeafPane1.getPanes().get(0)).getParent());
        assertSame(nonLeafPane1, ((LeafPane) nonLeafPane1.getPanes().get(1)).getParent());

        // Change focus
        browsr.handleMouseEvent(501, 150, 150, 1, 1, 1024);
        // Remove one more panes
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Rootpane should be the same
        assertSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // Remove one more pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane should have changed
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());

        // Check dimensions
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());

        // Check new hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(0) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(1)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Check position of old panes
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY() - rootPane.getPanes().get(1).getHeight());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth() + 2);
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(1).getWidth() + 2);
        assertEquals(rootPane.getHeight() / 2, rootPane.getPanes().get(0).getHeight());
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) rootPane.getPanes().get(0)).getParent(), ((NonLeafPane) rootPane.getPanes().get(1)).getParent());
        assertSame(rootPane, ((LeafPane) rootPane.getPanes().get(0)).getParent());
        assertNull(rootPane.getParent());

        // Check positioning of younger panes.
        NonLeafPane nonLeafPane2 = (NonLeafPane) rootPane.getPanes().get(1);
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(0).getX());
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(1).getX());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(0).getY());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(1).getY() - nonLeafPane2.getPanes().get(1).getHeight());
        assertEquals(nonLeafPane2.getWidth(), nonLeafPane2.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane2.getPanes().get(0).getWidth(), nonLeafPane2.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane2.getHeight(), nonLeafPane2.getPanes().get(0).getHeight() * 2 + 1);
        assertEquals(nonLeafPane2.getPanes().get(0).getHeight(), nonLeafPane2.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) nonLeafPane2.getPanes().get(0)).getParent(), ((LeafPane) nonLeafPane2.getPanes().get(1)).getParent());
        assertSame(nonLeafPane2, ((LeafPane) nonLeafPane2.getPanes().get(1)).getParent());
        assertSame(rootPane, nonLeafPane2.getParent());
    }

    @Test
    // Tests some other easy cases of the shiftpanes function
    public void mediumDeleteTestTwo() {
        // Split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 0);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);

        // Change focus
        browsr.handleMouseEvent(501, 150, 750, 1, 1, 1024);

        // Remove one pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane has changed
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // Check dimensions
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 0);
        assertTrue(rootPane.getPanes().get(0) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);

        // Check positioning
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX() - rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth() * 2);
        assertEquals(rootPane.getPanes().get(0).getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight(), rootPane.getPanes().get(0).getHeight());
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) rootPane.getPanes().get(0)).getParent(), ((LeafPane) rootPane.getPanes().get(1)).getParent());
        assertSame(rootPane, ((LeafPane) rootPane.getPanes().get(0)).getParent());
    }

    @Test
    // Tests other easy cases of the shiftpanes method
    public void mediumDeleteTestThree() {
        // Split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);

        // Change focus
        browsr.handleMouseEvent(501, 150, 750, 1, 1, 1024);

        // Remove one pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane has changed
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // Check dimensions
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(0) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);

        // Check positioning
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY() - rootPane.getPanes().get(1).getHeight());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth());
        assertEquals(rootPane.getPanes().get(0).getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight(), rootPane.getPanes().get(0).getHeight() * 2 + 2);
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) rootPane.getPanes().get(0)).getParent(), ((LeafPane) rootPane.getPanes().get(1)).getParent());
        assertSame(rootPane, ((LeafPane) rootPane.getPanes().get(0)).getParent());
    }

    @Test
    // Tests some more advanced cases of the shiftpanes method
    public void complexDeleteTestThree() {
        // Split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane some more
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Change focus
        browsr.handleMouseEvent(501, 750, 150, 1, 1, 1024);
        // Split pane one more time
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 0);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getSplitType(), 0);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Change focus
        browsr.handleMouseEvent(501, 150, 750, 1, 1, 1024);

        // Remove one pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane has changed
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // Check dimensions
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 0);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 1);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(1)).getSplitType(), 0);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Check positioning of oldest panes
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX() - rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth() * 2);
        assertEquals(rootPane.getPanes().get(0).getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight(), rootPane.getPanes().get(0).getHeight());
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((NonLeafPane) rootPane.getPanes().get(0)).getParent(), ((NonLeafPane) rootPane.getPanes().get(1)).getParent());
        assertSame(rootPane, ((NonLeafPane) rootPane.getPanes().get(0)).getParent());

        // Check youngest panes
        NonLeafPane nonLeafPane1 = (NonLeafPane) rootPane.getPanes().get(0);
        assertEquals(nonLeafPane1.getX(), nonLeafPane1.getPanes().get(0).getX());
        assertEquals(nonLeafPane1.getX(), nonLeafPane1.getPanes().get(1).getX());
        assertEquals(nonLeafPane1.getY(), nonLeafPane1.getPanes().get(0).getY());
        assertEquals(nonLeafPane1.getY(), nonLeafPane1.getPanes().get(1).getY() - nonLeafPane1.getPanes().get(1).getHeight());
        assertEquals(nonLeafPane1.getWidth(), nonLeafPane1.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane1.getPanes().get(0).getWidth(), nonLeafPane1.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane1.getHeight(), nonLeafPane1.getPanes().get(0).getHeight() * 2 + 2);
        assertEquals(nonLeafPane1.getPanes().get(0).getHeight(), nonLeafPane1.getPanes().get(1).getHeight());

        // Check parents
        assertSame(nonLeafPane1.getParent(), rootPane);
        assertSame(nonLeafPane1, ((LeafPane) nonLeafPane1.getPanes().get(0)).getParent());
        assertSame(nonLeafPane1, ((LeafPane) nonLeafPane1.getPanes().get(1)).getParent());

        // Check positioning of other youngest panes.
        NonLeafPane nonLeafPane2 = (NonLeafPane) rootPane.getPanes().get(1);
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(0).getX());
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(1).getX() - nonLeafPane2.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(0).getY());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(1).getY());
        assertEquals(nonLeafPane2.getWidth(), nonLeafPane2.getPanes().get(0).getWidth() * 2 + 1);
        assertEquals(nonLeafPane2.getPanes().get(0).getWidth(), nonLeafPane2.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane2.getHeight(), nonLeafPane2.getPanes().get(0).getHeight());
        assertEquals(nonLeafPane2.getPanes().get(0).getHeight(), nonLeafPane2.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) nonLeafPane2.getPanes().get(0)).getParent(), ((LeafPane) nonLeafPane2.getPanes().get(1)).getParent());
        assertSame(nonLeafPane2, ((LeafPane) nonLeafPane2.getPanes().get(1)).getParent());
        assertSame(rootPane, nonLeafPane2.getParent());
    }

    @Test
    // Tests the remaining advanced cases of the shiftpanes method
    public void complexDeleteTestFour() {
        // Split pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane again
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Split pane some more
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,86,'v',128);
        browsr.handleKeyEvent(402,17,' ',128);
        // Change focus
        browsr.handleMouseEvent(501, 150, 450, 1, 1, 1024);
        // Split pane one more time
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        NonLeafPane rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(1) instanceof LeafPane);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getSplitType(), 1);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getSplitType(), 0);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) ((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Change focus
        browsr.handleMouseEvent(501, 150, 750, 1, 1, 1024);

        // Remove one pane
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,88,'x',128);
        browsr.handleKeyEvent(402,17,' ',128);

        // Rootpane has changed
        assertNotSame(rootPane, browsr.getBrowsrScreen().getRootpane());
        // Check dimensions
        assertEquals(rootPane.getHeight(), browsr.getBrowsrScreen().getRootpane().getHeight());
        assertEquals(rootPane.getWidth(), browsr.getBrowsrScreen().getRootpane().getWidth());

        // Check hierarchy
        assertTrue(browsr.getBrowsrScreen().getRootpane() instanceof NonLeafPane);
        rootPane = (NonLeafPane) browsr.getBrowsrScreen().getRootpane();
        assertNull(rootPane.getParent());
        assertEquals(rootPane.getSplitType(), 1);
        assertTrue(rootPane.getPanes().get(0) instanceof NonLeafPane);
        assertTrue(rootPane.getPanes().get(1) instanceof NonLeafPane);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(0)).getSplitType(), 1);
        assertEquals(((NonLeafPane) rootPane.getPanes().get(1)).getSplitType(), 0);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(0)).getPanes().get(1) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(0) instanceof LeafPane);
        assertTrue(((NonLeafPane) rootPane.getPanes().get(1)).getPanes().get(1) instanceof LeafPane);

        // Check positioning of oldest panes
        assertEquals(rootPane.getX(), rootPane.getPanes().get(0).getX());
        assertEquals(rootPane.getX(), rootPane.getPanes().get(1).getX());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(0).getY());
        assertEquals(rootPane.getY(), rootPane.getPanes().get(1).getY() - rootPane.getPanes().get(1).getHeight());
        assertEquals(rootPane.getWidth(), rootPane.getPanes().get(0).getWidth());
        assertEquals(rootPane.getPanes().get(0).getWidth(), rootPane.getPanes().get(1).getWidth());
        assertEquals(rootPane.getHeight(), rootPane.getPanes().get(0).getHeight() * 2 + 2);
        assertEquals(rootPane.getPanes().get(0).getHeight(), rootPane.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((NonLeafPane) rootPane.getPanes().get(0)).getParent(), ((NonLeafPane) rootPane.getPanes().get(1)).getParent());
        assertSame(rootPane, ((NonLeafPane) rootPane.getPanes().get(0)).getParent());

        // Check youngest panes
        NonLeafPane nonLeafPane1 = (NonLeafPane) rootPane.getPanes().get(0);
        assertEquals(nonLeafPane1.getX(), nonLeafPane1.getPanes().get(0).getX());
        assertEquals(nonLeafPane1.getX(), nonLeafPane1.getPanes().get(1).getX());
        assertEquals(nonLeafPane1.getY(), nonLeafPane1.getPanes().get(0).getY());
        assertEquals(nonLeafPane1.getY(), nonLeafPane1.getPanes().get(1).getY() - nonLeafPane1.getPanes().get(1).getHeight());
        assertEquals(nonLeafPane1.getWidth(), nonLeafPane1.getPanes().get(0).getWidth());
        assertEquals(nonLeafPane1.getPanes().get(0).getWidth(), nonLeafPane1.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane1.getHeight(), nonLeafPane1.getPanes().get(0).getHeight() * 2 + 2);
        assertEquals(nonLeafPane1.getPanes().get(0).getHeight(), nonLeafPane1.getPanes().get(1).getHeight());

        // Check parents
        assertSame(nonLeafPane1.getParent(), rootPane);
        assertSame(nonLeafPane1, ((LeafPane) nonLeafPane1.getPanes().get(0)).getParent());
        assertSame(nonLeafPane1, ((LeafPane) nonLeafPane1.getPanes().get(1)).getParent());

        // Check positioning of other youngest panes.
        NonLeafPane nonLeafPane2 = (NonLeafPane) rootPane.getPanes().get(1);
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(0).getX());
        assertEquals(nonLeafPane2.getX(), nonLeafPane2.getPanes().get(1).getX() - nonLeafPane2.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(0).getY());
        assertEquals(nonLeafPane2.getY(), nonLeafPane2.getPanes().get(1).getY());
        assertEquals(nonLeafPane2.getWidth(), nonLeafPane2.getPanes().get(0).getWidth() * 2);
        assertEquals(nonLeafPane2.getPanes().get(0).getWidth(), nonLeafPane2.getPanes().get(1).getWidth());
        assertEquals(nonLeafPane2.getHeight(), nonLeafPane2.getPanes().get(0).getHeight());
        assertEquals(nonLeafPane2.getPanes().get(0).getHeight(), nonLeafPane2.getPanes().get(1).getHeight());

        // Check Parents.
        assertSame(((LeafPane) nonLeafPane2.getPanes().get(0)).getParent(), ((LeafPane) nonLeafPane2.getPanes().get(1)).getParent());
        assertSame(nonLeafPane2, ((LeafPane) nonLeafPane2.getPanes().get(1)).getParent());
        assertSame(rootPane, nonLeafPane2.getParent());
    }
}
