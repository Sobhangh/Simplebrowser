package ui.listeners;

public interface AddBookmarkListener {
    /**
     *  Method the registered listeners implement to handle when a buttons's pressed and a bookmark should be added
     *
     *  @param name the name of the bookmark
     *  @param url the link of the bookmark
     */
    void addBookmark(String name, String url);
}