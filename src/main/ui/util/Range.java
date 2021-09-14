package ui.util;

/**
 * Class to represent the range of text that is supposed to be selected
 */
public class Range {
    private int left;
    private int right;

    /**
     * Constructor for the Range Class
     *
     * @param left left bound of the range
     * @param right right bound of the range
     */
    public Range(int left, int right) {
        this.left = left;
        this.right = right;
    }

    /**
     * @return the left boundary of the range
     */
    public int getLeft() {
        return left;
    }
    /**
     * set the left boundary of the range
     */
    public void setLeft(int left) {
        this.left = left;
    }
    /**
     * @return the right boundary of the range
     */
    public int getRight() {
        return right;
    }
    /**
     * set the right boundary of the range
     */
    public void setRight(int right) {
        this.right = right;
    }
}