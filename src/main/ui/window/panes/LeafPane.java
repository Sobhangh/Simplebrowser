package ui.window.panes;

import domain.Contentspan;
import ui.component.DocumentComponent;
import ui.listeners.EditListener;
import ui.listeners.LinkClickedListener;
import ui.util.ToComponentUtil;
import ui.window.screens.BrowsrScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LeafPane extends Pane{

    /**
     * The DocumentComponent of the pane
     */
    private DocumentComponent doc;

    /**
     * The parent of the pane
     */
    private NonLeafPane parent;

    /**
     * True if ctrl key is pressed, else false
     */
    private boolean ctrlPressed;

    /**
     * The url of the pane, default is https://
     */
    private String url = "https://";

    /**
     * The contentspan of the pane
     */
    private Contentspan contentspan;

    /**
     * Constructor for the LeafPane class
     *
     * @param x         The x-coordinate of the pane
     * @param y         The y-coordinate of the pane
     * @param width     The width of the pane
     * @param height    The height of the pane
     * @param doc       The DocumentComponent of the pane
     * @param parent    The parent of the pane
     * @param screen    The BrowsrScreen of the pane
     */
    public LeafPane(int x, int y, int width, int height, DocumentComponent doc,NonLeafPane parent, BrowsrScreen screen){
        super( x,  y,  width,  height, screen);
        setParent(parent);
        setCtrlPressed(false);
        setFocused(false);
        try {
            this.setDoc(doc);
        }catch (NullPointerException e){

        }

    }

    /**
     * Getter for the parent of the pane
     * @return The NonLeafPane parent of the pane
     */
    public NonLeafPane getParent() {
        return parent;
    }

    /**
     * Setter for the parent of the pane
     * @param parent
     */
    public void setParent(NonLeafPane parent) {
        this.parent = parent;
    }

    /**
     * Setter for the DocumentComponent of the pane
     * @param doc
     */
    public void setDoc(DocumentComponent doc) {
        if(doc ==null){
            throw new NullPointerException();
        }
        this.doc = doc;
        doc.setXr(getX());
        doc.setYr(getY());
        doc.setHeight(getHeight());
        doc.setWidth(getWidth());
        System.out.println("Listere added to the doc");
        doc.addLinkClickedListener((link) -> handleLinkClick(link));
        doc.addEditListener((editmode -> handleEditModeChanged(editmode)));
    }


    @Override
    public void paint(Graphics g) {
        Graphics2D paneGraphics = (Graphics2D) g.create();
        paneGraphics.setClip(getX() - 1,getY()- 1,getWidth() + 1,getHeight() + 1);
        doc.paint(paneGraphics);

        paneGraphics.setColor(Color.BLACK);
        Stroke oldStroke = paneGraphics.getStroke();
        if(isFocused()){
            paneGraphics.setColor(Color.BLUE);
            paneGraphics.setStroke(new BasicStroke(2));
        }
        paneGraphics.drawRect(getX(),getY(),getWidth(),getHeight());
        paneGraphics.setStroke(oldStroke);
        paneGraphics.setColor(Color.BLACK);
    }


    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if(isPositionInComponent(x,y)){
            if (isFocused()) {
                if(doc!=null) {
                    this.doc.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
                }
            } else {
                setFocused(true);
            }
        }
        else if (!this.getScreen().getAddressbar().isPositionInComponent(x, y)) {
            setFocused(false);
        }
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        if (isFocused()){
            if (id == 401 && keyCode == 17) {
                setCtrlPressed(true);
                this.doc.handleKeyEvent(id,keyCode,keyChar,modifiersEx);
            }
            else if (id == 402 && keyCode == 17) {
                setCtrlPressed(false);
                this.doc.handleKeyEvent(id,keyCode,keyChar,modifiersEx);
            }
            if (isCtrlPressed() && id == 401 && keyCode == 88) {
                // if delete x = 88
                if ( this.getParent() != null ) {
                    this.getParent().removePane(this);
                } else {
                    // if no parents then rootpane was deleted so we create a new rootpane.
                    this.getScreen().setPane();
                }
            }
            if(isCtrlPressed() && id == 402 && (keyCode == 72 || keyCode == 86)) {
                DocumentComponent d = (DocumentComponent) getDoc().copy();
                NonLeafPane nonleaf;
                //if split
                if (keyCode == 86) {
                    //if vertical v = 86
                    nonleaf = new NonLeafPane(getX(), getY(), getWidth(), getHeight(), getScreen(), null,
                            getParent(), 1);
                    nonleaf.setFocused(true);
                    // dimensions of the document has to be updated also
                    LeafPane upper = new LeafPane(getX(), getY(), getWidth(), getHeight() / 2, getDoc(), nonleaf, getScreen());
                    upper.setUrl(this.getUrl());
                    upper.setContentspan(this.getContentspan());
                    upper.setFocused(true);

                    LeafPane lower = new LeafPane(getX(), getY() + getHeight() / 2, getWidth(), getHeight() / 2,
                            d, nonleaf, getScreen());
                    lower.setUrl(this.getUrl());
                    lower.setContentspan(this.getContentspan());
                    lower.setFocused(false);

                    List<Pane> panes = new ArrayList<>(List.of(upper, lower));
                    nonleaf.setPanes(panes);

                } else {
                    //if horizontal h = 72
                    nonleaf = new NonLeafPane(getX(), getY(), getWidth(), getHeight(), getScreen(), null,
                            getParent(), 0);
                    nonleaf.setFocused(true);

                    LeafPane left = new LeafPane(getX(), getY(), getWidth() / 2, getHeight(), getDoc(), nonleaf, getScreen());
                    left.setUrl(this.getUrl());
                    left.setContentspan(this.getContentspan());
                    left.setFocused(true);

                    LeafPane right = new LeafPane(getX() + getWidth() / 2, getY(), getWidth() / 2, getHeight(),
                            d, nonleaf, getScreen());
                    right.setUrl(this.getUrl());
                    right.setContentspan(this.getContentspan());
                    right.setFocused(false);

                    List<Pane> panes = new ArrayList<>(List.of(left, right));
                    nonleaf.setPanes(panes);
                }
                List<LinkClickedListener> c = this.getLinkClickedListeners();
                List<EditListener> e = this.getEditListeners();
                c.forEach(nonleaf::addLinkClickedListener);
                e.forEach(nonleaf::addEditListener);
                if (this.getParent() != null) {
                    getParent().replacePanes(this, nonleaf);
                } else {
                    // if no parent then it was the rootpane so the rootpane has to be changed to a nonleaf pane.
                    this.getScreen().setRootpane(nonleaf);
                }
            } else {
                this.doc.handleKeyEvent(id, keyCode, keyChar, modifiersEx);
            }
        }
    }

    /**
     * Getter for the DocumentComponent of the pane
     * @return  The DocumentComponent of the pane
     */
    public DocumentComponent getDoc() {
        return doc;
    }

    /**
     * Getter for the ctrlPressed boolean
     * @return
     */
    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    /**
     * Setter for the ctrlPressed boolean
     * @param ctrlPressed
     */
    public void setCtrlPressed(boolean ctrlPressed) {
        this.ctrlPressed = ctrlPressed;
    }

    /**
     * Getter for the url of the pane
     * @return  The url string of the pane
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for the url of the pane
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void update(Contentspan c, FontMetrics m) {
        System.out.println("updating document in pane");
        this.contentspan = c;
        this.doc.setComponent(ToComponentUtil.toComponent(getX(), getY(), c, m));
    }

    /**
     * Getter for the Contentspan of the pane
     * @return  The Contentspan of the pane
     */
    public Contentspan getContentspan() {
        return this.contentspan;
    }

    /**
     * Setter for the Contentspan of the pane
     * @param contentspan
     */
    public void setContentspan(Contentspan contentspan) {
        this.contentspan = contentspan;
    }

    /**
     * Setter X coordinate
     * @param x
     * @throws IllegalArgumentException if given x coordinate is negative
     */
    public void setX(int x){
        if(x < 0){
            throw new IllegalArgumentException("X coordinate cannot be negative");
        }
        super.setX(x);
        if(this.doc!=null) {
            this.doc.setXr(x);
        }
    }

    /**
     * Setter Y coordinate
     * @param y
     * @throws IllegalArgumentException if given y coordinate is negative
     */
    public void setY(int y){
        if(y < 0){
            throw new IllegalArgumentException("Y coordinate cannot be negative");
        }
        super.setY(y);
        if(this.doc!=null) {
            this.doc.setYr(y);
        }

    }

    /**
     * Setter width
     * @param width
     * @throws IllegalArgumentException if the given width is negative
     */
    public void setWidth(int width){
        if(width < 0){
            throw new IllegalArgumentException("width cannot be negative");
        }
        super.setWidth(width);
        if(doc!=null) {
            this.doc.setWidth(width);
        }
    }

    /**
     * Setter height
     * @param height
     * @throws IllegalArgumentException if the given height is negative
     */
    public void setHeight(int height){
        if(height < 0){
            throw new IllegalArgumentException("height cannot be negative");
        }
        super.setHeight(height);
        if(doc!=null) {
            this.doc.setHeight(height);
        }
    }
}
