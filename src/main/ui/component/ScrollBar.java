package ui.component;

import ui.listeners.LinkClickedListener;
import ui.listeners.ScrollListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ScrollBar extends Component {

    /**
     *  boolean indicating if the mouse is held down
     */
    private boolean mouseDown;

    /**
     * List containing all the ScrollListeners
     */
    protected final List<ScrollListener> scrollListeners = new ArrayList<ScrollListener>();

    /**
     * Constructor of the Component class
     *
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param width  width of the component
     */
    public ScrollBar(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY) {}

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {}

    /**
     * @return if the mouse is being held down
     */
    public boolean isMouseDown() {
        return mouseDown;
    }

    /**
     * Setter for boolean indicating if the mouse if being held down
     * @param mouseDown
     */
    public void setMouseDown(boolean mouseDown) {
        this.mouseDown = mouseDown;
    }

    /**
     * Register a ScrollListener
     * @param listener
     * @throws IllegalArgumentException if the given listener is null
     */
    public void addScrollListener(ScrollListener listener){
        if(listener == null){
            throw new IllegalArgumentException("ScrollListener cannot be null");
        }
        this.scrollListeners.add(listener);
    }

    /**
     * @return list of scrollListeners
     */
    public List<ScrollListener> getScrollListeners(){
        return this.scrollListeners;
    }
}
