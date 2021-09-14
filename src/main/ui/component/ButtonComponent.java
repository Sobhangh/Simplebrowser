package ui.component;

import ui.listeners.AddBookmarkListener;
import ui.listeners.ClickListener;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonComponent extends Component {
    /**
     * Text of the button
     */
    private String buttonText;

    /**
     * Boolean indicating if button is being clicked
     */
    private boolean clicked;

    /**
     * Constructor of the ButtonComponent class
     * @param buttonText text of the button
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param width  width of the component
     * @param height height of the component
     */
    public ButtonComponent(String buttonText, int x, int y, int width, int height) {
        super(x, y, width, height);
        setButtonText(buttonText);
        setClicked(false);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (isClicked()) {
            g2d.setColor(Color.PINK);
        } else {
            g2d.setColor(Color.lightGray);
        }
        g2d.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 40, 20);
        g2d.setColor(Color.black);
        g2d.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 40, 20);
        int x = getX() + (getWidth() - g.getFontMetrics().stringWidth(buttonText)) / 2;
        g2d.drawString(buttonText, x, getY() + getHeight() - 2);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if (isPositionInComponent(x, y) && id == MouseEvent.MOUSE_PRESSED
                && x > getLowerLimitX() && x < getUpperLimitX() &&
                y > getLowerLimitY() && y < getUpperLimitY()) {
            setClicked(true);
        } else if (id == MouseEvent.MOUSE_RELEASED && isClicked()) {
            setClicked(false);
            clicked();
        }
    }

    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx, String name, String url) {
        if (isPositionInComponent(x, y)&& id == MouseEvent.MOUSE_PRESSED
                && x > getLowerLimitX() && x < getUpperLimitX() &&
                y > getLowerLimitY() && y < getUpperLimitY()) {
            setClicked(true);

        } else if (id == MouseEvent.MOUSE_RELEASED && isClicked()) {
            setClicked(false);
            addBookmark(name,url);
            clicked();
        }
    }


    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
    }

    /**
     * Getter for text of the button
     * @return text of the button
     */
    public String getButtonText() {
        return this.buttonText;
    }

    /**
     * Setter for text of the button
     * @param buttonText
     * @throws IllegalArgumentException if the given button text is null
     */
    public void setButtonText(String buttonText) {
        if (buttonText == null) {
            throw new IllegalArgumentException("Text cannot be null.");
        }
        this.buttonText = buttonText;
    }

    /**
     * @return true if button is being clicked, false otherwise
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Setter for clicked
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Method to notify all the clicklisteners when this button has been clicked
     */
    private void clicked(){
        for(ClickListener l: getClickListeners()){
            l.clicked();
        }
    }

    /**
     * Method to notify all bookmarkListeners when a new bookmark has been added
     */
    private void addBookmark(String name, String url){
        for(AddBookmarkListener l: getAddBookmarkListeners()){
            l.addBookmark(name, url);
        }
    }

    @Override
    public Component copy() {
        return new ButtonComponent(getButtonText(),getX(),getY(),getWidth(),getHeight());
    }

}
