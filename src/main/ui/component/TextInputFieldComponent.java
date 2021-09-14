package ui.component;

import ui.listeners.InputListener;
import java.awt.*;
import java.util.ArrayList;

public class TextInputFieldComponent extends InputComponent{
    /**
     * Name of the text input field
     */
    private String name;

    /**
     * Constructor of the TextInputFieldComponent class
     *
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param height height of the component
     * @param metrics metrics of the canvas
     * @param name name of the text input field
     */
    public TextInputFieldComponent(int x, int y, int height, FontMetrics metrics, String name) {
        super(x, y, height, metrics);
        setName(name);
    }

    /**
     * Constructor of the TextInputFieldComponent class
     *
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param width width of the component
     * @param height height of the component
     * @param metrics metrics of the canvas
     * @param name name of the text input field
     */
    public TextInputFieldComponent(int x, int y, int width, int height, FontMetrics metrics, String name) {
        super(x, y, width, height, metrics, "");
        setName(name);
    }

    /**
     * Constructor of the TextInputFieldComponent class
     *
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param width width of the component
     * @param height height of the component
     * @param metrics metrics of the canvas
     * @param name name of the text input field
     */
    public TextInputFieldComponent(int x, int y, int width, int height, FontMetrics metrics, String name, String input) {
        super(x, y, width, height, metrics, input);
        setName(name);
    }

    public Component copy(){
        return  new TextInputFieldComponent(getX(), getY(), getHeight(), getMetrics(), getName());
    }

    /**
     * Getter for name of the text input field
     * @return name of the text input field
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of the text input field
     * @param name
     * @throws IllegalArgumentException if the given name is null
     */
    public void setName(String name) {
        if(name == null){
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
    }

    @Override
    public void inputChanged(){
        for(InputListener inputListener: new ArrayList<>(getInputListeners())){
            inputListener.inputUpdated(getInput(), this.name);
        }
    }
}
