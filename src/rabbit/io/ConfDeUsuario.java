/*
 * Copyright (C) 2017 Félix Pedrozo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package rabbit.io;

import rabbit.ui.EditorUI;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ConfDeUsuario {
    private static Preferences prefs;

    public static final String KEY_TEMA = "editor_tema";
    public static final String KEY_NUM_LINEA = "numero_linea";
    public static final String KEY_BARRA_HERRAMIENTAS = "barra_herramientas";
    public static final String KEY_GUIAS_IDENTACION = "guias_identacion";
    public static final String KEY_FUENTE_TAMANIO = "fuente_tamaño";

    static {
        prefs = Preferences.userNodeForPackage(EditorUI.class);
    }

    private ConfDeUsuario () {}

    public static void putBoolean (String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    public static void putInt (String key, int value) {
        prefs.putInt(key, value);
    }

    public static void putString (String key, String value) {
        prefs.put(key, value);
    }

    public static boolean getBoolean (String key) {
        return prefs.getBoolean(key, true);
    }

    public static int getInt (String key) {
        int defaultValue = key.equals(KEY_FUENTE_TAMANIO) ? 12 : 0;

        return prefs.getInt(key, defaultValue);
    }

    public static String getString (String key) {
        String defaultValue = key.equals(KEY_TEMA) ? "intelli-j" : null;

        return prefs.get(key, defaultValue);
    }

    public static void putRutasDeArchivos (List <String> listArchivosRuta) {
        String ruta = System.getProperty("user.home") + "/.rabbit";
        File file = new File(ruta);

        if (!file.exists()) file.mkdir();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(ruta + "/file.list");

            for (int i = 0; i < listArchivosRuta.size(); i ++) {
                fileWriter.write(listArchivosRuta.get(i) + (listArchivosRuta.size() - 1 == i ? "" : "\n"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getRutasDeArchivos () {
        String ruta = System.getProperty("user.home") + "/.rabbit/file.list";
        List<String> list = new ArrayList<>();
        File file = new File (ruta);

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                while ((ruta = reader.readLine()) != null)
                    list.add(ruta);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
