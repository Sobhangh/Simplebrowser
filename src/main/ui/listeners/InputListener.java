package ui.listeners;

public interface InputListener {
    /**
     * Method the registered listeners implement to handle when input has been updated
     * @param input updated input
     * @param name name is also given if it's an input text field
     */
    void inputUpdated(String input, String name);
}
