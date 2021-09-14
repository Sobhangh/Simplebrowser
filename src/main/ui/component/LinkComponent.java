package ui.component;

import ui.listeners.LinkClickedListener;

import java.awt.*;

public class LinkComponent extends Component {
    /**
     * Href of the link
     */
    private String href;

    /**
     * Name of the link
     */
    private String name;

    /**
     * Constructor for the LinkComponent class
     *
     * @param x      x coordinate of the Link Component
     * @param y      y coordinate of the Link Component
     * @param width  width of the Link Component
     * @param height height of the Link Component
     * @param href   href of the Link Component
     */
    public LinkComponent(int x, int y, int width, int height, String href) {
        super(x, y, width, height);
        setHref(href);
    }

    /**
     * Second Constructor for the LinkComponent class
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param href
     * @param name
     */
    public LinkComponent(int x, int y, int width, int height, String href, String name) {
        super(x, y, width, height);
        setHref(href);
        setName(name);
    }


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.blue);
        if (getName() == null) {
            g.drawString(getHref(), getX(), getY() + g.getFontMetrics().getHeight());
            g.drawLine(getX(),
                    getY() + g.getFontMetrics().getHeight(),
                    getX() + g.getFontMetrics().stringWidth(this.href), getY() + g.getFontMetrics().getHeight());

        } else {
            g.drawString(getName(), getX(), getY() + g.getFontMetrics().getHeight());
            g.drawLine(getX(),
                    getY() + g.getFontMetrics().getHeight(),
                    getX() + g.getFontMetrics().stringWidth(this.name), getY() + g.getFontMetrics().getHeight());
        }


        g.setColor(Color.BLACK);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if (isPositionInComponent(x, y) && id == 501 && x > getLowerLimitX() && x < getUpperLimitX() &&
                y > getLowerLimitY() && y < getUpperLimitY()) {
            System.out.println("Link clicked: x - " + getX() + " y - " + getY()
                    + "clicked x - " + x + "clicked y - " + y);
            linkClick();
        }
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
    }

    @Override
    public Component copy() {
        if (this.getName() == null) {
            return new LinkComponent(this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getHref());
        } else {
            return new LinkComponent(getX(), getY(), getWidth(), getHeight(), getHref(), getName());
        }
    }

    /**
     * Getter for href of the link
     *
     * @return href of the link
     */
    public String getHref() {
        return this.href;
    }

    /**
     * Setter for href of the link
     *
     * @param href
     * @throws IllegalArgumentException if the given href is null
     */
    public void setHref(String href) {
        if (href == null) {
            throw new IllegalArgumentException("Href cannot be null");
        }
        this.href = href;
    }

    /**
     * Method to notify all the linkClickedlisteners when this link has been clicked
     */
    private void linkClick() {
        for (LinkClickedListener l : getLinkClickedListeners()) {
            l.linkClicked(this.href);
        }
    }

    /**
     * Getter for name of the link
     * @return name of the link
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of the link
     * @param name name of the link
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }
}
