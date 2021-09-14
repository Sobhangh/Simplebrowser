package ui.component;

import domain.Link;
import ui.listeners.LinkClickedListener;

import java.awt.*;
import java.util.ArrayList;

public class BookmarkComponent extends Component {
    /**
     *  String representing the name of the bookmark
     */
    private String name;
    /**
     * linkComponent that is shown inside the bookmark and is used as a reference when the bookmark is clicked
     */
    private LinkComponent link;
    /**
     * metrics used to paint the name of the bookmark
     */
    private FontMetrics metrics;

    /**
     * Constructor for the bookmark Class
     * @param x x coordinate of the bookmark
     * @param y y coordinate of the bookmark
     * @param width width of the bookmark
     * @param height height of the bookmark
     * @param metrics metrics used to represent text inside the bookmark
     * @param name name that is shown for the bookmark
     * @param url link to which the bookmark navigates when clicked
     */
    public BookmarkComponent(int x, int y, int width, int height, FontMetrics metrics, String name, String url) {
        super(x, y, width, height);
        setMetrics(metrics);
        setName(name);
        setLink(url);
    }

    @Override
    public void paint(Graphics g) {
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        getLink().paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        link.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {

    }

    @Override
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY) {

    }

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {

    }

    /**
     *  Getter for the link of the bookmark
     *
     * @return the link of the bookmark
     */
    public LinkComponent getLink() {
        return link;
    }

    /**
     *  Setter for the link of the bookmark
     *
     * @param url url that the bookmark should link to
     */
    public void setLink(String url) {
        this.link = new LinkComponent(getX(), getY() - getMetrics().getHeight()/2, getMetrics().stringWidth(url), getMetrics().getHeight(), url, name);
        this.link.setLowerLimitX(getX());
        this.link.setUpperLimitX(getX() + getWidth());
        this.link.setLowerLimitY(getY());
        this.link.setUpperLimitY(getY() + getHeight());
    }

    /**
     *  Getter for the name of the bookmark
     *
     * @return name of the bookmark
     */
    public String getName() {
        return name;
    }

    /**
     *  Setter for the name of the bookmark
     *
     * @param name the name that the bookmark should display
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  Getter for the metrics of the bookmark
     *
     * @return the metrics that this bookmark uses to paint the name
     */
    public FontMetrics getMetrics() {
        return metrics;
    }

    /**
     *  Setter for the metrics of this bookmark
     *
     * @param metrics the metrics this bookmark uses to paint the name
     */
    public void setMetrics(FontMetrics metrics) {
        this.metrics = metrics;
    }

    @Override
    public Component copy() {
        return new BookmarkComponent(getX(),getY(),getWidth(),getHeight(),getMetrics(),getName(),getLink().getHref());
    }
}
