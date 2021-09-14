package ui.component;

import java.awt.*;

public class TextComponent extends Component{
    /**
     * Text of the component
     */
    private String text;

    /**
     * Constructor of the TextComponent class
     * @param x x coordinate of the Text Component
     * @param y y coordinate of the Text Component
     * @param width width of the Text Component
     * @param height height of the Text Component
     * @param text text of the Text Component
     */
    public TextComponent(int x, int y, int width, int height, String text){
        super(x, y, width, height);
        setText(text);
    }

    /**
     * Getter for the text
     * @return text
     */
    public String getText(){
        return this.text;
    }

    /**
     * Setter for the text
     * @param text
     * @throws IllegalArgumentException if the given text is null
     */
    public void setText(String text){
        if(text == null){
            throw new IllegalArgumentException("Text cannot be null");
        }
        this.text = text;
    }

    @Override
    public void paint(Graphics g) {
        g.drawString(getText(), getX(), getY() + g.getFontMetrics().getHeight());
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {

    }


    @Override
    public Component copy() {
        return new TextComponent(getX(),getY(),getWidth(),getHeight(),getText());
    }

    @Override
    public boolean isPositionInComponent(int x, int y){
        return x >= getX() && x <= (getX() + getWidth()) & y >= getY() && y <= (getY() + getHeight());
    }

}
