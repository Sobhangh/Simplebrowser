package ui.window;

import domain.Contentspan;
import domain.DocumentController;
import ui.window.screens.AddToBookMarkScreen;
import ui.window.screens.BrowsrScreen;
import ui.window.screens.SaveAsScreen;
import ui.window.screens.Screen;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Context class used by all screens
 */
public class BrowsrView extends CanvasWindow {
    /**
     * Font used in Browsr screens
     */
    private final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 22);
    /**
     * Current screen
     */
    private Screen current;
    /**
     * Boolean indicating if CTRL is being pressed
     */
    private boolean ctrlPressed;

    /**
     * Boolean indicating if the browsr window is active.
     */
    private boolean active;
    /**
     * Fontmetrics of window
     */
    private FontMetrics metrics;
    /**
     * Browsr screen
     */
    private BrowsrScreen browsr;
    /**
     * Controller used by all screens
     */
    private final DocumentController controller;

    /**
     * Constructor for the BrowsrView class
     * @param controller controller used by all screens
     */
    public BrowsrView(DocumentController controller){
        super("Browsr");
        this.controller = controller;
        this.active = true;
    }

    @Override
    public void handleShown() {
        this.metrics = getFontMetrics(font);
        this.browsr = new BrowsrScreen(getWidth(), getHeight(), metrics, controller, this);
        this.current = browsr;
        //setActive(true);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setFont(font);
        current.paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if (isActive() || !current.equals(browsr)) {
            current.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
            handleEditMode(getEditMode());
            repaint();
        }
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        if (isActive() || !current.equals(browsr))
            this.current.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        if (id == 401 && keyCode == 17) {
            this.ctrlPressed = true;
        }
        else if (id == 402 && keyCode == 17) {
            this.ctrlPressed = false;
        }
        //SHOULD AN EVENTLISTER BE USED WHEN A FRAME IS FOCUSED OR WE CAN UPDATE THE URL ONLY WHEN WE NEED TO SAVE OR BOOKMARK?
        //SHOULD THERE BE A CONTENTSPAN FOR EACH FRAME OR ONE GENERAL IS OK?
        else if (id == 401 && keyCode == 68 && ctrlPressed) {
            if(current.equals(browsr)){
                setActive(false);

                String url = browsr.getRootpane().getUrl();
                // controller.updateURL(url);
                this.current = new AddToBookMarkScreen(url, getWidth(), getHeight(), metrics, controller, this);
                try {
                    setTitle(this.current.getTitle());
                }catch (NullPointerException e){

                }
            }
        }
        else if (id == 401 && keyCode == 83 && ctrlPressed) {
            if(current.equals(browsr)){
                setActive(false);
                String url = browsr.getRootpane().getUrl();
                Contentspan c =browsr.getRootpane().getContentspan();
                controller.setContentSpan(c);
                // controller.updateURL(url);
                //what will happen if it is the homepage?
                this.current = new SaveAsScreen(getWidth(), getHeight(), metrics, controller, this);
                try {
                    setTitle(this.current.getTitle());
                }catch (NullPointerException e){

                }
            }
        }
        handleEditMode(getEditMode());
        repaint();
    }

    @Override
    public void handleResize() {
        setWidth(this.getWidth());
        setHeight(this.getHeight());
        browsr.setWidthBrowsr(this.getWidth());
        browsr.setHeightBrowsr(this.getHeight());
        repaint();
    }


    /**
     * Setter for width of current and browsr screen
     * @param width
     */
    public void setWidth(int width){
        this.current.setWidth(width);
        this.browsr.setWidth(width);
    }

    /**
     * Setter for the height of the current and browsr screen
     * @param height
     */
    public void setHeight(int height){
        current.setHeight(height);
        browsr.setHeight(height);
    }

    /**
     * Getter for editmode of current screen
     * @return editmode of current screen
     */
    public boolean getEditMode(){
        return current.getEditMode();
    }

    /**
     * @return browsr screen
     */
    public BrowsrScreen getBrowsrScreen() {
        return browsr;
    }

    /**
     * Method to set current screen back to browsr screen
     */
    public void backToBrowsr(){
        this.current = browsr;
        setActive(true);
        try {
            setTitle(this.current.getTitle());
        }catch (NullPointerException e){

        }
    }

    /**
     * Method to handle the change of cursor icon when current screen changes edit mode
     * @param editMode
     */
    private void handleEditMode(Boolean editMode){
        if(editMode){
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        } else {
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Setter for Browsr screen
     * @param browsr
     */
    public void setBrowsr(BrowsrScreen browsr) {
        this.browsr = browsr;
    }

    /**
     * Setter for current screen
     * @param current
     */
    public void setCurrent(Screen current) {
        this.current = current;
    }

    /**
     * Getter for the current screen
     * @return
     */
    public Screen getCurrent() {
        return current;
    }

    /**
     * Setter for the panel of this window
     */
    public void setPanel(){
        this.panel = new Panel();

    }

    /**
     * Setter for metrics of this window
     * @param metrics
     */
    public void setMetrics(FontMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Getter for active state of browsr window.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for active state of browsr window.
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
