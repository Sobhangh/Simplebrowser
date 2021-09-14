package usecases;

import domain.DocumentController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.screens.BrowsrScreen;
import ui.window.screens.SaveAsScreen;
import ui.window.screens.Screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SaveDocument {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    Canvas c = new Canvas();
    FontMetrics metrics = c.getFontMetrics(font);
    DocumentController controller = new DocumentController();
    BrowsrView browsr = new BrowsrView(controller);



    @BeforeEach
    void init(){
        browsr.setMetrics(metrics);
        BrowsrScreen s = new BrowsrScreen(1000, 1000, metrics, controller, browsr);
        browsr.setBrowsr(s);
        browsr.setCurrent(s);
        browsr.setPanel();
    }

    @Test
    void saveDocumentTest() throws IOException {
        //1.
        //what should the keychar be??
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        LeafPane left = (LeafPane) ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,83,'s',128);
        //2.
        assertTrue(browsr.getCurrent() instanceof SaveAsScreen);
        assertEquals("Save As",browsr.getCurrent().getTitle());

        //3.
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,152,12,1,1,1024);
        assertTrue(browsr.getCurrent().getEditMode());

        //5.
        SaveAsScreen s = (SaveAsScreen) browsr.getCurrent();
        browsr.handleKeyEvent(401, 76, 'l', 0);
        browsr.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(s.getFilename().getInput(),"la");

        browsr.handleKeyEvent(401, 8, '\u0000', 0);
        assertEquals(s.getFilename().getInput(),"l");

        //click outside the input field
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,148,1,1,1,1024);
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,152,12,1,1,1024);
        assertEquals(s.getFilename().getInputAtFocus(),"l");
        browsr.handleKeyEvent(401, 65, 'a', 0);
        assertEquals(s.getFilename().getInput(),"a");

        //7.
        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED,241,88,1,1,1024);
        assertTrue(s.getSaveButton().isClicked());
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED,241,88,1, 1, 1024);

        //8.
        String html = "<table><tr><td>Welcome to Browsr!<tr><td>This is a valid link:<tr><td><a href=\"https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html\">https://people.cs.kuleuven.be/~bart.jacobs/browsrtest.html</a><tr><td><a href=\"https://people.cs.kuleuven.be/~bart.jacobs/swop/browsrformtest.html\">https://people.cs.kuleuven.be/~bart.jacobs/swop/browsrformtest.html</a><tr><td><a href=\"https://people.cs.kuleuven.be/~bart.jacobs/swop/rowsrormest.html\">https://people.cs.kuleuven.be/~bart.jacobs/swop/rowsrormest.html</a></table>";
        Path workingDir = Path.of("", "src/");
        Path file = workingDir.resolve("a.html");
        String content = Files.readString(file);
        assertEquals(html, content);

        assertTrue(browsr.getCurrent() instanceof BrowsrScreen);
        assertEquals("Browsr",browsr.getCurrent().getTitle());
    }

    @Test
    void cancelButtonClicked(){
        //1.
        //what should the keychar be??
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(401,83,'s',128);

        //3.
        SaveAsScreen s = (SaveAsScreen) browsr.getCurrent();
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,152,12,1,1,1024);
        browsr.handleKeyEvent(401, 76, 'l', 0);
        assertEquals(s.getFilename().getInput(),"l");

        browsr.handleMouseEvent(MouseEvent.MOUSE_PRESSED,370,80,1,1,1024);
        assertTrue(s.getCancelButton().isClicked());
        browsr.handleMouseEvent(MouseEvent.MOUSE_RELEASED,370,80,1, 1, 1024);

        File tempFile = new File("src/l.html");
        assertEquals(false,tempFile.exists());

        assertTrue(browsr.getCurrent() instanceof BrowsrScreen);
        assertEquals("Browsr",browsr.getCurrent().getTitle());

    }
}
