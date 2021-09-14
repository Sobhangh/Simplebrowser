package ui.component;

import ui.listeners.ClickListener;
import ui.listeners.EditListener;
import ui.listeners.InputListener;
import ui.listeners.LinkClickedListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TableRowComponent extends Component{
    /**
     * Components for the cells of the row
     */
    private List<Component> cells;

    /**
     * Constructor for the TableRowComponent class
     * @param x x coordinate of the TableRow Component
     * @param y y coordinate of the TableRow Component
     * @param cells cells of the TableRow Component
     */
    public TableRowComponent(int x, int y, List<Component> cells){
        super(x, y, 0, 0);
        setCells(cells);
        setHeightByCells();
        setWidthByCells();
    }

    /**
     * Constructor for the TableRowComponent class
     * @param x x coordinate of the TableRow Component
     * @param y y coordinate of the TableRow Component
     * @param width width of the TableRow Component
     * @param height height of the TableRow Component
     * @param cells cells of the TableRow Component
     */
    public TableRowComponent(int x, int y, int width, int height, List<Component> cells) {
        super(x, y, width, height);
        setCells(cells);
    }

    /**
     * Method to set the x coordinate of each cell considering the width of each cell
     */
    public void setXCoordinatesOfCells(){
        int currentX = getX();
        for(Component c: cells){
            c.setX(currentX);

            // add the width of the current cell to the x coordinate
            currentX += c.getWidth();
        }
    }

    @Override
    public void paint(Graphics g) {
        for(Component cell: cells){
            g.drawRect(cell.getX(), getY(), cell.getWidth(), getHeight());
            cell.paint(g);
        }
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        cells.stream().forEach(r -> r.handleMouseEvent(id, x, y, clickCount, button, modifiersEx));
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        cells.stream().forEach(r -> r.handleKeyEvent(id, keyCode, keyChar, modifiersEx));

    }

    @Override
    public Component copy() {
        List<Component> r = new ArrayList<>();
        for (Component c : getCells()) {
            r.add(c.copy());
        }
        return new TableRowComponent(getX(), getY(), r);
    }

    @Override
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY) {
        setLowerLimitY(lowerLimitY);
        setUpperLimitY(upperLimitY);
        setY(getY() - delta);
        cells.stream().forEach(r -> r.handleVerticalScroll(delta, lowerLimitY, upperLimitY));
    }

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {
        setLowerLimitX(lowerLimitX);
        setUpperLimitX(upperLimitX);setX(getX() - delta);
        cells.stream().forEach(r -> r.handleHorizontalScroll(delta, lowerLimitX, upperLimitX));
        setXCoordinatesOfCells();
    }

    /**
     * Getter of the cells of the row
     * @return cells of the row
     */
    public List<Component> getCells(){
        return this.cells;
    }

    /**
     * Setter of the cells of the row
     * @param cells
     * @throws IllegalArgumentException if the given cells are null
     */
    public void setCells(List<Component> cells){
        if(cells == null){
            throw new IllegalArgumentException("Cells cannot be null");
        }
        this.cells = List.copyOf(cells);
    }

    /**
     * Method to set the width of the row by accumulating the widths of all the cells
     */
    public void setWidthByCells(){
        setWidth(getCells().stream().mapToInt(c -> c.getWidth()).sum());
    }

    /**
     * Method to set the height of the row to the maximum height of the cells
     */
    public void setHeightByCells(){
        setHeight(getCells().stream().mapToInt(c -> c.getHeight()).max().getAsInt());
    }

    public void setXr(int x){
        this.setX(x);
        int w =x;
        for(Component r: cells){
            r.setXr(w);
            w+=r.getWidth();
        }
    }

    public void setYr(int y) {
        this.setY(y);
        for (Component r : cells) {
            r.setYr(y);
        }
    }

    @Override
    public void setY(int y){
        super.setY(y);
        if(cells != null){
            setYCoordinates();
        }
    }

    /**
     * Set y coordinates of all the cells to the y coordinate of this table row
     */
    public void setYCoordinates(){
        for(Component cell: getCells()){
            cell.setY(getY());
        }
    }

    @Override
    public void setUpperLimitX(int upperLimitX) {
        super.setUpperLimitX(upperLimitX);
        cells.forEach(c -> c.setUpperLimitX(upperLimitX));
    }

    @Override
    public void setLowerLimitX(int lowerLimitX) {
        super.setLowerLimitX(lowerLimitX);
        cells.forEach(c -> c.setLowerLimitX(lowerLimitX));
    }

    @Override
    public void setUpperLimitY(int upperLimitY) {
        super.setUpperLimitY(upperLimitY);
        cells.forEach(c -> c.setUpperLimitY(upperLimitY));
    }

    @Override
    public void setLowerLimitY(int lowerLimitY) {
        super.setLowerLimitY(lowerLimitY);
        cells.forEach(c -> c.setLowerLimitY(lowerLimitY));
    }

    @Override
    public void addClickListener(ClickListener listener){
        for(Component cell: this.cells){
            cell.addClickListener(listener);
        }
    }

    @Override
    public void removeClickListener(ClickListener listener){
        for(Component cell: this.cells){
            cell.removeClickListener(listener);
        }
    }

    @Override
    public void addLinkClickedListener(LinkClickedListener listener){
        for(Component cell: this.cells){
            cell.addLinkClickedListener(listener);
        }
    }

    @Override
    public void removeLinkClickedListener(LinkClickedListener listener){
        for(Component cell: this.cells){
            cell.removeLinkClickedListener(listener);
        }
    }

    @Override
    public void addInputListener(InputListener listener){
        for(Component cell: this.cells){
            cell.addInputListener(listener);
        }
    }

    @Override
    public void removeInputListener(InputListener listener){
        for(Component cell: this.cells){
            cell.removeInputListener(listener);
        }
    }

    @Override
    public void addEditListener(EditListener listener){
        for(Component cell: this.cells){
            cell.addEditListener(listener);
        }
    }

    @Override
    public void removeEditListener(EditListener listener){
        for(Component cell: this.cells){
            cell.removeEditListener(listener);
        }
    }
}
