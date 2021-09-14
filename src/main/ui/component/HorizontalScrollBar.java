package ui.component;

import ui.listeners.ScrollListener;

import java.awt.*;

public class HorizontalScrollBar extends ScrollBar {

    /**
     * the x coordinate of the horizontal slider
     */
    private int xslider;

    /**
     * the width of the horizontal slider
     */
    private int widthslider;

    /**
     * the difference in position of the cursor compared the previous measurement
     */
    private int deltaX;

    /**
     * the x coordinate of the cursor at the previous measurement
     */
    private int previousX;

    /**
     * percentage deciding how wide the slider should be compared to the total width of the scrollbar
     */
    private double percentage;

    /**
     * Constructor of the Component class
     *
     * @param x     x coordinate of the component
     * @param y     y coordinate of the component
     * @param width width of the component
     */
    public HorizontalScrollBar(int x, int y, int width) {
        super(x, y, width, 20);
        setXslider(x);
        setWidthsliderPercentage(1.0);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(getX(), getY(), getWidth() + 1, getHeight());
        g2d.setColor(Color.WHITE);
        g2d.fillRect(getX() + 1, getY() + 1, getWidth() - 2, getHeight() - 1);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(getXslider() + 2, getY() + 2,
                getWidthslider() - 4, getHeight() - 4);
        g2d.setColor(Color.BLACK);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if (isPositionInComponent(x, y)) {
            if (id == 501) {
                setMouseDown(true);
                setPreviousX(x);
            }
        }
        if (id == 502) {
            setMouseDown(false);
        } else if (id == 506) {
            if (isMouseDown()) {
                System.out.println("BEING DRAGGED HORIZONTALLY");
                setDeltaX(x - getPreviousX());
                setPreviousX(x);
                int abs_delta = (int) Math.round(getDeltaX() / percentage);
                System.out.println(abs_delta);
                if (getDeltaX() > 0) {
                    if (getXslider() + getWidthslider() + getDeltaX() <= getX() + getWidth()) {
                        setXslider(getXslider() + getDeltaX());
                        for (ScrollListener s1 : scrollListeners) {
                            s1.scrolled(abs_delta);
                        }
                    }
                } else if (getDeltaX() < 0) {
                    if (getXslider() + getDeltaX() >= getX() - 1) {
                        setXslider(getXslider() + getDeltaX());
                        for (ScrollListener s1 : scrollListeners) {
                            s1.scrolled(abs_delta);
                        }
                    }
                }
            }
        }

    }

    @Override
    public Component copy() {
        return new HorizontalScrollBar(getX(),getY(),getWidth());
    }


    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {

    }

    /**
     * Getter for the x coordinate of the slider
     * @return x coordinate of the slider
     */
    public int getXslider() {
        return xslider;
    }

    /**
     * Setter x coordinate of slider
     * @param xslider x coordinate of slider
     */
    public void setXslider(int xslider) {
        this.xslider = xslider;
    }

    /**
     * Getter for width of slider
     * @return width of slider
     */
    public int getWidthslider() {
        return widthslider;
    }

    /**
     * Setter for width of slider
     * @param widthslider width of slider
     */
    public void setWidthslider(int widthslider) {
        this.widthslider = widthslider;
    }

    /**
     * Set width of slider by percentage of width of full scrollbar
     * @param percentage percentage of width of full scrollbar
     */
    public void setWidthsliderPercentage(double percentage) {
        int width = (int) (percentage * getWidth());
        setWidthslider(width);
        this.percentage = percentage;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.setXslider(x);
    }

    /**
     * Getter for the difference in x coordinate of the cursor compared the previous measurement
     * @return difference in x coordinate of the cursor compared the previous measurement
     */
    public int getDeltaX() {
        return deltaX;
    }

    /**
     * Setter for the difference in x coordinate of the cursor compared the previous measurement
     * @param deltaX difference in x coordinate of the cursor compared the previous measurement
     */
    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    /**
     * Getter for the x coordinate of the cursor at the previous measurement
     * @return x coordinate of the cursor at the previous measurement
     */
    public int getPreviousX() {
        return previousX;
    }

    /**
     * Setter for the x coordinate of the cursor at the previous measurement
     * @param previousX x coordinate of the cursor at the previous measurement
     */
    public void setPreviousX(int previousX) {
        this.previousX = previousX;
    }

    /**
     * Setter for percentage deciding how wide the slider should be compared to the total width of the scrollbar
     * @param percentage how wide the slider should be compared to the total width of the scrollbar
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for percentage deciding how wide the slider should be compared to the total width of the scrollbar
     * @return how wide the slider should be compared to the total width of the scrollbar
     */
    public double getPercentage() {
        return this.percentage;
    }
}