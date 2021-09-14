package ui.component;

import ui.listeners.*;
import ui.util.Range;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class Component {
    /**
     * x coordinate of component
     */
    private int x;

    /**
     * y coordinate of component
     */
    private int y;

    /**
     * Width of component
     */
    private int width;

    /**
     * Height of component
     */
    private int height;

    /**
     *  Limited Range of y coordinates in which the component should be visible
     */
    private Range limitY = new Range(0,10000);

    /**
     *  Limited Range of x coordinates in which the component should be visible
     */
    private Range limitX = new Range(0, 10000);

    /**
     * List containing all the LinkClickedListener
     */
    private final List<LinkClickedListener> linkClickedListeners = new ArrayList<>();

    /**
     * List containing all the Clicklisteners
     */
    private final List<ClickListener> clickListeners = new ArrayList<>();

    /**
     * List containig all the InputListeners
     */
    private final List<InputListener> inputListeners = new ArrayList<>();

    /**
     * List containing all the EditListeners
     */
    private final List<EditListener> editListeners = new ArrayList<>();

    /**
     * List containing all the EditListeners
     */
    private final List<AddBookmarkListener> addBookmarkListeners = new ArrayList<>();

    /**
     * Constructor of the Component class
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     * @param width width of the component
     * @param height height of the component
     */
    public Component(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Method that draws the component on the appropriate location
     * @param g
     */
    public abstract void paint(Graphics g);

    /**
     * Method that handles the behavior the component should have at mouse events
     * @param id
     * @param x
     * @param y
     * @param clickCount
     * @param button
     * @param modifiersEx
     */
    public abstract void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx);

    /**
     * Method that handles the behavior the component should have at key events
     * @param id
     * @param keyCode
     * @param keyChar
     * @param modifiersEx
     */
    public abstract void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx);

    /**
     * Method to create a new component from this component
     * @return A component with the same x & y coordinate, height & width and maybe other characteristics
     */
    public abstract Component copy();

    /**
     * Method for when vertical scrollbar in pane has been dragged
     * @param delta how much the scrollbar has been dragged up or down
     * @param lowerLimitY lowest y coordinate of the pane
     * @param upperLimitY highest y coordinate of the pane
     */
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY){
        setLowerLimitY(lowerLimitY);
        setUpperLimitY(upperLimitY);
        if(((getY() + getHeight() > upperLimitY) && delta > 0) || (getY() < lowerLimitY && delta < 0)){
            setY(getY() - delta);
        }

    }

    /**
     * Method for when horizontal scrollbar in pane has been dragged
     * @param delta how much the scrollbar has been dragged left or right
     * @param lowerLimitX lowest x coordinate of the pane
     * @param upperLimitX highest x coordinate of the pane
     */
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {
        setLowerLimitX(lowerLimitX);
        setUpperLimitX(upperLimitX);
        if(((getX() + getWidth() > upperLimitX) && delta > 0) || (getX() < lowerLimitX && delta < 0)){
            setX(getX() - delta);
        }

    }

    /**
     * Getter X coordinate
     * @return X coordinate of component
     */
    public int getX(){
        return this.x;
    }

    /**
     * Setter X coordinate
     * @param x
     */
    public void setX(int x){
        this.x = x;
    }

    public void setXr(int x){
        setX(x);
    }
    /**
     * Getter Y coordinate
     * @return Y coordinate of component
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
        this.y = y;
    }

    public void setYr(int y){
        setY(y);
    }

    /**
     * Getter width
     * @return width of component
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
     * Getter height
     * @return height of component
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
     * Returns if the given x and y coordinates are a position in the component
     * @param x
     * @param y
     * @return boolean indicating if given (x, y) coordinate are positioned in component
     */
    public boolean isPositionInComponent(int x, int y){
        return x >= getX() && y >= getY() && (x <= getX() + getWidth()) && (y <= getY() + getHeight());
    }

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
     * Register a clicklistener
     * @param listener
     */
    public void addClickListener(ClickListener listener){
        if(listener == null){
            throw new IllegalArgumentException("ClickListener cannot be null");
        }
        this.clickListeners.add(listener);
    }

    /**
     * Register an inputlistener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     */
    public void addInputListener(InputListener listener){
        if(listener == null){
            throw new IllegalArgumentException("InputListener cannot be null");
        }
        this.inputListeners.add(listener);
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
     * Register an inputlistener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     */
    public void addBookmarkListener(AddBookmarkListener listener){
        if(listener == null){
            throw new IllegalArgumentException("InputListener cannot be null");
        }
        this.addBookmarkListeners.add(listener);
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
     * Remove a ClickListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     * @throws IllegalArgumentException if the given listener is not registered
     */
    public void removeClickListener(ClickListener listener){
        if(listener == null){
            throw new IllegalArgumentException("ClickListener cannot be null");
        }
        if(!this.clickListeners.contains(listener)){
            throw new IllegalArgumentException("ClickListener is not registered");
        }
        this.clickListeners.remove(listener);
    }

    /**
     * Remove a InputListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     * @throws IllegalArgumentException if the given listener is not registered
     */
    public void removeInputListener(InputListener listener){
        if(listener == null){
            throw new IllegalArgumentException("InputListener cannot be null");
        }
        if(!this.inputListeners.contains(listener)){
            throw new IllegalArgumentException("InputListener is not registered");
        }
        this.inputListeners.remove(listener);
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
     * Remove an editListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     * @throws IllegalArgumentException if the given listener is not registered
     */
    public void removeAddBookmarkListener(AddBookmarkListener listener){
        if(listener == null){
            throw new IllegalArgumentException("EditListener cannot be null");
        }
        if(!this.addBookmarkListeners.contains(listener)){
            throw new IllegalArgumentException("EditListener is not registered");
        }
        this.addBookmarkListeners.remove(listener);
    }

    /**
     * Getter linkClickedlisteners
     * @return linkClickedlisteners of component
     */
    public List<LinkClickedListener> getLinkClickedListeners(){
        return this.linkClickedListeners;
    }

    /**
     * Getter clicklisteners
     * @return clicklisteners of component
     */
    public List<ClickListener> getClickListeners(){
        return this.clickListeners;
    }

    /**
     * Getter InputListeners
     * @return InputListeners of component
     */
    public List<InputListener> getInputListeners(){
        return this.inputListeners;
    }

    /**
     * Getter EditListeners
     * @return EditListeners of component
     */
    public List<EditListener> getEditListeners(){
        return this.editListeners;
    }

    /**
     * Getter EditListeners
     * @return EditListeners of component
     */
    public List<AddBookmarkListener> getAddBookmarkListeners(){
        return this.addBookmarkListeners;
    }

    /**
     * Getter for current highest y coordinate visible
     * @return highest y coordinate
     */
    public int getUpperLimitY() {
        return limitY.getRight();
    }

    /**
     * Getter for current lowest y coordinate visible
     * @return lowest y coordinate
     */
    public int getLowerLimitY() {
        return limitY.getLeft();
    }

    /**
     * Setter for highest visible y coordinate
     * @param upperLimitY highest y coordinate visible
     */
    public void setUpperLimitY(int upperLimitY) {
        this.limitY.setRight(upperLimitY);
    }

    /**
     * Setter for lowest visible y coordinate
     * @param lowerLimitY lowest y coordinate visible
     */
    public void setLowerLimitY(int lowerLimitY) {
        this.limitY.setLeft(lowerLimitY);
    }

    /**
     * Getter for current highest x coordinate visible
     * @return highest x coordinate
     */
    public int getUpperLimitX() {
        return limitX.getRight();
    }

    /**
     * Getter for current lowest x coordinate visible
     * @return lowest x coordinate
     */
    public int getLowerLimitX() {
        return limitX.getLeft();
    }

    /**
     * Setter for highest visible x coordinate
     * @param upperLimitX highest x coordinate visible
     */
    public void setUpperLimitX(int upperLimitX) {
        this.limitX.setRight(upperLimitX);
    }

    /**
     * Setter for lowest visible x coordinate
     * @param lowerLimitX lowest x coordinate visible
     */
    public void setLowerLimitX(int lowerLimitX) {
        this.limitX.setLeft(lowerLimitX);
    }

}
