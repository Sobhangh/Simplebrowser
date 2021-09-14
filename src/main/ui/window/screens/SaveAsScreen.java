package ui.window.screens;

import domain.DocumentController;
import ui.component.ButtonComponent;
import ui.component.InputComponent;
import ui.window.BrowsrView;

import java.awt.*;

public class SaveAsScreen extends Screen {
    /**
     * Filename of the file being saved
     */
    private InputComponent filename;
    /**
     * Save Button
     */
    private ButtonComponent saveButton;
    /**
     * Cancel Button
     */
    private ButtonComponent cancelButton;

    /**
     * Constructor of the SaveAsScreen class
     * @param width width of the screen
     * @param height height of the screen
     * @param metrics metrics of the screen
     * @param controller controller used by screen
     * @param context context of screen
     */
    public SaveAsScreen(int width, int height, FontMetrics metrics, DocumentController controller, BrowsrView context){
        super("Save As", width, height, metrics, controller, context);
    }

    @Override
    void init() {
        this.filename = new InputComponent(150, 10, 30, getMetrics());
        this.filename.addEditListener((editmode -> setEditMode(editmode)));

        this.saveButton = new ButtonComponent("Save", 100, this.filename.getY() + this.filename.getHeight() + 10, 200, getMetrics().getHeight());
        this.saveButton.addClickListener(() -> saveFile());
        this.cancelButton = new ButtonComponent("Cancel", 320, this.filename.getY() + this.filename.getHeight() + 10, 200, getMetrics().getHeight());
        this.cancelButton.addClickListener(() -> getContext().backToBrowsr());
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("File name: ", 5, 30);

        this.filename.paint(g);
        this.saveButton.paint(g);
        this.cancelButton.paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        this.filename.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        this.saveButton.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        this.cancelButton.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        this.filename.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        this.saveButton.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        this.cancelButton.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
    }

    /**
     * Method to save the html of the current document to a file
     */
    public void saveFile() {
        getController().writeHtmlOfContentSpan(this.filename.getInput());
        getContext().backToBrowsr();
    }

    /**
     * Getter for the name of the file being saved
     * @return filename
     */
    public InputComponent getFilename() {
        return filename;
    }

    /**
     * Getter for the save button
     * @return the save button of the Save screen
     */
    public ButtonComponent getSaveButton() {
        return saveButton;
    }

    /**
     * Getter for the cancel button
     * @return the cancel button of the Save screen
     */
    public ButtonComponent getCancelButton() {
        return cancelButton;
    }

}
