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
}
