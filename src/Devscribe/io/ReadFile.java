package Devscribe.io;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import Devscribe.ui.EditorUI;

/**
 * This code defines a `ReadFile` class that reads the content of a selected
 * file and stores it as text.
 * 
 * The `ReadFile` class has instance variables for the selected file (`file`)
 * and its content (`text`).
 * 
 * Its constructor (`ReadFile(EditorUI parent)`) displays a file chooser dialog
 * and, if a file is chosen, reads its content and stores it as text.
 * 
 * The `read` method, a static function, reads a file's content character by
 * character. It returns the text as a string.
 * 
 * Getter and setter methods are provided for `file` and `text`.
 * 
 * The `fileRead` method checks if a file has been successfully read.
 * 
 */

public class ReadFile {
    private File file; // Represents the selected file.
    private String text; // Stores the content of the selected file as text.

    // Constructor for the ReadFile class.
    // It takes an EditorUI parent as a parameter for positioning the file chooser
    // dialog.
    public ReadFile(EditorUI parent) {
        JFileChooser open = new JFileChooser();
        open.setPreferredSize(new Dimension(900, 500)); // Set the dimensions of the file chooser dialog.

        final int response = open.showOpenDialog(parent); // Display the file chooser dialog.
        if (response == JFileChooser.APPROVE_OPTION) { // If a file is chosen.
            file = open.getSelectedFile(); // Get the selected file.
            text = read(file); // Read the content of the selected file and store it as text.
        }
    }

    // Static method to read the content of a file and return it as a string.
    public static String read(File file) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader reader = null;

            try {
                // Determine the file encoding based on its extension.
                String encoding = "UTF-8";
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));

                int character;
                // Read characters from the file and append them to the text StringBuilder.
                while ((character = reader.read()) > -1)
                    text.append((char) character);

            } finally {
                if (reader != null)
                    reader.close(); // Close the file reader.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any IOException by printing the stack trace.
        }

        return text.toString(); // Return the text content of the file as a string.
    }

    // Getter method for the file.
    public File getFile() {
        return file;
    }

    // Setter method for the file.
    public void setFile(File file) {
        this.file = file;
    }

    // Getter method for the text content.
    public String getText() {
        return text;
    }

    // Setter method for the text content.
    public void setText(String text) {
        this.text = text;
    }

    // Method to check if a file has been successfully read.
    public boolean fileRead() {
        return file != null;
    }
}
