package usecases;
import domain.DocumentController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.component.TableComponent;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.NonLeafPane;
import ui.window.panes.Pane;
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

public class SelectFrame {
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
    void selectFrameTest(){
        browsr.handleKeyEvent(401,17,' ',128);
        browsr.handleKeyEvent(402,72,'h',128);
        Pane left = ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(0);
        Pane right = ((NonLeafPane) browsr.getBrowsrScreen().getRootpane()).getPanes().get(1);
        assertEquals(false,right.isFocused());
        browsr.handleMouseEvent(MouseEvent.MOUSE_CLICKED,700,700,1,1,1024);
        assertEquals(true,right.isFocused());
        assertEquals(false,left.isFocused());





    }




}
