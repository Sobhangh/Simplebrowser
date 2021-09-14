package ui.window.panes;

import domain.Contentspan;
import ui.listeners.EditListener;
import ui.listeners.LinkClickedListener;
import ui.window.screens.BrowsrScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Pane {
    /**
     * x coordinate left of pane
     */
    private int x;

    /**
     * y coordinate top of pane
     */
    private int y;

    /**
     * Width of pane
     */
    private int width;

    /**
     * Height of pane
     */
    private int height;

    /**
     * BrowsrScreen that renders the pane.
     */
    BrowsrScreen screen;

    /**
     * Focused pane or not
     */
    boolean focused;

    /**
     * Getter for the ratio of the pane
     * @return The ratio of the pane
     */
    public double getRatio() {
        return ratio;
    }

    /**
     * Setter for the ratio of the pane
     * @param ratio
     */
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    /**
     * The ratio of the pane
     */
    private double ratio;


    /**
     * Constructor for the Pane class
     * @param x         The x-coordinate of the pane
     * @param y         The y-coordinate of the pane
     * @param width     The width of the pane
     * @param height    The height of the pane
     * @param screen    The browsrScreen of the pane (it is the same for all the panes)
     */
    public Pane (int x, int y, int width, int height, BrowsrScreen screen) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.screen = screen;
    }

    /**
     * Getter for the focused boolean
     * @return focused
     */
    public boolean isFocused() {
        return focused;
    }

    /**
     * Setter for the focused boolean
     * @param focused
     */
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    /**
     * Getter for the BrowsrScreen of the pane
     * @return The BrowsrScreen of the pane
     */
    public BrowsrScreen getScreen() {
        return this.screen;
    }
    /**
     * Method that draws the component on the appropriate location
     * @param g
     */
    public abstract void paint(Graphics g);

    /**
     * Getter for the url of the pane
     * @return The url string of the pane
     */
    public abstract String getUrl();

    /**
     * Setter for the url of the pane
     * @param url
     */
    public abstract void setUrl(String url);

    /**
     * Updates the contentspan of the pane
     * @param c     The new contentspan
     * @param m     The fontmetrics
     */
    public abstract void update(Contentspan c,FontMetrics m);

    /**
     * Getter for the contentspan of the pane
     * @return  The contentspan of the pane
     */
    public abstract Contentspan getContentspan();


    /**
     * Method that handles the behavior the pane should have at mouse events
     * @param id
     * @param x
     * @param y
     * @param clickCount
     * @param button
     * @param modifiersEx
     */
    public abstract void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx);

    /**
     * Method that handles the behavior the pane should have at key events
     * @param id
     * @param keyCode
     * @param keyChar
     * @param modifiersEx
     */
    public abstract void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx);

    /**
     * Getter X coordinate
     * @return X coordinate of pane
     */
    public int getX(){
        return this.x;
    }

    /**
     * Setter X coordinate
     * @param x
     * @throws IllegalArgumentException if given x coordinate is negative
     */
    public void setX(int x){
        if(x < 0){
            throw new IllegalArgumentException("X coordinate cannot be negative");
        }
        this.x = x;
    }

    /**
     * Getter Y coordinate
     * @return Y coordinate of pane
     */
    public int getY(){
        return this.y;
    }

    /**
     * Setter Y coordinate
     * @param y
     * @throws IllegalArgumentException if given y coordinate is negative
     */
    public void setY(int y){
        if(y < 0){
            throw new IllegalArgumentException("Y coordinate cannot be negative");
        }
        this.y = y;
    }

    /**
     * Getter width
     * @return width of pane
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Setter width
     * @param width
     * @throws IllegalArgumentException if the given width is negative
     */
    public void setWidth(int width){
        if(width < 0){
            throw new IllegalArgumentException("width cannot be negative");
        }
        this.width = width;
    }

    /**
     * Returns if the given x and y coordinates are a position in the pane
     * @param x
     * @param y
     * @return boolean indicating if given (x, y) coordinate are positioned in pane
     */
    public boolean isPositionInComponent(int x, int y){
        return x > getX() && y > getY() && (x < getX() + getWidth()) && (y < getY() + getHeight());
    }

    /**
     * Getter height
     * @return height of pane
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * Setter height
     * @param height
     * @throws IllegalArgumentException if the given height is negative
     */
    public void setHeight(int height){
        if(height < 0){
            throw new IllegalArgumentException("height cannot be negative");
        }
        this.height = height;
    }

    /**
     * Getter for the LinkClickedListeners
     * @return  The LinkClickedListeners
     */
    public List<LinkClickedListener> getLinkClickedListeners() {
        return linkClickedListeners;
    }

    /**
     * List containing all the LinkClickedListener
     */
    private final List<LinkClickedListener> linkClickedListeners = new ArrayList<>();

    /**
     * Getter for the EditListeners
     * @return  The EditListeners
     */
    public List<EditListener> getEditListeners() {
        return editListeners;
    }

    /**
     * List containing all the EditListeners
     */
    private final List<EditListener> editListeners = new ArrayList<>();

    /**
     * Register a linkClickedlistener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     */
    public void addLinkClickedListener(LinkClickedListener listener){
        if(listener == null){
            throw new IllegalArgumentException("LinkClickedListener cannot be null");
        }
        this.linkClickedListeners.add(listener);
    }

    /**
     * Register an editListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     */
    public void addEditListener(EditListener listener){
        if(listener == null){
            throw new IllegalArgumentException("EditListener cannot be null");
        }
        this.editListeners.add(listener);
    }

    /**
     * Remove a linkClickedListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     * @throws IllegalArgumentException if the given listener is not registered
     */
    public void removeLinkClickedListener(LinkClickedListener listener){
        if(listener == null){
            throw new IllegalArgumentException("LinkClickedListener cannot be null");
        }
        if(!this.linkClickedListeners.contains(listener)){
            throw new IllegalArgumentException("LinkClickedListener is not registered");
        }
        this.linkClickedListeners.remove(listener);
    }

    /**
     * Remove an editListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     * @throws IllegalArgumentException if the given listener is not registered
     */
    public void removeEditListener(EditListener listener){
        if(listener == null){
            throw new IllegalArgumentException("EditListener cannot be null");
        }
        if(!this.editListeners.contains(listener)){
            throw new IllegalArgumentException("EditListener is not registered");
        }
        this.editListeners.remove(listener);
    }

    /**
     * Method to notify all the linkClickedListeners of the document when a link is clicked in the component
     * @param link link clicked
     */
    public void handleLinkClick(String link){
        System.out.println("LINK CLICKED (PANE): " + link);
        for(LinkClickedListener l: new ArrayList<>(linkClickedListeners)){
            l.linkClicked(link);
        }
    }

    /**
     * Method to notify all the editListeners of the document when a input field's editMode is changed in the component
     * @param editmode link clicked
     */
    public void handleEditModeChanged(boolean editmode){
        for(EditListener l: new ArrayList<>(editListeners)){
            l.editModeUpdated(editmode);
        }
    }
}
