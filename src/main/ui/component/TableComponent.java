package ui.component;

import ui.listeners.ClickListener;
import ui.listeners.EditListener;
import ui.listeners.InputListener;
import ui.listeners.LinkClickedListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TableComponent extends Component{
    /**
     * RowComponents for the rows of the Table
     */
    private List<TableRowComponent> rows;

    /**
     * Constructor for the TableComponent class
     * @param x x coordinate of the Table Component
     * @param y y coordinate of the Table Component
     * @param rows rows of the Table Component
     */
    public TableComponent(int x, int y, List<TableRowComponent> rows){
        super(x, y, 0, 0);
        setRows(rows);
        setWidthByRows();
        setHeightByRows();
    }

    /**
     * Constructor for the TableComponent class
     * @param x x coordinate of the Table Component
     * @param y y coordinate of the Table Component
     * @param width width of the Table Component
     * @param height height of the Table Component
     * @param rows rows of the Table Component
     */
    public TableComponent(int x, int y, int width, int height, List<TableRowComponent> rows){
        super(x, y, width, height);
        setRows(rows);
    }

    @Override
    public void paint(Graphics g) {
        for(TableRowComponent row: rows){
            row.paint(g);
        }
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        rows.stream().forEach(r -> r.handleMouseEvent(id, x, y, clickCount, button, modifiersEx));
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        rows.stream().forEach(r -> r.handleKeyEvent(id, keyCode, keyChar, modifiersEx));
    }

    @Override
    public Component copy() {
        List<TableRowComponent> r = new ArrayList<>();
        for (TableRowComponent row : getRows()) {
            r.add((TableRowComponent) row.copy());
        }
        return new TableComponent(getX(), getY(), r);
    }

    @Override
    protected void handleVerticalScroll(int delta, int lowerLimitY, int upperLimitY) {
        setLowerLimitY(lowerLimitY);
        setUpperLimitY(upperLimitY);
        if(((getY() + getHeight() > upperLimitY) && delta > 0) || (getY() < lowerLimitY && delta < 0)){
            setY(getY() - delta);
            rows.stream().forEach(r -> r.handleVerticalScroll(delta, lowerLimitY, upperLimitY));
        }

    }

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {
        setLowerLimitX(lowerLimitX);
        setUpperLimitX(upperLimitX);
        if(((getX() + getWidth() > upperLimitX) && delta > 0) || (getX() < lowerLimitX && delta < 0)){
            setX(getX() - delta);
            rows.stream().forEach(r -> r.handleHorizontalScroll(delta, lowerLimitX, upperLimitX));
        }
    }

    @Override
    public void setY(int y){
        super.setY(y);
        if(rows != null){
            setYCoordinatesRows();
        }
    }

    /**
     * Set Y coordinates of rows taking into account the heights of the rows
     */
    public void setYCoordinatesRows(){
        int currentY = getY();
        for(TableRowComponent row: rows){
            row.setY(currentY);
            currentY += row.getHeight();
        }
    }

    /**
     * Getter for the rows of the Table
     * @return Row components of the table
     */
    public List<TableRowComponent> getRows(){
        return this.rows;
    }

    /**
     * Setter for rows of the table
     * @param rows
     * @throws IllegalArgumentException if the given rows are null
     */
    public void setRows(List<TableRowComponent> rows){
        if(rows == null){
            throw new IllegalArgumentException("Rows cannot be null");
        }
        this.rows = List.copyOf(rows);
        setColumnWidths();
    }

    /**
     * Method to set the widths of cells by taking the maximum width of its column
     */
    public void setColumnWidths(){
        // iterate over all columns
        if(rows.size() != 0){
            for(int i = 0; i < rows.get(0).getCells().size(); i++){
                final int finalI = i;
                // get max width in column
                int max = rows.stream().mapToInt(x -> {
                    if(finalI < x.getCells().size()){
                        return x.getCells().get(finalI).getWidth();
                    } else {
                        return 0;
                    }
                }).max().getAsInt();

                // set all widths for cells of column
                rows.stream().forEach(r -> {
                    if(finalI < r.getCells().size()){
                        r.getCells().get(finalI).setWidth(max);
                    }
                });
            }

            // update all x-coordinates & update the width of the row
            rows.stream().forEach(r -> {
                r.setWidthByCells();
                r.setXCoordinatesOfCells();
            });
        }
    }

    /**
     * Method to set the width of the table by taking the max width of the rows
     */
    public void setWidthByRows(){
        if(getRows().size() != 0){
            setWidth(getRows().stream().mapToInt(r -> r.getWidth()).max().getAsInt());
        }
    }

    /**
     * Method to set the height of the table by accumulating the heights of its rows
     */
    public void setHeightByRows(){
        if(getRows().size() != 0) {
            setHeight(rows.stream().mapToInt(r -> r.getHeight()).sum());
        }
    }

    @Override
    public void addClickListener(ClickListener listener){
        for(TableRowComponent row: this.rows){
            row.addClickListener(listener);
        }
    }

    @Override
    public void removeClickListener(ClickListener listener){
        for(TableRowComponent row: this.rows){
            row.removeClickListener(listener);
        }
    }

    @Override
    public void addLinkClickedListener(LinkClickedListener listener){
        for(TableRowComponent row: this.rows){
            row.addLinkClickedListener(listener);
        }
    }

    @Override
    public void removeLinkClickedListener(LinkClickedListener listener){
        for(TableRowComponent row: this.rows){
            row.removeLinkClickedListener(listener);
        }
    }

    @Override
    public void addInputListener(InputListener listener) {
        for(TableRowComponent row : this.rows) {
            row.addInputListener(listener);
        }
    }

    @Override
    public void removeInputListener(InputListener listener){
        for(TableRowComponent row: this.rows){
            row.removeInputListener(listener);
        }
    }

    @Override
    public void addEditListener(EditListener listener) {
        for(TableRowComponent row : this.rows) {
            row.addEditListener(listener);
        }
    }

    @Override
    public void removeEditListener(EditListener listener){
        for(TableRowComponent row: this.rows){
            row.removeEditListener(listener);
        }
    }

    public void setXr(int x){
        this.setX(x);
        for(TableRowComponent r: rows){
            r.setXr(x);
        }

    }

    public void setYr(int y){
        this.setY(y);
        int h =y;
        for(TableRowComponent r: rows){
            r.setYr(h);
            h +=r.getHeight();
        }

    }

    @Override
    public void setUpperLimitX(int upperLimitX) {
        super.setUpperLimitX(upperLimitX);
        rows.forEach(r -> r.setUpperLimitX(upperLimitX));
    }

    @Override
    public void setLowerLimitX(int lowerLimitX) {
        super.setLowerLimitX(lowerLimitX);
        rows.forEach(r -> r.setLowerLimitX(lowerLimitX));
    }

    @Override
    public void setUpperLimitY(int upperLimitY) {
        super.setUpperLimitY(upperLimitY);
        rows.forEach(r -> r.setUpperLimitY(upperLimitY));
    }

    @Override
    public void setLowerLimitY(int lowerLimitY) {
        super.setLowerLimitY(lowerLimitY);
        rows.forEach(r -> r.setLowerLimitY(lowerLimitY));
    }

}
