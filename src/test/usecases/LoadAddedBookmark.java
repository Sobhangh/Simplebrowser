package usecases;

import domain.DocumentController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.screens.AddToBookMarkScreen;
import ui.window.screens.BrowsrScreen;
import ui.window.screens.SaveAsScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoadAddedBookmark {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);



    @BeforeEach
    void init(){
        browsr.setActive(true);
        browsr.setMetrics(metrics);
        BrowsrScreen s = new BrowsrScreen(1000, 1000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    void AddBookmarkTest() throws IOException {
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        //1.
        //Simulate ctrl + d
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,68,'d',128);
        //2.
        //Check if the current screen is the AddToBookMarkScreen
        assertTrue(browsr.getCurrent() instanceof AddToBookMarkScreen);
        assertEquals("Add Bookmark",browsr.getCurrent().getTitle());

        //3.
        //Check if the inputs of the AddToBookMarkScreen register keystrokes and mouseclicks properly
        browsr.handleKeyEvent(402,17,' ',128);
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,245,27,1,1,1024);
        browsr.handleKeyEvent(401,68,'d',128);
        assertTrue(browsr.getCurrent().getEditMode());
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,245,83,1,1,1024);
        browsr.handleKeyEvent(401,68,'d',128);
        assertTrue(browsr.getCurrent().getEditMode());
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,245,527,1,1,1024);
        assertTrue(!browsr.getCurrent().getEditMode());

        AddToBookMarkScreen s = (AddToBookMarkScreen) browsr.getCurrent();
        assertEquals("d", s.getName());
        assertEquals("d", s.getUrl());

        //4.
        //Check if the add bookmark button returns the browst to the main screen
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED,196,140,1,1,1024);
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED,196,140,1,1,1024);
        assertEquals("Browsr", browsr.getCurrent().getTitle());


        //5.
        //Check if the correct bookmark has been added to the bookmark
        assertEquals(1, browsr.getBrowsrScreen().getBookmarkbar().getAmountOfBookmarks());
        assertEquals("d", browsr.getBrowsrScreen().getBookmarkbar().getBookmarkAtIndex(0).getName());
        assertEquals("d", browsr.getBrowsrScreen().getBookmarkbar().getBookmarkAtIndex(0).getLink().getHref());



    }

    @Test
    void LoadBookmarkTest(){
        String bookmark = "https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html";
        //1.
        //Add Bookmark and Click it
        browsr.getBrowsrScreen().getBookmarkbar().addBookmark("test", bookmark);
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED,6,60,1,1,1024);
        //3.
        assertEquals(bookmark, browsr.getBrowsrScreen().getAddressbar().getUrl());

    }
}
