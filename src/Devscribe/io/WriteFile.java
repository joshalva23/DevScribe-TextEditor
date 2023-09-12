package Devscribe.io;

import java.io.*;
import java.awt.*;
import javax.swing.*;

import Devscribe.ui.DialogBox;
import Devscribe.ui.EditorUI;

/**
 * This code block handles the writing of text content to a file in a Java
 * application.
 * 
 * The `WriteFile` class manages file writing operations, allowing text
 * content to be saved to a file.
 * 
 * It provides constructors for creating instances with either an `EditorUI`
 * parent reference or a direct text string for saving.
 * 
 * The `saveFile` method allows saving text content to a specified file.
 * 
 * The `saveFile` method with a string parameter prompts the user to choose a
 * file location for saving the content.
 * 
 * A confirmation dialog is shown if the chosen file already exists, allowing
 * the user to decide whether to replace it.
 * 
 * The `saveFileValidateExtension` method is used to ensure that files have
 * valid extensions when saving.
 * 
 * The `writeFile` method writes the text content to the file using the
 * specified encoding.
 * 
 * Getter and setter methods for the file are provided for retrieving and
 * updating the file being written.
 */

public class WriteFile {
    private EditorUI parent; // Reference to the parent EditorUI.
    private File file; // The file to write.

    // Constructor that takes an EditorUI parent.
    public WriteFile(EditorUI parent) {
        this.parent = parent;
    }

    // Constructor that takes a text string for direct saving.
    public WriteFile(String text) {
        saveFileValidateExtension(null, text);
    }

    // Save the file with the specified text content.
    public boolean saveFile(File file, String text) {
        if (file == null || text == null)
            throw new NullPointerException();

        try {
            this.file = file;

            // Determine the file encoding based on its extension.
            String encoding = "UTF-8";
            writeFile(text, encoding);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Save the file with the specified text content using a file chooser.
    public boolean saveFile(String text) {
        JFileChooser save = new JFileChooser();
        save.setPreferredSize(new Dimension(900, 500));

        final int response = save.showSaveDialog(parent); // Get the path where the file will be saved.
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = save.getSelectedFile();
            if (file.exists()) {
                int r = showMessageReplaceFile(file.getName());
                // NO = 1.
                if (r == 1)
                    return false;
            }

            return saveFile(file, text);
        }

        return false;
    }

    // Show a confirmation dialog when replacing an existing file.
    private int showMessageReplaceFile(String nameFile) {
        String[] buttons = { "Yes", "No" };
        int r = DialogBox.show(parent, nameFile + " already exists. Do you want to replace it?",
                "Confirm Save As", buttons, DialogBox.WARNING);

        return r;
    }

    // Save the file with a valid extension and specified text content.
    public boolean saveFileValidateExtension(File archivo, String text) {
        if (archivo == null) {
            JFileChooser save = new JFileChooser();
            save.setDialogTitle("New File");
            save.setPreferredSize(new Dimension(900, 500));

            final int resp = save.showSaveDialog(parent); // Get the path where the file will be saved.
            if (resp == JFileChooser.APPROVE_OPTION) {
                File file = save.getSelectedFile();
                this.file = new File(file.getAbsolutePath());

                if (this.file.exists()) {
                    int r = showMessageReplaceFile(this.file.getName());
                    // NO = 1.
                    if (r == 1) {
                        this.file = null;
                        return false;
                    }
                }

                try {
                    writeFile(text, "latin1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        } else {
            saveFile(archivo, text);
            return true;
        }

        return false;
    }

    // Write the specified text content to the file with the given encoding.
    public void writeFile(String text, String encoding) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
            writer.write(text);

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            if (writer != null)
                writer.close();
        }
    }

    // Getter for the file.
    public File getFile() {
        return file;
    }

    // Setter for the file.
    public void setFile(File file) {
        this.file = file;
    }
}
