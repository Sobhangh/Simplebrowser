package ui.component;

import ui.listeners.EditListener;
import ui.listeners.LinkClickedListener;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FormComponent extends Component {
    /**
     * Component for the contentspan in the form
     */
    private Component content;
    /**
     * Action of the form
     */
    private String action;
    /**
     * All the filled in inputs and their names in the form
     */
    HashMap<String, String> inputs = new HashMap<String, String>();

    /**
     * Constructor of the FormComponent class
     * @param content content of the form
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param width  width of the component
     * @param height height of the component
     */
    public FormComponent(Component content, String action, int x, int y, int width, int height) {
        super(x, y, width, height);
        setContent(content);
        this.action=action;
        // inputs are added to the list if they are filled in => when inputUpdated is called
        content.addInputListener((input, name) -> inputs.put(name, input));
        // handle button clicked in form => link (constructed by inputs and action) clicked
        content.addClickListener(() -> {
            ArrayList<String> name_value = new ArrayList<>();
            for (Map.Entry<String, String> entry : inputs.entrySet()) {
                String name = null;
                name = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
                String value = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
                name_value.add(name + "=" + value);
            }
            String temp = name_value.stream().
                    collect(Collectors.joining("&"));

            linkClick(action + "?" + temp);
        });
    }

    @Override
    public void paint(Graphics g) {
        content.paint(g);
        //content.paint(g);
        //System.out.println("graphics bound: "+(g.getClip().getBounds().getX()+g.getClip().getBounds().getWidth()));
        System.out.println("INPUTS: -------------\n");
        for (Map.Entry<String, String> entry : inputs.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        content.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        content.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
    }

    @Override
    public Component copy() {
        return new FormComponent(getContent().copy(), action, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY) {
        super.handleVerticalScroll(delta, lowerLimitY, upperLimitY);
        content.handleVerticalScroll(delta, lowerLimitY, upperLimitY);
    }

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {
        super.handleHorizontalScroll(delta, lowerLimitX, upperLimitX);
        content.handleHorizontalScroll(delta, lowerLimitX, upperLimitX);
    }

    /**
     * Getter of the content
     * @return the content of the form
     */
    public Component getContent() {
        return this.content;
    }

    /**
     * Setter for the content of the form
     * @param content
     * @throws IllegalArgumentException if the given content in null
     */
    public void setContent(Component content) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null.");
        }
        this.content = content;
    }

    @Override
    public void addEditListener(EditListener listener) {
        this.content.addEditListener(listener);
    }

    @Override
    public void removeEditListener(EditListener listener) {
        this.content.removeEditListener(listener);
    }

    /**
     * Method to notify all the linkClickedlisteners when the form is submitted
     */
    private void linkClick(String link) {
        for (LinkClickedListener l : getLinkClickedListeners()) {
            l.linkClicked(link);
        }
    }

    public void setXr(int x){
        this.setX(x);
        content.setXr(x);
    }

    public void setYr(int y){
        this.setY(y);
        content.setYr(y);
    }

    @Override
    public void setUpperLimitX(int upperLimitX) {
        super.setUpperLimitX(upperLimitX);
        content.setUpperLimitX(upperLimitX);
    }

    @Override
    public void setLowerLimitX(int lowerLimitX) {
        super.setLowerLimitX(lowerLimitX);
        content.setLowerLimitX(lowerLimitX);
    }

    @Override
    public void setUpperLimitY(int upperLimitY) {
        super.setUpperLimitY(upperLimitY);
        content.setUpperLimitY(upperLimitY);
    }

    @Override
    public void setLowerLimitY(int lowerLimitY) {
        super.setLowerLimitY(lowerLimitY);
        content.setLowerLimitY(lowerLimitY);
    }
}
