package ui.component;

import ui.listeners.EditListener;
import ui.listeners.InputListener;
import ui.util.Range;

import java.awt.*;
import java.util.ArrayList;

/**
 * https://stackoverflow.com/questions/9616716/how-to-draw-characters-only-partially-using-awt-or-another-drawing-mechanism
 */
public class InputComponent extends Component{
    /**
     * Current input that has been filled in
     */
    private String input = "";
    /**
     * Boolean indicating whether editMode is active or not
     */
    private boolean editMode;

    /**
     * Position of the cursor inside the input field
     */
    private int cursorPosition;

    /**
     * Range that represents which characters are selected
     */
    private final Range selectedRange = new Range(0, 0);

    /**
     * String that holds the input that was in place when input field entered editMode
     */
    private String inputAtFocus;

    /**
     * Offset between the frame of the input field and the text
     */
    private final int offset = 2;

    /**
     * Metrics used to display text
     */
    private FontMetrics metrics;

    /**
     * Boolean indicating whether shift key is pressed or not
     */
    private boolean shiftDown;

    /**
     * A horizontal scrollbar used to scroll through content when this doesn't fit
     */
    private HorizontalScrollBar scrollBar;

    /**
     * Integer that indicates the offset the text inside the input should be painted at when scrolled
     */
    private int scrollPosition;


    /**
     * Constructor of the InputComponent class
     *
     * @param x       x coordinate of the component
     * @param y       y coordinate of the component
     * @param height  height of the component
     * @param metrics metrics of the canvas
     */
    public InputComponent(int x, int y, int height, FontMetrics metrics) {
        super(x, y, 800, height);
        setMetrics(metrics);
        setEditMode(false);
        scrollBar = new HorizontalScrollBar(getX(), getY() + getHeight(), getWidth());
        scrollBar.addScrollListener(delta -> handleInternalHorizontalScroll(delta, getX(), getX() + getWidth()));
        setScrollBarSlider();
        setHeight(height + scrollBar.getHeight());
    }

    /**
     * Constructor of the InputComponent class
     *
     * @param x       x coordinate of the component
     * @param y       y coordinate of the component
     * @param width   width of the component
     * @param height  height of the component
     * @param metrics metrics of the canvas
     */
    public InputComponent(int x, int y, int width, int height, FontMetrics metrics, String input) {
        super(x, y, width, height);
        setInput(input);
        setMetrics(metrics);
        setEditMode(false);
        scrollBar = new HorizontalScrollBar(getX(), getY() + getHeight(), getWidth());
        scrollBar.addScrollListener(delta -> handleInternalHorizontalScroll(delta, getX(), getX() + getWidth()));
        setScrollBarSlider();
        setHeight(height + scrollBar.getHeight());
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Shape clip = g2d.getClip();
        g2d.clip(new Rectangle(getX(), getY(), getWidth() + getOffset(), getHeight() + getOffset()));

        g2d.drawRect(getX(), getY(), getWidth(), getHeightInput());
        /**
         * The cursor is drawn at cursorPosition
         * If editMode is not active, the cursor is removed
         */
        if (getEditMode()) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(getX() + (getOffset()/2), getY() + 1, getWidth() - (getOffset()/2), getHeightInput() - (getOffset()/2));
            markSelected(g2d);
            g2d.drawString(input, getX() + getOffset() + getScrollPosition(), getY() + getHeightInput() - getMetrics().getDescent() + getOffset());
            int x_cursor = g2d.getFontMetrics().stringWidth(input.substring(0, cursorPosition)) + getScrollPosition() + getX() + getOffset();
            int end_y = getY() + getHeightInput();
            g2d.drawLine(x_cursor, getY(), x_cursor, end_y);
        } else {
            g2d.drawString(input, getX() + getScrollPosition() + getOffset(), getY() + getHeightInput() - getMetrics().getDescent() + getOffset());
            try {
                g2d.clearRect(g2d.getFontMetrics().stringWidth(input.substring(0, cursorPosition)) + getX() + getOffset(), getY() + 1, 1, getHeightInput() - 1);
            } catch (StringIndexOutOfBoundsException e) {
            }
        }
        scrollBar.paint(g2d);
        g2d.setClip(clip);
    }

    public int getHeightInput() {
        return getHeight() - scrollBar.getHeight();
    }

    @Override
    public void handleMouseEvent(int id, int x, int y, int clickCount, int button, int modifiersEx) {
        scrollBar.handleMouseEvent(id, x, y, clickCount, button, modifiersEx);
        if (id == 500) {
            if (isPositionInComponent(x, y)) {
                /**Input field is entering editMode, so all text should be selected and the that text should be stored for when the escape button is pressed*/
                if (!getEditMode()) {
                    setCursorPosition(input.length());
                    setInputAtFocus(this.input);
                    selectedRange.setLeft(0);
                    selectedRange.setRight(input.length());
                    this.setEditMode(true);
                    editModeChanged();

                } else {
                    this.selectedRange.setLeft(0);
                    this.selectedRange.setRight(0);
                }
            } else {
                if (this.getEditMode()) {
                    this.setEditMode(false);
                    inputChanged();
                    editModeChanged();
                }
            }
        }
    }

    @Override
    public void handleKeyEvent(int id, int keyCode, char keyChar, int modifiersEx) {
        if (this.getEditMode()) {
            /**
             * Enter key is pressed so editMode is exited
             */
            if (id == 401 && (keyCode == 13 || keyCode == 10)) {
                this.setEditMode(false);
                this.resetSelectedRange();
                inputChanged();
                editModeChanged();
            }
            /**
             * Remove a character
             */
            else if (id == 401 && keyCode == 8) {
                if (this.input.length() > 0) {
                    this.removeChar();
                }
            }
            /**
             * Delete a character
             */
            else if (id == 401 && (keyCode == 46 || keyCode == 127)) {
                if (this.input.length() > 0) {
                    this.deleteChar();
                }
            }
            /**
             * Escape is pressed, exit Editmode, reset input and selectedRange
             */
            else if (id == 401 && keyCode == 27) {
                this.setEditMode(false);
                this.input = this.inputAtFocus;
                this.resetSelectedRange();
                this.moveCursorToEndOfInput();
                editModeChanged();
                this.setScrollPosition(0);
                this.scrollBar.setXslider(scrollBar.getX());
                this.setScrollBarSlider();
            }
            /**
             * Shift is pressed, ShiftDown to true
             */
            else if (id == 401 && keyCode == 16) {
                this.setShiftDown(true);
            }
            /**
             * Shift is released, ShiftDown to false
             */
            else if (id == 402 && keyCode == 16) {
                this.setShiftDown(false);
            }
            /**
             * Home Key Logic when shift button is not pressed
             */
            else if (id == 401 && (keyCode == 36 || keyCode == 38) && !this.getShiftDown()) {
                setCursorPosition(0);
            }
            /**
             * Home Key Logic when shift button is pressed
             */
            else if (id == 401 && (keyCode == 36 || keyCode == 38) && this.getShiftDown()) {
                this.setSelectedRange(0, this.getCursorPosition());
                setCursorPosition(0);
            }
            /**
             * End Key Logic when shift button is not pressed
             */
            else if (id == 401 && (keyCode == 35 || keyCode == 40) && !this.getShiftDown()) {
                setCursorPosition(this.input.length());
            }
            /**
             * End Key Logic when shift button is pressed
             */
            else if (id == 401 && (keyCode == 35 || keyCode == 40) && this.getShiftDown()) {
                this.setSelectedRange(this.getSelectedRangeLeft(), this.input.length());
                setCursorPosition(this.input.length());
            }
            /**
             * Left Arrow Key Logic when shift button is not pressed
             */
            else if (id == 401 && keyCode == 37 && !this.getShiftDown()) {
                if (this.isSelectedEmpty()) {
                    this.moveCursorLeft();
                } else {
                    this.setCursorPosition(this.getSelectedRangeLeft());
                }
                this.resetSelectedRange();
            }
            /**
             * Left Arrow Key Logic when shift button is pressed
             */
            else if (id == 401 && keyCode == 37 && this.getShiftDown()) {
                if (this.isSelectedEmpty()) {
                    if (this.moveCursorLeft()) {
                        this.setSelectedRange(this.getCursorPosition(), this.getCursorPosition() + 1);
                    }
                } else if (!this.isSelectedEmpty() && this.getCursorPosition() == this.getSelectedRangeLeft()) {
                    if (this.moveCursorLeft()) {
                        this.setSelectedRange(this.getCursorPosition(), this.getSelectedRangeRight());
                    }
                } else if (!this.isSelectedEmpty() && this.getCursorPosition() == this.getSelectedRangeRight()) {
                    if (this.moveCursorLeft()) {
                        this.setSelectedRange(this.getSelectedRangeLeft(), this.getCursorPosition());
                    }
                }
            }
            /**
             * Right Arrow Key Logic when shift button is not pressed
             */
            else if (id == 401 && keyCode == 39 && !this.getShiftDown()) {
                if (this.isSelectedEmpty()) {
                    this.moveCursorRight();
                } else {
                    this.setCursorPosition(this.getSelectedRangeRight());
                }
                this.resetSelectedRange();
            }
            /**
             * Left Arrow Key Logic when shift button is pressed
             */
            else if (id == 401 && keyCode == 39 && this.getShiftDown()) {
                if (this.isSelectedEmpty()) {
                    if (this.moveCursorRight()) {
                        this.setSelectedRange(this.getCursorPosition() - 1, this.getCursorPosition());
                    }
                } else if (!this.isSelectedEmpty() && this.getCursorPosition() == this.getSelectedRangeRight()) {
                    if (this.moveCursorRight()) {
                        this.setSelectedRange(this.getSelectedRangeLeft(), this.getCursorPosition());
                    }
                } else if (!this.isSelectedEmpty() && this.getCursorPosition() == this.getSelectedRangeLeft()) {
                    if (this.moveCursorRight()) {
                        this.setSelectedRange(this.getCursorPosition(), this.getSelectedRangeRight());
                    }
                }
            }
            /**
             * Add a character to the input if none of the special cases above apply
             */
            else if (id == 401 && Character.isDefined(keyChar)) {
                this.addChar(keyChar);
            }
        }
        setScrollBarSlider();
    }

    /**
     * Handle internal horizontal scroll of input field
     * @param delta how much the scrollbar has been dragged left or right
     * @param lowerLimitX lowest x coordinate of the input field
     * @param upperLimitX highest x coordinate of the input field
     */
    protected void handleInternalHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {
        if (((getScrollPosition() + getX() + getInputStringWidth() > upperLimitX) && delta > 0)
                || (getScrollPosition() + getX() < lowerLimitX && delta < 0)) {
            setScrollPosition(getScrollPosition() - delta);
        }
    }

    @Override
    public Component copy() {
        if (this.getInput() != null) {
            return new InputComponent(getX(), getY(), getWidth(), getHeight(), getMetrics(), getInput());
        } else {
            return new InputComponent(getX(), getY(), getWidth(), getMetrics());
        }
    }

    /**
     * Method to highlight selected text with a blue background
     *
     * @param g the graphics element to draw components on
     */
    public void markSelected(Graphics g) {
        g.setColor(new Color(102, 153, 255));
        g.fillRect(g.getFontMetrics().stringWidth(this.input.substring(0, selectedRange.getLeft())) + getX() + getScrollPosition() + offset + 1, getY() + 1,
                g.getFontMetrics().stringWidth(this.input.substring(selectedRange.getLeft(), selectedRange.getRight())),
                getHeightInput() - 1);
        g.setColor(Color.BLACK);
    }

    /**
     * This method is called by the EventKeyHandler when the backspace key is pressed.
     * <p>
     * If (isSelectedEmpty) then nothing is selected and the character in front of the cursor is deleted
     * Otherwise there is some text selected and that selection will get removed
     */
    private void removeChar() {
        if (this.isSelectedEmpty() && cursorPosition != 0) {
            char remove = input.charAt(cursorPosition - 1);
            setInput(this.input.substring(0, cursorPosition - 1) + this.input.substring(cursorPosition));
            this.moveCursorLeft();
            setScrollPositionBy(getMetrics().charWidth(remove));
        } else {
            String remove = input.substring(getSelectedRangeLeft(), getSelectedRangeRight());
            this.setCursorPosition(getSelectedRangeLeft());
            setInput(this.input.substring(0, getSelectedRangeLeft()) + this.input.substring(getSelectedRangeRight()));
            setScrollPositionBy(getMetrics().stringWidth(remove));
            this.resetSelectedRange();
        }
    }

    /**
     * This method is called by the EventKeyHandler when the Delete key is pressed.
     * <p>
     * If (isSelectedEmpty && getInput().length() > getCursorPosition()) then nothing is selected and the character behind the cursor is deleted
     * Otherwise there is some text selected and that selection will get deleted
     */
    private void deleteChar() {
        if (this.isSelectedEmpty() && input.length() > getCursorPosition()) {
            char remove = input.charAt(cursorPosition);
            setInput(this.input.substring(0, cursorPosition) + this.input.substring(cursorPosition + 1));
            setScrollPositionBy(getMetrics().charWidth(remove));
        } else if (!this.isSelectedEmpty()) {
            String remove = input.substring(getSelectedRangeLeft(), getSelectedRangeRight());
            this.setCursorPosition(getSelectedRangeLeft());
            setInput(this.input.substring(0, getSelectedRangeLeft()) + this.input.substring(getSelectedRangeRight()));
            setScrollPositionBy(getMetrics().stringWidth(remove));
            this.resetSelectedRange();
        }
    }

    /**
     * @param keyChar character that needs to be added to the input
     */
    private void addChar(char keyChar) {
        if (!this.isSelectedEmpty()) {
            String remove = input.substring(getSelectedRangeLeft(), getSelectedRangeRight());
            setCursorPosition(getSelectedRangeLeft());
            setInput(this.input.substring(0, getSelectedRangeLeft()) + this.input.substring(getSelectedRangeRight()));
            setScrollPositionBy(getMetrics().stringWidth(remove));
            resetSelectedRange();
        }
        setInput(this.input.substring(0, cursorPosition) + keyChar + this.input.substring(cursorPosition));
        moveCursorRight();
        setScrollPositionBy(-1 * getMetrics().charWidth(keyChar));
    }

    /**
     * Try to move the cursor one position to the left
     *
     * @return true if the cursor can be moved left, false otherwise
     */
    public boolean moveCursorLeft() {
        //moveLeft(getLengthCharLeftOfCursor());
        return this.setCursorPosition(this.getCursorPosition() - 1);
    }

    /**
     * Try to move the cursor one position to the right
     *
     * @return true if the cursor can be moved right, false otherwise
     */
    public boolean moveCursorRight() {
        ///moveRight(getLengthCharRightOfCursor());
        return this.setCursorPosition(this.getCursorPosition() + 1);
    }

    /**
     * Move the cursor to the end of the input
     */
    public void moveCursorToEndOfInput() {
        this.setCursorPosition(this.input.length());
    }

    /**
     * reset the selected range
     */
    public void resetSelectedRange() {
        this.setSelectedRange(0, 0);
    }


    /**
     * @return the position at which the cursor is positioned
     * (if the input equals "www.test.com" and the cursor is placed behind the 'e', then getCursorPosition returns 6)
     */
    public int getCursorPosition() {
        return cursorPosition;
    }

    /**
     * @param cursorPosition the position at which the cursor should be placed
     * @return true if the position is valid and the cursor is moved, false otherwise
     */
    public boolean setCursorPosition(int cursorPosition) {
        if (cursorPosition <= input.length() && cursorPosition >= 0) {
            this.cursorPosition = cursorPosition;
            return true;
        }
        return false;
    }

    /**
     * @return the range of the input that is selected
     */
    public int getSelectedRangeLeft() {
        return this.selectedRange.getLeft();
    }

    /**
     * @return the left bound of the selected range
     */
    public int getSelectedRangeRight() {
        return this.selectedRange.getRight();
    }

    /**
     * @return true if nothing is selected, false otherwise
     */
    public boolean isSelectedEmpty() {
        return (getSelectedRangeLeft() == 0 && getSelectedRangeRight() == 0);
    }

    /**
     * @param x left boundary of the selected range
     * @param y right boundary of the selected range
     */
    public void setSelectedRange(int x, int y) {
        if (x >= 0 && y >= 0 && x <= input.length() && y <= input.length()) {
            this.selectedRange.setLeft(x);
            this.selectedRange.setRight(y);
        }
    }

    /**
     * @return offset of the input field (space between frame and input)
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * @return input that was located in the input field when editMode was entered
     */
    public String getInputAtFocus() {
        return inputAtFocus;
    }

    /**
     * @param inputAtFocus input to save when entering editMode
     */
    public void setInputAtFocus(String inputAtFocus) {
        this.inputAtFocus = inputAtFocus;
    }

    /**
     * @return the metrics of the input field
     */
    public FontMetrics getMetrics() {
        return metrics;
    }

    /**
     * @param metrics the metrics of the input field
     */
    public void setMetrics(FontMetrics metrics) {
        this.metrics = metrics;
    }

    /**
     * @return true if shift is held, false otherwise
     */
    public boolean getShiftDown() {
        return shiftDown;
    }

    /**
     * @param shiftDown boolean that indicates if Shift is held or not
     */
    public void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }

    /**
     * @param bool boolean that indicates if editMode is active or not
     */
    public void setEditMode(boolean bool) {
        this.editMode = bool;
    }

    /**
     * @return true if editMode is active, false otherwise
     */
    public boolean getEditMode() {
        return this.editMode;
    }

    /**
     * @return current input
     */
    public String getInput() {
        return this.input;
    }

    /**
     * Setter for current input
     *
     * @param input
     */
    public void setInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        this.input = input;
    }

    /**
     * Notify all inputListeners if input is updated
     */
    public void inputChanged() {
        for (InputListener inputListener : getInputListeners()) {
            inputListener.inputUpdated(this.input, null);
        }
    }

    /**
     * Notify all editListeners if editMode is changed
     */
    public void editModeChanged() {
        for (EditListener editListener : new ArrayList<>(getEditListeners())) {
            editListener.editModeUpdated(editMode);
        }
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        if (this.scrollBar != null) {
            this.scrollBar.setX(x);
        }
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        if (this.scrollBar != null) {
            this.scrollBar.setY(y + getHeightInput());
        }
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (this.scrollBar != null) {
            this.scrollBar.setWidth(width);
            setScrollBarSlider();
        }
    }

    /**
     * Setter of the scrollbar slider based on how much of the input is shown compared to the width of the field
     */
    public void setScrollBarSlider() {
        int length_input = metrics.stringWidth(this.input);
        if (length_input > getWidth()) {
            double percentage = (double) getWidth() / length_input;
            this.scrollBar.setWidthsliderPercentage(percentage);
            this.scrollBar.setXslider(
                    scrollBar.getX() + (int)Math.round(-1 * getScrollPosition() * percentage));
        } else {
            this.scrollBar.setWidthsliderPercentage(1.0);
            this.scrollBar.setXslider(scrollBar.getX());
        }
    }

    /**
     * Getter for offset that the input inside the field should be painted at when scrolled
     * @return offset that the input inside the field should be painted at when scrolled
     */
    public int getScrollPosition() {
        return scrollPosition;
    }

    /**
     * Setter for offset that the input inside the field should be painted at when scrolled
     * @param scrollPosition offset that the input inside the field should be painted at when scrolled
     */
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    /**
     * @return Width of the input given the current metrics
     */
    public int getInputStringWidth() {
        return getMetrics().stringWidth(getInput());
    }

    /**
     * Setter for the offset of the input inside the field given how much the input should move to the left or right
     * @param delta how the input should move to the left or right
     */
    public void setScrollPositionBy(int delta) {
        if (getInputStringWidth() < getWidth() || getCursorPosition() == 0
                || (delta > 0 && ((getScrollPosition() == 0) || (delta > (-1 * getScrollPosition()))))) {
            setScrollPosition(0);
        } else {
            setScrollPosition(getScrollPosition() + delta);
        }
    }

    @Override
    protected void handleHorizontalScroll(int delta, int lowerLimitX, int upperLimitX) {
        super.handleHorizontalScroll(delta, lowerLimitX, upperLimitX);
        this.setScrollPosition(0);
    }

}
