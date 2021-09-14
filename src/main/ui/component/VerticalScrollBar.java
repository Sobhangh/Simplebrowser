package ui.component;

import ui.listeners.ScrollListener;

import java.awt.*;

public class VerticalScrollBar extends ScrollBar {

    /**
     * Y coordinate of the vertical slider
     */
    private int yslider;

    /**
     *  the height of the vertical slider
     */
    private int heightslider;

    /**
     *  the difference in position of the cursor compared the previous measurement
     */
    private int deltaY;

    /**
     *  the y coordinate of the cursor at the previous measurement
     */
    private int previousY;

    /**
     * percentage decides how wide the slider should be compared to the total width of the scrollbar
     */
    private double percentage;


    /**
     * Constructor of the Component class
     *
     * @param x      x coordinate of the component
     * @param y      y coordinate of the component
     * @param height height of the component
     */
    public VerticalScrollBar(int x, int y, int height) {
        super(x, y, 20, height);
        setYslider(y);
        setHeightsliderPercentage(1.0);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(getX(), getY(), getWidth() + 1, getHeight());
        g2d.setColor(Color.WHITE);
        g2d.fillRect(getX() + 1, getY() + 1, getWidth() + 1, getHeight() -2);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(getX() + 2, getYslider() + 2, getWidth() - 2, getHeightslider() - 4);
        g2d.setColor(Color.BLACK);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if (isPositionInComponent(x,y)) {
            if (id == 501) {
                setMouseDown(true);
                setPreviousY(y);
            }
        }
        if (id == 502) {
            setMouseDown(false);
        }
        else if (id == 506) {
            if (isMouseDown()) {
                System.out.println("VERTICALLY SCROLLED");
                setDeltaY(y - getPreviousY());
                setPreviousY(y);
                int abs_delta = (int)Math.round(getDeltaY()/percentage);
                System.out.println(abs_delta);

                if (getDeltaY() > 0) {
                    if (getYslider() + getHeightslider() + getDeltaY() <= getY() + getHeight()) {
                        setYslider(getYslider() + getDeltaY());
                        for (ScrollListener s1 : scrollListeners) {
                            s1.scrolled(abs_delta);
                        }
                    }
                }
                else if (getDeltaY() < 0) {
                    if (getYslider() + getDeltaY() >= getY()) {
                        setYslider(getYslider() + getDeltaY());
                        for (ScrollListener s1 : scrollListeners) {
                            s1.scrolled(abs_delta);
                        }
                    }
                }
            }
        }
    }

    /**
     * Set height of slider by percentage of height of full scrollbar
     * @param percentage percentage of height of full scrollbar
     */
    public void setHeightsliderPercentage(double percentage){
        int height = (int) (percentage * getHeight());
        setHeightslider(height);
        setPercentage(percentage);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {

    }

    @Override
    public void setY(int y){
        super.setY(y);
        this.setYslider(y);
    }

    @Override
    public Component copy() {
        return new VerticalScrollBar(getX(),getY(),getHeight());
    }

    /**
     * Getter for height of slider
     * @return height of slider
     */
    public int getHeightslider() {
        return heightslider;
    }

    /**
     * Setter for height of slider
     * @param heightslider height of slider
     */
    public void setHeightslider(int heightslider) {
        this.heightslider = heightslider;
    }

    /**
     * Getter for the y coordinate of the slider
     * @return y coordinate of the slider
     */
    public int getYslider() {
        return yslider;
    }

    /**
     * Setter y coordinate of slider
     * @param yslider y coordinate of slider
     */
    public void setYslider(int yslider) {
        this.yslider = yslider;
    }

    /**
     * Getter for the difference in y coordinate of the cursor compared the previous measurement
     * @return difference in y coordinate of the cursor compared the previous measurement
     */
    public int getDeltaY() {
        return deltaY;
    }

    /**
     * Setter for the difference in y coordinate of the cursor compared the previous measurement
     * @param deltaY difference in y coordinate of the cursor compared the previous measurement
     */
    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    /**
     * Getter for the y coordinate of the cursor at the previous measurement
     * @return y coordinate of the cursor at the previous measurement
     */
    public int getPreviousY() {
        return previousY;
    }

    /**
     * Setter for the y coordinate of the cursor at the previous measurement
     * @param previousY y coordinate of the cursor at the previous measurement
     */
    public void setPreviousY(int previousY) {
        this.previousY = previousY;
    }

    /**
     * Getter for percentage deciding the height of the slider compared to the total height of the scrollbar
     * @return percentage of how much the height of the slider should be compared to the total height of the scrollbar
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Setter for percentage deciding the height of the slider compared to the total height of the scrollbar
     * @param percentage percentage of how much the height of the slider should be compared to the total height of the scrollbar
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}