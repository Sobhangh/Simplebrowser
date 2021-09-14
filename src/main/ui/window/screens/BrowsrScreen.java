package ui.window.screens;

import domain.DocumentController;
import ui.component.AddressBarComponent;
import ui.component.BookmarkBarComponent;
import ui.component.DocumentComponent;
import ui.util.ToComponentUtil;
import ui.window.BrowsrView;
import ui.window.panes.LeafPane;
import ui.window.panes.Pane;
import java.awt.*;

public class BrowsrScreen extends Screen {
    /**
     * Address bar of the the Browsr
     */
    private AddressBarComponent addressbar;

    /**
     * BookmarkBar viewer of the Browsr
     */
    private BookmarkBarComponent bookmarkbar;

    /**
     * Rootpane of the Browsr
     */
    private Pane rootpane;
    /**
     * Constructor of the BrowsrScreen class
     * @param width width of the screen
     * @param height height of the screen
     * @param metrics metrics of the screen
     * @param controller controller used by screen
     * @param context context of screen
     */
    public BrowsrScreen(int width, int height, FontMetrics metrics, DocumentController controller, BrowsrView context){
        super("Browsr", width, height, metrics, controller, context);
        setWidthBrowsr(width);
        setHeightBrowsr(height);
    }

    @Override
    public void init(){
        setAddressbar();
        setBookMarkBar();
        setPane();
    }

    @Override
    public void paint(Graphics g) {
        addressbar.paint(g);
        bookmarkbar.paint(g);
        rootpane.paint(g);
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        addressbar.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        bookmarkbar.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        rootpane.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        addressbar.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
        rootpane.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
    }

    /**
     * Handle the clicked link
     *
     * @param link The link that was clicked
     */
    public void handleClickLink(String link){
        System.out.println("BROWSR: LINK  CLICKED -- " + link);
        getController().clickLink(link);
        update();
    }

    /**
     * Handle the entered url
     *
     * @param url The url that was entered
     */
    public void handleEnteredUrl(String url){
        getController().updateURL(url);
        update();
    }

    /**
     * Handle the clicked link
     *
     * @param url The link that was clicked
     */
    public void addBookmark(String name, String url){
        bookmarkbar.addBookmark(name, url);
        update();
    }

    /**
     * Method that updates the addressbar and the documentviewer with the data from the controller
     */
    public void update(){
        this.addressbar.setUrl(getController().getUrl());
        this.rootpane.setUrl(getController().getUrl());
        this.rootpane.update(getController().getContentSpan(), getMetrics());
    }

    /**
     * Setter for the address bar
     */
    public void setAddressbar(){
        this.addressbar = new AddressBarComponent(1, 0, getWidth(), getMetrics().getHeight(), getMetrics(), getController().getUrl());
        this.addressbar.addInputListener((input, name) -> handleEnteredUrl(input));
        this.addressbar.addEditListener((editmode) -> setEditMode(editmode));
    }

    /**
     * Setter for the bookmark bar
     */
    private void setBookMarkBar() {
        this.bookmarkbar = new BookmarkBarComponent(1, getAddressbar().getHeight() + 5, getWidth(), getMetrics().getHeight(), getMetrics());
        this.bookmarkbar.addLinkClickedListener((link) -> handleClickLink(link));
    }

    /**
     * Setter for the rootpane of the browsr
     */
    public void setPane(){
        DocumentComponent d = new DocumentComponent( 0, getBookmarkbar().getY() + getBookmarkbar().getHeight() + 5, getWidth(), getHeight() - getAddressbar().getHeight() - getBookmarkbar().getHeight() - 10 );
        this.getController().setContentSpan(this.getController().getstartpage());
        d.setComponent( ToComponentUtil.toComponent(1, getBookmarkbar().getY() + getBookmarkbar().getHeight() + 5, getController().getContentSpan(), getMetrics()));
        this.rootpane = new LeafPane(d.getX(),d.getY(),d.getWidth(),d.getHeight(),d,null,this);
        this.rootpane.update(this.getController().getstartpage(),getMetrics());
        this.rootpane.setFocused(true);
        rootpane.addLinkClickedListener((link) -> handleClickLink(link));
        rootpane.addEditListener((editmode) -> setEditMode(editmode));
        this.addressbar.setUrl("https://");
    }

    /**
     * Setter of width of browsr, updating the width of the address bar, bookmark bar and rootpane
     * @param width
     */
    public void setWidthBrowsr(int width){
        this.addressbar.setWidth(width - 2);
        this.bookmarkbar.setWidth(width - 2);
        this.rootpane.setWidth(width - 2);
    }

    /**
     * Setter for height of the browsr, updating the height of the rootpane
     * @param height
     */
    public void setHeightBrowsr(int height){
        this.rootpane.setHeight(height - (getAddressbar().getHeight() + getBookmarkbar().getHeight() + 10));
    }

    /**
     * Getter for the url of the address bar
     * @return url of the address bar
     */
    public String getUrl(){
        return this.addressbar.getUrl();
    }

    /**
     * Getter for the address bar of the Browsr screen
     * @return address bar of the Browsr screen
     */
    public AddressBarComponent getAddressbar() {
        return addressbar;
    }

    /**
     * Getter for the bookmark bar of the Browsr screen
     * @return bookmark bar of the Browsr screen
     */
    public BookmarkBarComponent getBookmarkbar() {
        return bookmarkbar;
    }

    /**
     * @return rootpane of the browsr
     */
    public Pane getRootpane() {
        return this.rootpane;
    }

    /**
     * Setter for the rootpane of the browsr
     * @param pane
     */
    public void setRootpane(Pane pane) {
        this.rootpane = pane;
    }
}