package ui.window.screens;

import domain.DocumentController;
import ui.component.ButtonComponent;
import ui.component.InputComponent;
import ui.window.BrowsrView;

import java.awt.*;

public class AddToBookMarkScreen extends Screen {
    /**
     * Name of the Bookmark
     */
    private InputComponent name;
    /**
     * Url of the Bookmark
     */
    private InputComponent url;
    /**
     * Add button
     */
    private ButtonComponent addButton;
    /**
     * Cancel button
     */
    private ButtonComponent cancelButton;

    /**
     * Constructor of the AddToBookMarkScreen class
     * @param current_url current url of the address bar (default url)
     * @param width width of the screen
     * @param height height of the screen
     * @param metrics metrics of the screen
     * @param controller controller used by screen
     * @param context context of screen
     */
    public AddToBookMarkScreen(String current_url, int width, int height, FontMetrics metrics, DocumentController controller, BrowsrView context){
        super("Add Bookmark", width, height, metrics, controller, context);
        this.name.setInput(current_url);
        this.url.setInput(current_url);
    }

    @Override
    void init() {
        this.name = new InputComponent(100, 10, 30, getMetrics());
        this.name.addEditListener((editmode -> setEditMode(editmode)));
        this.url = new InputComponent(100, this.name.getY() + this.name.getHeight() + 10, 30, getMetrics());
        this.url.addEditListener((editmode -> setEditMode(editmode)));
        this.addButton = new ButtonComponent("Add Bookmark", 100, this.url.getY() + this.url.getHeight() + 10, 200, getMetrics().getHeight());
        this.addButton.addClickListener(() -> getContext().backToBrowsr());
        this.addButton.addBookmarkListener((name, url) -> getContext().getBrowsrScreen().addBookmark(name, url));
        this.cancelButton = new ButtonComponent("Cancel", 320,  this.url.getY() + this.url.getHeight() + 10, 200, getMetrics().getHeight());
        this.cancelButton.addClickListener(() -> getContext().backToBrowsr());
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("Name: ", 5, 30);
        g.drawString("URL: ", 5, 65);

        this.name.paint(g);
        this.url.paint(g);
        this.addButton.paint(g);
        this.cancelButton.paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        this.name.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        this.url.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        this.addButton.handleMouseEvent(id, x, y, clickCount, button, modifiersEx, name.getInput(), url.getInput());
        this.cancelButton.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        this.name.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        this.url.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        this.addButton.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        this.cancelButton.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
    }

    /**
     * @return input of the name field
     */
    public String getName() {
        return this.name.getInput();
    }

    /**
     * @return input of the url field
     */
    public String getUrl() {
        return this.url.getInput();
    }
}
