package ui.window.panes;

import domain.Contentspan;
import ui.listeners.EditListener;
import ui.listeners.LinkClickedListener;
import ui.window.screens.BrowsrScreen;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NonLeafPane extends Pane{

    /**
     * List containing the children of the NonLeafPane
     */
    List<Pane> panes = new ArrayList<>(2);

    /**
     * The parent of the pane
     */
    private NonLeafPane parent;

    /**
     * The split type of the pane
     * 0 is a horizontal split
     * 1 is a vertical split
     */
    int splitType;

    /**
     * True if the latest mouse event was on the border of the pane
     */
    private boolean onBorder=false;

    /**
     * Used to calculate the new ratio of the pane after a separator was dragged
     */
    private final double percent = 0.1;

    /**
     * Constructor for the non NonLeafPane class.
     *
     * @param x             x-coordinate of the pane
     * @param y             y-coordinate of the pane
     * @param width         Width of the pane
     * @param height        Height of the pane
     * @param panes         The children of the pane
     * @param parent        The parent of the pane
     * @param splittype     0 is for a horizontal split (ctrl+h), 1 is for a vertical split (ctrl+v).
     */
    public NonLeafPane(int x, int y, int width, int height, BrowsrScreen screen, List<Pane> panes, NonLeafPane parent, int splittype){
        super( x,  y,  width,  height, screen);
        try{
            setPanes(panes);
        }catch (NullPointerException e){

        }
        setParent(parent);
        setSplitType(splittype);
    }

    /**
     * Getter of the parent of the pane
     * @return  The parent of the pane
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
     * Setter for the children of the pane
     * @param panes
     */
    public void setPanes(List<Pane> panes) {
        if (panes==null) {
            throw  new NullPointerException();
        }
        this.panes = panes;
        this.setRatio(0.5);
        for (Pane p : panes) {
            p.addLinkClickedListener((link) -> handleLinkClick(link));
            p.addEditListener((editmode -> handleEditModeChanged(editmode)));
        }
    }

    @Override
    public void addLinkClickedListener(LinkClickedListener listener){
        if(listener == null){
            throw new IllegalArgumentException("LinkClickedListener cannot be null");
        }
        for (Pane p : panes) {
            p.addLinkClickedListener(listener);
        }
    }

    /**
     * Getter for the children of the pane
     * @return  The children of the pane
     */
    public List<Pane> getPanes() {
        return this.panes;
    }

    /**
     * Replaces a children of the pane with a new pane
     * @param old       The child to be replaced
     * @param newPane   The new child that will replace the old
     */
    public void replacePanes(Pane old,Pane newPane){
        boolean swap = false;
        if (panes.get(0) == old) {
            swap = true;
        }
        old.setFocused(false);
        panes.remove(old);
        List<LinkClickedListener> c = old.getLinkClickedListeners();
        List<EditListener> e = old.getEditListeners();
        panes.add(newPane);
        c.stream().forEach(x->newPane.addLinkClickedListener(x));
        e.stream().forEach(x->newPane.addEditListener(x));
        if (swap) {
            Collections.swap(panes, 0, 1);
        }
    }

    /**
     * Called when a pane needs to be removed.
     * This method will remove the selected pane and will then call setPanes to set all the other panes in the right position.
     *
     * @param p     The pane to remove.
     */
    public void removePane(LeafPane p){
        p.setFocused(false);
        panes.remove(p);
        Pane pane = panes.get(0);
        setPanes(pane, this);
    }

    /**
     * Called in removePane.
     * This method will set the brother of the removed pane (pane) to fill the new blank space.
     * If pane is a nonleafpane, then the method shiftpanes is called to shift all his children on the right position.
     *
     * @param pane      The brother of the removed pane.
     * @param parent    The parent of the param pane.
     */
    public void setPanes(Pane pane, NonLeafPane parent) {
        if (pane instanceof LeafPane) {
            LeafPane newLeaf = new LeafPane(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight(),
                    ((LeafPane) pane).getDoc(), parent.getParent(), pane.getScreen());
            newLeaf.setFocused(true);
            if (parent.getParent() != null) {
                parent.getParent().replacePanes(parent, newLeaf);
            } else {
                newLeaf.getScreen().setRootpane(newLeaf);
            }

        } else if (pane instanceof NonLeafPane) {
            NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), parent.getY(), parent.getWidth(), parent.getHeight(),
                    pane.getScreen(), ((NonLeafPane) pane).getPanes(), parent.getParent(), ((NonLeafPane) pane).getSplitType());
            newNonLeaf.setFocused(true);
            if (parent.getParent() != null) {
                parent.getParent().replacePanes(parent, newNonLeaf);
            } else {
                newNonLeaf.getScreen().setRootpane(newNonLeaf);
            }
            shiftPanes(newNonLeaf, parent.getSplitType());
        }
    }

    @Override
    public String getUrl() {
        for (Pane child: panes) {
            if (child.isFocused()) {
                return child.getUrl();
            }
        }
        return null;
    }

    @Override
    public void setUrl(String url) {
        for (Pane child: panes) {
            if (child.isFocused()) {
                child.setUrl(url);
            }
        }
    }

    @Override
    public void update(Contentspan c, FontMetrics m) {
        for (Pane child: panes) {
            if (child.isFocused()) {
                child.update(c, m);
            }
        }
    }

    @Override
    public Contentspan getContentspan() {
        for (Pane child: panes) {
            if (child.isFocused()) {
                return child.getContentspan();
            }
        }
        return null;
    }

    /**
     * Called from setPanes.
     * This recursive method will shift all the children of a non leafpane to the right position.
     *
     * @param parent        The non leafpane whose children have to be shifted in the right position.
     * @param split         The splitType of the parent of the parent. To know in which direction to shift.
     */
    public void shiftPanes(NonLeafPane parent, int split) {
        Pane childPaneOne = parent.getPanes().get(0);
        Pane childPaneTwo = parent.getPanes().get(1);

        // If the new blank space is in a horizontal split => children will be shifted to the left or to the right.
        if (split == 0) {
            // If the children are split horizontally.
            if (parent.getSplitType() == 0) {
                if (childPaneOne instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX(), parent.getY(), childPaneOne.getWidth() * 2, childPaneOne.getHeight(),
                            ((LeafPane) childPaneOne).getDoc(), parent, childPaneOne.getScreen());
                    if (parent.isFocused()) {
                        newLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newLeaf);

                } else if (childPaneOne instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), parent.getY(), childPaneOne.getWidth() * 2,
                            childPaneOne.getHeight(), childPaneOne.getScreen(), ((NonLeafPane) childPaneOne).getPanes(),
                            parent, ((NonLeafPane) childPaneOne).getSplitType());
                    if (parent.isFocused()) {
                        newNonLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }
                if (childPaneTwo instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX() + childPaneTwo.getWidth() * 2, parent.getY(),
                            childPaneTwo.getWidth() * 2, childPaneTwo.getHeight(), ((LeafPane) childPaneTwo).getDoc(),
                            parent, childPaneTwo.getScreen());
                    parent.replacePanes(childPaneTwo, newLeaf);

                } else if (childPaneTwo instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX() + childPaneTwo.getWidth() * 2, parent.getY(), childPaneTwo.getWidth() * 2,
                            childPaneTwo.getHeight(), childPaneTwo.getScreen(),((NonLeafPane) childPaneTwo).getPanes(),
                            parent, ((NonLeafPane) childPaneTwo).getSplitType());
                    parent.replacePanes(childPaneTwo, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }

                // If the children are split vertically.
            } else if (parent.getSplitType() == 1) {
                if (childPaneOne instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX(), childPaneOne.getY(), childPaneOne.getWidth() * 2, childPaneOne.getHeight(),
                            ((LeafPane) childPaneOne).getDoc(), parent, childPaneOne.getScreen());
                    if (parent.isFocused()) {
                        newLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newLeaf);

                } else if (childPaneOne instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), childPaneOne.getY(), childPaneOne.getWidth() * 2,
                            childPaneOne.getHeight(), childPaneOne.getScreen(), ((NonLeafPane) childPaneOne).getPanes(),
                            parent, ((NonLeafPane) childPaneOne).getSplitType());
                    if (parent.isFocused()) {
                        newNonLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }
                if (childPaneTwo instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX(), childPaneTwo.getY(),
                            childPaneTwo.getWidth() * 2, childPaneTwo.getHeight(), ((LeafPane) childPaneTwo).getDoc(),
                            parent, childPaneTwo.getScreen());
                    parent.replacePanes(childPaneTwo, newLeaf);

                } else if (childPaneTwo instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), childPaneTwo.getY(), childPaneTwo.getWidth() * 2,
                            childPaneTwo.getHeight(), childPaneTwo.getScreen(), ((NonLeafPane) childPaneTwo).getPanes(),
                            parent, ((NonLeafPane) childPaneTwo).getSplitType());
                    parent.replacePanes(childPaneTwo, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }
            }

            // If the new blank space is in a vertical split => children will be shifted up or down.
        } else if (split == 1) {
            // If the children are split horizontally.
            if (parent.getSplitType() == 0) {
                if (childPaneOne instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX(), parent.getY(), childPaneOne.getWidth(), childPaneOne.getHeight() * 2,
                            ((LeafPane) childPaneOne).getDoc(), parent, childPaneOne.getScreen());
                    if (parent.isFocused()) {
                        newLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newLeaf);

                } else if (childPaneOne instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), parent.getY(), childPaneOne.getWidth(),
                            childPaneOne.getHeight() * 2, childPaneOne.getScreen(), ((NonLeafPane) childPaneOne).getPanes(),
                            parent, ((NonLeafPane) childPaneOne).getSplitType());
                    if (parent.isFocused()) {
                        newNonLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }
                if (childPaneTwo instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(childPaneTwo.getX(), parent.getY(),
                            childPaneTwo.getWidth(), childPaneTwo.getHeight() * 2, ((LeafPane) childPaneTwo).getDoc(),
                            parent, childPaneTwo.getScreen());
                    parent.replacePanes(childPaneTwo, newLeaf);

                } else if (childPaneTwo instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(childPaneTwo.getX(), parent.getY(), childPaneTwo.getWidth(),
                            childPaneTwo.getHeight() * 2, childPaneTwo.getScreen(), ((NonLeafPane) childPaneTwo).getPanes(),
                            parent, ((NonLeafPane) childPaneTwo).getSplitType());
                    parent.replacePanes(childPaneTwo, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }

                // If the children are split vertically.
            } else if (parent.getSplitType() == 1) {
                if (childPaneOne instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX(), parent.getY(), childPaneOne.getWidth(), childPaneOne.getHeight() * 2,
                            ((LeafPane) childPaneOne).getDoc(), parent, childPaneOne.getScreen());
                    if (parent.isFocused()) {
                        newLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newLeaf);

                } else if (childPaneOne instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), parent.getY(), childPaneOne.getWidth(),
                            childPaneOne.getHeight() * 2, childPaneOne.getScreen(), ((NonLeafPane) childPaneOne).getPanes(),
                            parent, ((NonLeafPane) childPaneOne).getSplitType());
                    if (parent.isFocused()) {
                        newNonLeaf.setFocused(true);
                    }
                    parent.replacePanes(childPaneOne, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }
                if (childPaneTwo instanceof LeafPane) {
                    LeafPane newLeaf = new LeafPane(parent.getX(), parent.getY() + childPaneTwo.getHeight() * 2,
                            childPaneTwo.getWidth(), childPaneTwo.getHeight() * 2, ((LeafPane) childPaneTwo).getDoc(),
                            parent, childPaneTwo.getScreen());
                    parent.replacePanes(childPaneTwo, newLeaf);

                } else if (childPaneTwo instanceof NonLeafPane) {
                    NonLeafPane newNonLeaf = new NonLeafPane(parent.getX(), parent.getY() + childPaneTwo.getHeight() * 2, childPaneTwo.getWidth(),
                            childPaneTwo.getHeight() * 2, childPaneTwo.getScreen(), ((NonLeafPane) childPaneTwo).getPanes(),
                            parent, ((NonLeafPane) childPaneTwo).getSplitType());
                    parent.replacePanes(childPaneTwo, newNonLeaf);
                    shiftPanes(newNonLeaf , split);
                }
            }
        }
    }

    /**
     * Adds a new child to the pane
     * @param p     The child to be added
     */
    public void addPane(Pane p){
        panes.add(p);
        p.addLinkClickedListener((link) -> handleLinkClick(link));
        p.addEditListener((editmode -> handleEditModeChanged(editmode)));
    }

    /**
     * Setter for the split type of the pane
     * @param split
     */
    private void setSplitType(int split) {
        this.splitType = split;
    }

    /**
     * Getter for the split type of the pane
     * @return  The split type of the pane
     */
    public int getSplitType() {
        return this.splitType;
    }

    @Override
    public void paint(Graphics g) {
        for(Pane p : panes){
            p.paint(g);
        }
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        if(this.onBorder){
            System.out.println("draging......");
            if(id==MouseEvent.MOUSE_RELEASED){
                if(getSplitType()==0){
                    if(insidePercentageOfPaneX(x)==0){
                        double r = (double)(x-getX())/getWidth();
                        this.setRatio(r);
                        this.setWidth(getWidth());
                    }
                    else if(insidePercentageOfPaneX(x)==-1){
                        this.setRatio(percent/2);
                        this.setWidth(getWidth());
                    }
                    else{
                        this.setRatio(1-percent/2);
                        this.setWidth(getWidth());
                    }
                }
                else{
                    if(insidePercentageOfPaneY(y)==0){
                        double r = (double)(y-getY())/getHeight();
                        this.setRatio(r);
                        this.setHeight(getHeight());
                    }
                    else if(insidePercentageOfPaneY(y)==-1){
                        this.setRatio(percent/2);
                        this.setHeight(getHeight());
                    }
                    else{
                        this.setRatio(1-percent/2);
                        this.setHeight(getHeight());
                    }
                }
                this.onBorder =false;
            }
        }
        else {
            if (isPositionInComponent(x, y)) {
                setFocused(true);
                isOnBorder(id,x,y);
            } else if (!this.getScreen().getAddressbar().isPositionInComponent(x, y)) {
                setFocused(false);
            }
            for (Pane p : panes) {
                p.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
            }
        }
    }

    /**
     * Checks if the mouse event is on the border of the pane
     * @param id    The event id
     * @param x     The x-coordinate of the mouse event
     * @param y     The y-coordinate of the mouse event
     */
    private void isOnBorder(int id,int x, int y){
        if(MouseEvent.MOUSE_PRESSED==id) {
            if (getSplitType() == 0) {
                this.onBorder = x <= (getX() + (getRatio()+0.01) * getWidth()) && x >= (getX() + (getRatio()-0.01) * getWidth()) ;
            } else {
                this.onBorder = y <= (getY() + (getRatio()+0.01) * getHeight()) && y >= (getY() + (getRatio()-0.01) * getHeight());
            }
        }
    }

    /**
     * Checks if the mouse event was in the margin of the separator
     * @param x The x-coordinate of the mouse event
     * @return 0 if inside  , -1 if on the left side , 1 if on the right side of the margin
     */
    private int insidePercentageOfPaneX(int x){
        double margin = getWidth()*percent/2;
        double leftborder = getX() + margin;
        double rightborder = getX() +getWidth() -margin;
        if(x>leftborder && x<rightborder){
            return 0;
        }
        if(x<=leftborder){
            return -1;
        }
        return 1;
    }

    /**
     * Checks if the mouse event was in the margin of the separator
     * @param y The y-coordinate of the mouse event
     * @return 0 if inside, -1 if under the top border, 1 if above the bottom border of the margin
     */
    private int insidePercentageOfPaneY(int y){
        double percent = 0.1;
        double margin = getHeight()*percent/2;
        double topborder = getY() + margin;
        double downborder = getY() +getHeight() -margin;
        if(y>topborder && y<downborder){
            return 0;
        }
        if(y<=topborder){
            return -1;
        }
        return 1;
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        if (isFocused()) {
            // It is this way to avoid a concurrentModification exception.
            this.panes.get(0).handleKeyEvent(id, keyCode, keyChar, modifiersEx);
            if (this.panes.size() > 1) {
                this.panes.get(1).handleKeyEvent(id, keyCode, keyChar, modifiersEx);
            }
        }
    }
    /**
     * Setter width
     * @param width
     * @throws IllegalArgumentException if the given width is negative
     */
    public void setWidth(int width){
        int oldwidth = this.getWidth();
        super.setWidth(width);
        if(getSplitType()==0){
            Pane left;
            Pane right;
            if(this.panes.get(0).getX()<panes.get(1).getX()){
                left=this.panes.get(0);
                right=this.panes.get(1);
            }
            else{
                left=this.panes.get(1);
                right=this.panes.get(0);
            }
            //double ratio = (double) (left.getWidth())/oldwidth;
            int lwidth = (int) (this.getRatio()*width);
            int rwidth = width - lwidth;
            System.out.println("left:.......");
            left.setWidth(lwidth);
            right.setX(left.getX()+lwidth);
            System.out.println("right:.......");
            right.setWidth(rwidth);
        }
        else{
            panes.stream().forEach(p->p.setWidth(width));
        }
    }

    /**
     * Setter height
     * @param height
     * @throws IllegalArgumentException if the given height is negative
     */
    public void setHeight(int height){
        int oldheight = getHeight();
        super.setHeight(height);
        if(getSplitType()==1){
            Pane up;
            Pane down;
            if(this.panes.get(0).getY()<panes.get(1).getY()){
                up=this.panes.get(0);
                down=this.panes.get(1);
            }
            else{
                up=this.panes.get(1);
                down=this.panes.get(0);
            }
            //double ratio = (double) (up.getHeight())/oldheight;
            int uheight = (int) (this.getRatio()*height);
            int dheight = height - uheight;
            up.setHeight(uheight);
            down.setY(up.getY()+uheight);
            down.setHeight(dheight);
            System.out.println(down.getX());
        }
        else{
            panes.stream().forEach(p->p.setHeight(height));
        }
    }

    /**
     * Setter X coordinate
     * @param x
     * @throws IllegalArgumentException if given x coordinate is negative
     */
    public void setX(int x){
        super.setX(x);
        if(getSplitType()==0){
            Pane left;
            Pane right;
            if(this.panes.get(0).getX()<panes.get(1).getX()){
                left=this.panes.get(0);
                right=this.panes.get(1);
            }
            else{
                left=this.panes.get(1);
                right=this.panes.get(0);
            }
            left.setX(x);
            right.setX(x+left.getWidth());
        }
        else{
            panes.stream().forEach(p->p.setX(x));
        }
    }

    /**
     * Setter Y coordinate
     * @param y
     * @throws IllegalArgumentException if given y coordinate is negative
     */
    public void setY(int y){
        super.setY(y);
        if(getSplitType()==1){
            Pane up;
            Pane down;
            if(this.panes.get(0).getY()<panes.get(1).getY()){
                up=this.panes.get(0);
                down=this.panes.get(1);
            }
            else{
                up=this.panes.get(1);
                down=this.panes.get(0);
            }
            up.setY(y);
            down.setY(y+up.getHeight());
        }
        else{
            panes.stream().forEach(p->p.setY(y));
        }

    }
}
