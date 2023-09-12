package Devscribe.ui;

import javax.swing.*;
import static javax.swing.UIManager.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // Set the system's native look and feel for the user interface.
        setLookAndFeel(getSystemLookAndFeelClassName());

        // Run the GUI initialization on the Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and launch the main editor user interface.
                new EditorUI();
            }
        });
    }
}
