package ui;

import domain.DocumentController;
import ui.window.BrowsrView;

public class Browsr {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            DocumentController controller = new DocumentController();
            new BrowsrView(controller).show();
        });
    }
}
