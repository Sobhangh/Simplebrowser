package ui.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.listeners.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {
    // Testing component methods with an TextComponent
    private TextComponent textComponent = new TextComponent(0, 0, 10, 10, "Text test");
    private LinkClickedListener lcl = href -> System.out.println("Test: Add ClickLinkListener");
    private ClickListener cl = () -> System.out.println("Test: Add ClickListener");
    private EditListener el = editmode -> System.out.println("Test: Add EditListener");
    private InputListener il = (input, name) -> System.out.println("Test: Add InputListener");
    private AddBookmarkListener bl = (name, url) -> System.out.println("Test: Add BookMarkListener");

    @BeforeEach
    public void init(){
        this.textComponent.addLinkClickedListener(lcl);
        this.textComponent.addClickListener(cl);
        this.textComponent.addEditListener(el);
        this.textComponent.addInputListener(il);
        this.textComponent.addBookmarkListener(bl);
    }


    @Test
    void getX() {
        assertEquals(0, this.textComponent.getX());
    }

    @Test
    void setX() {
        textComponent.setX(2);
        assertEquals(2, textComponent.getX());
    }


    @Test
    void getY() {
        assertEquals(0, this.textComponent.getY());
    }

    @Test
    void setY() {
        textComponent.setY(2);
        assertEquals(2, textComponent.getY());
    }

    @Test
    void getWidth() {
        assertEquals(10, this.textComponent.getWidth());
    }

    @Test
    void setWidth() {
        textComponent.setWidth(20);
        assertEquals(20, textComponent.getWidth());
    }

    @Test
    void setWidthNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            textComponent.setWidth(-20);
        });
    }

    @Test
    void getHeight() {
        assertEquals(10, this.textComponent.getHeight());
    }

    @Test
    void setHeight() {
        textComponent.setHeight(12);
        assertEquals(12, textComponent.getHeight());
    }

    @Test
    void setHeightNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            textComponent.setHeight(-12);
        });
    }


    @Test
    void isPositionInComponent() {
        assertTrue(this.textComponent.isPositionInComponent(5, 5));
    }

    @Test
    void isPositionInComponentLowerBound() {
        assertTrue(this.textComponent.isPositionInComponent(0, 0));
    }

    @Test
    void isPositionInComponentUpperBound() {
        assertTrue(this.textComponent.isPositionInComponent(10, 10));
    }


    @Test
    void isPositionInComponentOutSideBound() {
        assertFalse(this.textComponent.isPositionInComponent(11, 11));
    }

    @Test
    void addClickListener() {
        ClickListener cl1 = () -> System.out.println("Test: Add ClickListener");
        this.textComponent.addClickListener(cl1);
        assertEquals(List.of(cl, cl1), this.textComponent.getClickListeners());
    }

    @Test
    void addClickListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.addClickListener(null);
        });
    }

    @Test
    void addBookMarkListener() {
        AddBookmarkListener bl1 = (name, url) -> System.out.println("Test: Add BookMarkListener");
        this.textComponent.addBookmarkListener(bl1);
        assertEquals(List.of(bl, bl1), this.textComponent.getAddBookmarkListeners());
    }

    @Test
    void addAddBookMarkListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.addBookmarkListener(null);
        });
    }

    @Test
    void addLinkClickedListener() {
        LinkClickedListener lcl1 = (href) -> System.out.println("Test: Add LinkClickedListener");
        this.textComponent.addLinkClickedListener(lcl1);
        assertEquals(List.of(lcl, lcl1), this.textComponent.getLinkClickedListeners());
    }

    @Test
    void addLinkClickedListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.addLinkClickedListener(null);
        });
    }

    @Test
    void addEditListener() {
        EditListener el1 = (editmode) -> System.out.println("Test: Add EditListener");
        this.textComponent.addEditListener(el1);
        assertEquals(List.of(el, el1), this.textComponent.getEditListeners());
    }

    @Test
    void addEditListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.addEditListener(null);
        });
    }

    @Test
    void addInputListener() {
        InputListener listener = (input, name) -> System.out.println("Test: Add InputListener");
        this.textComponent.addInputListener(listener);
        assertEquals(List.of(il, listener), this.textComponent.getInputListeners());
    }

    @Test
    void addInputListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.addInputListener(null);
        });
    }

    @Test
    void removeClickListener() {
        this.textComponent.removeClickListener(cl);
        assertEquals(List.of(), this.textComponent.getClickListeners());
    }

    @Test
    void removeClickListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeClickListener(null);
        });
    }

    @Test
    void removeClickListenerNotRegistered() {
        ClickListener cl = () -> System.out.println("Test: Remove Unregistered ClickListener");
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeClickListener(cl);
        });
    }


    @Test
    void removeLinkClickedListener() {
        this.textComponent.removeLinkClickedListener(lcl);
        assertEquals(List.of(), this.textComponent.getLinkClickedListeners());
    }

    @Test
    void removeLinkClickedListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeLinkClickedListener(null);
        });
    }

    @Test
    void removeLinkClickedListenerNotRegistered() {
        LinkClickedListener lcl1 = (href) -> System.out.println("Test: Remove Unregistered LinkClickedListener");
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeLinkClickedListener(lcl1);
        });
    }

    @Test
    void removeInputListener() {
        this.textComponent.removeInputListener(il);
        assertEquals(List.of(), this.textComponent.getInputListeners());
    }

    @Test
    void removeInputListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeInputListener(null);
        });
    }

    @Test
    void removeInputListenerNotRegistered() {
        InputListener listener = (input, name) -> System.out.println("Test: Remove Unregistered InputListener");
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeInputListener(listener);
        });
    }

    @Test
    void removeEditListener() {
        this.textComponent.removeEditListener(el);
        assertEquals(List.of(), this.textComponent.getEditListeners());
    }

    @Test
    void removeEditListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeEditListener(null);
        });
    }

    @Test
    void removeEditListenerNotRegistered() {
        EditListener listener = (editmode) -> System.out.println("Test: Remove Unregistered EditListener");
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeEditListener(listener);
        });
    }

    @Test
    void removeBookMarkListener() {
        this.textComponent.removeAddBookmarkListener(bl);
        assertEquals(List.of(), this.textComponent.getAddBookmarkListeners());
    }

    @Test
    void removeBookMarkListenerNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeAddBookmarkListener(null);
        });
    }

    @Test
    void removeBookMarkListenerNotRegistered() {
        AddBookmarkListener listener = (name, url) -> System.out.println("Test: Remove Unregistered BookMarkListener");
        assertThrows(IllegalArgumentException.class, () -> {
            this.textComponent.removeAddBookmarkListener(listener);
        });
    }

    @Test
    void VerticalScrollEventTest() {
        textComponent.setY(10);
        int y_before = textComponent.getY();
        textComponent.handleVerticalScroll(-1, y_before + 5, y_before + 10);
        assertEquals(y_before + 5, textComponent.getLowerLimitY());
        assertEquals(y_before + 10, textComponent.getUpperLimitY());
        int y_after = textComponent.getY();
        assertEquals(y_before + 1, y_after);
    }

    @Test
    void HorizontalScrollEventTest() {
        textComponent.setX(10);
        int x_before = textComponent.getX();
        textComponent.handleHorizontalScroll(1, x_before, x_before + 5);
        assertEquals(x_before, textComponent.getLowerLimitX());
        assertEquals(x_before + 5, textComponent.getUpperLimitX());
        int x_after = textComponent.getX();
        assertEquals(x_before - 1, x_after);
    }

    @Test
    void getClickListeners() {
        assertEquals(List.of(cl), this.textComponent.getClickListeners());
    }

    @Test
    void getInputListeners() {
        assertEquals(List.of(il), this.textComponent.getInputListeners());
    }

    @Test
    void getLinkClickedListeners() {
        assertEquals(List.of(lcl), this.textComponent.getLinkClickedListeners());
    }

    @Test
    void getEditListeners() {
        assertEquals(List.of(el), this.textComponent.getEditListeners());
    }

    @Test
    void getBookMarkListener() {
        assertEquals(List.of(bl), this.textComponent.getAddBookmarkListeners());
    }
}