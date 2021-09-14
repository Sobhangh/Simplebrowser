package ui.window.screens;

import domain.DocumentController;
import ui.window.BrowsrView;

import java.awt.*;

public abstract class Screen {
    /**
     * Title of screen
     */
    private String title;
    /**
     * Editmode of screen
     */
    private boolean editMode;
    /**
     * Width of screen
     */
    private int width;
    /**
     * Height of screen
     */
    private int height;
    /**
     * FontMetrics of screen
     */
    private FontMetrics metrics;
    /**
     * Controller
     */
    private DocumentController controller;
    /**
     * Context
     */
    private BrowsrView context;

    /**
     * Constructor for the Screen class
     * @param title title of the screen
     * @param width width of the screen
     * @param height height of the screen
     * @param metrics metrics of the screen
     * @param controller controller used by screen
     * @param context context of screen
     */
    public Screen(String title, int width, int height, FontMetrics metrics, DocumentController controller, BrowsrView context){
        setTitle(title);
        setMetrics(metrics);
        setController(controller);
        this.height = height;
        this.width= width;
        init();
        setEditMode(false);
        setContext(context);
    }

    abstract void init();
    public abstract void paint(Graphics g);
    public abstract void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx);
    public abstract void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx);

    /**
     * Getter for edit mode of screen
     * @return edit mode of screen
     */
    public boolean getEditMode(){
        return this.editMode;
    }

    /**
     * Setter for edit mode of screen
     * @param editMode
     */
    public void setEditMode(boolean editMode){
        this.editMode = editMode;
    }

    /**
     * Getter for width of screen
     * @return width of screen
     */
    public int getWidth(){ return this.width; }

    /**
     * Setter for width of screen
     * @param width
     */
    public void setWidth(int width){ this.width = width; }

    /**
     * Getter for height of screen
     * @return height of the screen
     */
    public int getHeight(){ return this.height; }

    /**
     * Setter for height of screen
     * @param height
     */
    public void setHeight(int height){ this.height = height; }

    /**
     * Getter for metrics of screen
     * @return metrics of screen
     */
    public FontMetrics getMetrics(){ return this.metrics; }

    /**
     * Setter for metrics of screen
     * @param metrics
     */
    public void setMetrics(FontMetrics metrics){ this.metrics = metrics; }

    /**
     * Getter for controller being used by screen
     * @return controller
     */
    public DocumentController getController(){ return this.controller; }

    /**
     * Setter for controller used by screen
     * @param controller
     */
    public void setController(DocumentController controller){ this.controller = controller; }

    /**
     * Getter for context of screen
     * @return context of screen
     */
    public BrowsrView getContext(){ return this.context; }

    /**
     * Setter for context of screen
     * @param context
     */
    public void setContext(BrowsrView context){ this.context = context; }

    /**
     * Getter for title of screen
     * @return title of screen
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title of screen
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
