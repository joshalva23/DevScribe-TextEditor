package Devscribe.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import Devscribe.ui.EditorUI;

/**
 * This code manages user configuration settings for an application:
 * 
 * The `UserConfig` class handles user preferences and configuration settings.
 * 
 * It uses the Java Preferences API to store and retrieve these settings.
 * 
 * Configuration keys (e.g., `KEY_THEME`, `KEY_LINE_NUMBERS`) are defined for
 * various settings.
 * 
 * The static initializer block sets up the `Preferences` instance.
 * 
 * Methods like `putBoolean`, `putInt`, and `putString` store preferences in
 * user preferences.
 * 
 * Methods like `getBoolean`, `getInt`, and `getString` retrieve preferences
 * with default values if not found.
 * 
 * It provides methods to store and retrieve a list of file paths to/from a
 * file.
 */

public class UserConfig {
    private static Preferences prefs;

    // Keys for various user configuration settings
    public static final String KEY_THEME = "editor_theme";
    public static final String KEY_LINE_NUMBERS = "line_numbers";
    public static final String KEY_TOOLBAR = "toolbar";
    public static final String KEY_INDENT_GUIDES = "indentation_guides";
    public static final String KEY_WORD_WRAP = "word_wrap";
    public static final String KEY_FONT_SIZE = "font_size";

    // Static initializer block to set up the Preferences instance
    static {
        prefs = Preferences.userNodeForPackage(EditorUI.class);
    }

    // Private constructor to prevent instantiation
    private UserConfig() {
    }

    // Methods for storing boolean, integer, and string preferences

    /**
     * Store a boolean preference in user preferences.
     *
     * @param key   The key for the preference.
     * @param value The value to store.
     */
    public static void putBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    /**
     * Store an integer preference in user preferences.
     *
     * @param key   The key for the preference.
     * @param value The value to store.
     */
    public static void putInt(String key, int value) {
        prefs.putInt(key, value);
    }

    /**
     * Store a string preference in user preferences.
     *
     * @param key   The key for the preference.
     * @param value The value to store.
     */
    public static void putString(String key, String value) {
        prefs.put(key, value);
    }

    // Methods for retrieving boolean, integer, and string preferences with default
    // values

    /**
     * Retrieve a boolean preference from user preferences with a default value.
     *
     * @param key The key for the preference.
     * @return The retrieved boolean preference or the default value if not found.
     */
    public static boolean getBoolean(String key) {
        return prefs.getBoolean(key, true); // Default value is true
    }

    /**
     * Retrieve an integer preference from user preferences with a default value.
     *
     * @param key The key for the preference.
     * @return The retrieved integer preference or the default value if not found.
     */
    public static int getInt(String key) {
        // Default value is 12 for font size and 0 for others
        int defaultValue = key.equals(KEY_FONT_SIZE) ? 12 : 0;
        return prefs.getInt(key, defaultValue);
    }

    /**
     * Retrieve a string preference from user preferences with a default value.
     *
     * @param key The key for the preference.
     * @return The retrieved string preference or the default value if not found.
     */
    public static String getString(String key) {
        String defaultValue = key.equals(KEY_THEME) ? "monokai" : null;
        return prefs.get(key, defaultValue);
    }

    // Method for storing a list of file paths to a file

    /**
     * Store a list of file paths to a file.
     *
     * @param filePathsList The list of file paths to store.
     */
    public static void putFilePaths(List<String> filePathsList) {
        String path = System.getProperty("user.home") + "/.Devscribe";
        File file = new File(path);

        if (!file.exists())
            file.mkdir();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path + "/file.list");

            for (int i = 0; i < filePathsList.size(); i++) {
                fileWriter.write(filePathsList.get(i) + (filePathsList.size() - 1 == i ? "" : "\n"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method for retrieving a list of file paths from a file

    /**
     * Retrieve a list of file paths from a file.
     *
     * @return The list of file paths or an empty list if the file doesn't exist.
     */
    public static List<String> getFilePaths() {
        String path = System.getProperty("user.home") + "/.Devscribe/file.list";
        List<String> list = new ArrayList<>();
        File file = new File(path);

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String filePath;
                while ((filePath = reader.readLine()) != null)
                    list.add(filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
