package app;
import vista.main.MainFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // runs in AWT thread
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
