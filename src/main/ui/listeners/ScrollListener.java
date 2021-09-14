package ui.listeners;

public interface ScrollListener {
    /**
     * Method the registered listeners implement to handle when a scroll event happens
     * @param delta how much the the component should be scrolled
     */
    void scrolled(int delta);
}
