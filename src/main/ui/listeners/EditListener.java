package ui.listeners;

public interface EditListener {
    /**
     * Method the registered listeners implement to handle when an input's editmode has changed
     * @param editmode updated editmode
     */
    void editModeUpdated(boolean editmode);
}
