package ui.component;

import ui.listeners.EditListener;
import ui.listeners.LinkClickedListener;

import java.awt.*;
import java.util.ArrayList;

public class DocumentComponent extends Component{
    /**
     * Component for the loaded contentspan
     */
    private Component component;

    /**
     * A vertical scrollbar used to scroll through content when this doesn't fit
     */
    private VerticalScrollBar scrollBarV;

    /**
     * A horizontal scrollbar used to scroll through content when this doesn't fit
     */
    private HorizontalScrollBar scrollBarH;

    /**
     * Constructor for the DocumentComponent class
     * @param x x coordinate of the Document Component
     * @param y y coordinate of the Document Component
     * @param width width of the Document Component
     * @param height height of the Document Component
     */
    public DocumentComponent(int x, int y, int width, int height){
        super(x, y, width, height);
        scrollBarV = new VerticalScrollBar(x + width - 20, y, height);
        scrollBarV.addScrollListener(delta -> handleVerticalScroll(delta));
        scrollBarH = new HorizontalScrollBar(x, y + height - 20, width);
        scrollBarH.addScrollListener(delta -> handleHorizontalScroll(delta));
    }

    /**
     * Method for when the vertical scrollbar in document has been dragged
     * @param delta how much the scrollbar has been dragged up or down
     */
    protected void handleVerticalScroll(int delta) {
        component.handleVerticalScroll(delta, getY(), (getY() + getHeight() - scrollBarH.getHeight()));
    }

    /**
     * Method for when the horizontal scrollbar in document has been dragged
     * @param delta how much the scrollbar has been dragged to the left or right
     */
    protected void handleHorizontalScroll(int delta) {
        component.handleHorizontalScroll(delta, getX(), (getX() + getWidth() - scrollBarV.getWidth()));
    }

    @Override
    public void paint(Graphics g) {
        g.setClip(new Rectangle(getX(), getY(), getWidth() - scrollBarV.getWidth(), getHeight() - scrollBarH.getHeight()));
        component.paint(g);
        g.setClip(null);
        scrollBarV.paint(g);
        scrollBarH.paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if(component!=null) {
            this.component.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        }
        this.scrollBarH.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        this.scrollBarV.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        if(component!=null) {
            this.component.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        }
    }

    @Override
    public Component copy() {
        DocumentComponent d = new DocumentComponent(getX(),getY(),getWidth(),getHeight());
        d.setComponent(this.getComponent().copy());
        return d;
    }

    /**
     * Setter for the component of the Document
     * @param component
     * @throws IllegalArgumentException if the given component in null
     */
    public void setComponent(Component component){
        if(component == null){
            throw new IllegalArgumentException("Component cannot be null");
        }
        this.component = component;
        this.component.addLinkClickedListener((link) -> handleLinkClick(link));
        this.component.addEditListener((editmode -> handleEditModeChanged(editmode)));

        this.component.setLowerLimitX(getX());
        this.component.setUpperLimitX(getX() + getWidth());
        this.component.setLowerLimitY(getY());
        this.component.setUpperLimitY(getY() + getHeight());

        if(scrollBarV != null){
            this.scrollBarV.setYslider(scrollBarV.getY());
            setVerticalScrollBarSlider();
        }
        if(scrollBarH != null){
            this.scrollBarH.setXslider(scrollBarH.getX());
            setHorizontalScrollBarSlider();
        }
    }

    /**
     * Getter of the component
     * @return the component of the Document
     */
    public Component getComponent(){
        return this.component;
    }

    /**
     * Method to notify all the linkClickedListeners of the document when a link is clicked in the component
     * @param link link clicked
     */
    public void handleLinkClick(String link){
        System.out.println("DOCUMENT: A Link was clicked!" + link);
        for(LinkClickedListener l: new ArrayList<>(getLinkClickedListeners())){
            l.linkClicked(link);
        }
    }

    /**
     * Method to notify all the editListeners of the document when a input field's editMode is changed in the component
     * @param editmode link clicked
     */
    public void handleEditModeChanged(boolean editmode){
        for(EditListener l: new ArrayList<>(getEditListeners())){
            l.editModeUpdated(editmode);
        }
    }

    @Override
    public void setWidth(int width){
        System.out.println("doc width:"+ width);
        super.setWidth(width);
        if(this.scrollBarV != null){
            System.out.println("scrol x:"+ (width - this.scrollBarV.getWidth()));
            this.scrollBarV.setX(this.getX()+width - this.scrollBarV.getWidth());
        }
        if(this.scrollBarH != null){
            this.scrollBarH.setWidth(width - this.scrollBarV.getWidth());
            setHorizontalScrollBarSlider();
            if(component!=null) {
                this.scrollBarH.setXslider(
                        this.scrollBarH.getX() + (int) Math.round(-1 * (component.getX() - getX()) * getPercentageWidth()));
            }
        }
        if(component != null){
            component.setUpperLimitX(getX() + width - 20);
        }
    }

    @Override
    public void setHeight(int height){
        super.setHeight(height);
        if(this.scrollBarV != null){
            this.scrollBarV.setHeight(height);
            setVerticalScrollBarSlider();
            if(component!=null) {
                this.scrollBarV.setYslider(
                        this.scrollBarV.getY() + (int) Math.round(-1 * (component.getY() - getY()) * getPercentageHeight()));
            }
        }
        if(this.scrollBarH != null){
            this.scrollBarH.setY(getY() + height - this.scrollBarH.getHeight());
        }

        if(component != null){
            component.setUpperLimitY(getY() + height - 20);
        }

    }

    /**
     * Set the width of the horizontal scrollbar relative to how much of the component is shown
     */
    public void setHorizontalScrollBarSlider(){
        this.scrollBarH.setWidthsliderPercentage(getPercentageWidth());
    }

    /**
     * @return percentage of how much of the width of the component is shown
     */
    public double getPercentageWidth(){
        int width = getWidth() - scrollBarV.getWidth();
        if(component!=null) {
            if (component.getWidth() > width) {
                return (double) width / component.getWidth();
            } else {
                return 1.0;
            }
        }
        return 0;
    }

    /**
     * @return percentage of how much of the height of the component is shown
     */
    public double getPercentageHeight(){
        int height = (getHeight() - scrollBarH.getHeight());
        if(component!=null) {
            if (component.getHeight() > height) {
                return (double) height / component.getHeight();
            } else {
                return 1.0;
            }
        }
        return 0;
    }

    /**
     * Set the height of the scrollbar relative to how much of the component is shown
     */
    public void setVerticalScrollBarSlider(){
        this.scrollBarV.setHeightsliderPercentage(getPercentageHeight());
    }


    public void setXr(int x){
        this.setX(x);
        this.scrollBarV.setX(x+getWidth());
        this.scrollBarH.setX(x);
        if(component!=null) {
            component.setXr(x);
        }
    }

    public void setYr(int y){
        this.setY(y);
        this.scrollBarV.setY(y);
        this.scrollBarH.setY(y+this.getHeight());
        if(component!=null) {
            component.setYr(y);
        }
    }

    /**
     * @return horizontal scrollbar of this document
     */
    public HorizontalScrollBar getHorizontalScrollBar(){
        return this.scrollBarH;
    }

    /**
     * @return vertical scrollbar of this document
     */
    public VerticalScrollBar getVerticalScrollBar(){
        return this.scrollBarV;
    }

}
