package ui.component;

import ui.listeners.EditListener;
import ui.listeners.InputListener;
import java.awt.*;
import java.util.ArrayList;

public class AddressBarComponent extends Component {
    /**
     * InputField containing the url of the address bar
     */
    private InputComponent urlField;

    /**
     * Constructor for the AddressBarComponent Class
     * @param x x coordinate of the address bar
     * @param y y coordinate of the address bar
     * @param width width of the address bar
     * @param height height of the address bar
     * @param metrics metrics used to represent text inside the address bar
     */
    public AddressBarComponent(int x, int y, int width, int height, FontMetrics metrics, String url) {
        super(x, y, width, height);
        setUrlField(new InputComponent(x, y, width, height, metrics, url));
        this.urlField.addInputListener((input, name) -> updateUrl(input, name));
        this.urlField.addEditListener((editmode) -> updateEditMode(editmode));
        setHeight(this.urlField.getHeight());
    }


    @Override
    public void paint(Graphics g) {
        urlField.paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        urlField.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        urlField.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
    }

    @Override
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY) {

    }

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {

    }

    /**
     * Setter for url of the address bar
     * @param url
     * @throws IllegalArgumentException if the given url is null
     */
    public void setUrl(String url){
        if(url == null){
            throw new IllegalArgumentException("Url cannot be null.");
        }
        this.urlField.setInput(url);
        this.urlField.setScrollBarSlider();
    }

    /**
     * Getter for url of the address bar
     * @return url of the address bar
     */
    public String getUrl(){
        return this.urlField.getInput();
    }

    /**
     * Setter for input component containing the url of the address bar
     * @param urlField
     * @throws IllegalArgumentException if the given input component is null
     */
    public void setUrlField(InputComponent urlField){
        if(urlField == null){
            throw new IllegalArgumentException("Url Input Field cannot be null.");
        }
        this.urlField = urlField;
        this.urlField.setLowerLimitX(0);
        this.urlField.setLowerLimitY(0);
        this.urlField.setUpperLimitX(getWidth());
        this.urlField.setUpperLimitY(getHeight());
    }

    /**
     * Getter for input component containing the url of the address bar
     * @return input component containing the url of the address bar
     */
    public InputComponent getUrlField(){
        return this.urlField;
    }

    /**
     * Method to notify all the inputListeners when url is updated
     */
    public void updateUrl(String input, String name){
        for(InputListener listener: new ArrayList<>(getInputListeners())){
            listener.inputUpdated(input, name);
        }
    }

    /**
     * Method to notify all the editListeners when editmode of url field changes
     */
    public void updateEditMode(boolean editmode){
        for(EditListener listener: new ArrayList<>(getEditListeners())){
            listener.editModeUpdated(editmode);
        }
    }

    @Override
    public void setWidth(int width){
        super.setWidth(width);
        if(urlField != null){
            this.urlField.setWidth(width);
        }
    }

    @Override
    public Component copy() {
        return new AddressBarComponent(getX(),getY(),getWidth(),getHeight(), urlField.getMetrics(), "");
    }
}
