package ui.component;

import ui.listeners.LinkClickedListener;

import java.awt.*;
import java.util.ArrayList;

public class BookmarkBarComponent extends Component {
    /**
     *  List of BookmarkComponents containing all the bookmarks this bar holds
     */
    private ArrayList<BookmarkComponent> bookmarks = new ArrayList<BookmarkComponent>();
    /**
     *  The metrics used to paint the bookmarks
     */
    private FontMetrics metrics;
    /**
     *  The space that should be held between the left of the bar and the first bookmark, and between two bookmarks
     */
    private final static int OFFSET = 5;

    /**
     * Constructor for the BookmarkBarComponent Class
     * @param x x coordinate of the bookmarkbar
     * @param y y coordinate of the bookmarkbar
     * @param width width of the bookmarkbar
     * @param height height of the bookmarkbar
     * @param metrics metrics used to represent text inside the bookmarkbar
     */
    public BookmarkBarComponent(int x, int y, int width, int height, FontMetrics metrics) {
        super(x, y, width, height);
        setMetrics(metrics);
    }

    @Override
    public void paint(Graphics g) {
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        for (BookmarkComponent bookmark : bookmarks) {
            bookmark.paint(g);
        }
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if (id == 501) {
            for (BookmarkComponent bookmark : bookmarks) {
                bookmark.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
            }
        }

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
     * Method to notify all the documentListeners when a link clicked event happens
     * @param link link clicked
     */
    public void handleLinkClick(String link){
        for(LinkClickedListener l: new ArrayList<>(getLinkClickedListeners())){
            l.linkClicked(link);
        }
    }

    /**
     *  Method that adds a bookmark to the bookmarkbar with the given name and url
     *
     * @param name name of the bookmark
     * @param url url of the bookmark, where to navigate when bookmark is clicked
     */
    public void addBookmark(String name, String url) {
        int startingPos = calculateX();
        BookmarkComponent bookmark = new BookmarkComponent(startingPos, getY() + 2, getMetrics().stringWidth(name) + OFFSET, getMetrics().getHeight() - 4, metrics, name, url);
        bookmark.getLink().addLinkClickedListener((link) -> handleLinkClick(link));
        bookmarks.add(bookmark);
    }

    /**
     *  Method to calculate the starting x coordinate of a newly added bookmark
     *
     * @return the left x coodrinate used for a new bookmark
     */
    private int calculateX() {
        int x = OFFSET;
        for (BookmarkComponent bookmark : bookmarks) {
            x += bookmark.getWidth();
            x += OFFSET;
        }
        return x;
    }

    /**
     * Method to retrieve the amount of bookmarks this bar holds
     *
     * @return amount of bookmarks
     */
    public int getAmountOfBookmarks() {
        return bookmarks.size();
    }

    public BookmarkComponent getBookmarkAtIndex(int index) {
        return bookmarks.get(index);
    }

    /**
     *  Setting the metrics used to paint the name of the bookmarks
     *
     * @param metrics the metrics to use in the paint method
     */
    public void setMetrics(FontMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Getting the metrics used to paint the name of the bookmarks
     *
     * @return the metrics used to paint the bookmarks names
     */
    public FontMetrics getMetrics() {
        return metrics;
    }

    @Override
    public Component copy() {
        return new BookmarkBarComponent(getX(),getY(),getWidth(),getHeight(),getMetrics());
    }

}
