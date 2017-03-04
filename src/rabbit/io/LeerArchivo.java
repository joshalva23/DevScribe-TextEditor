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

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class LeerArchivo {
    private String nombreArchivo, archivoRuta, text;

    public LeerArchivo (EditorUI parent) {
        JFileChooser open = new JFileChooser();
        open.setPreferredSize(new Dimension(900, 500));
        open.setFileFilter(new FiltroDeArchivo());

        final int resp = open.showOpenDialog(parent);
        if (resp == JFileChooser.APPROVE_OPTION) {
            File file = open.getSelectedFile();
            nombreArchivo = file.getName();
            archivoRuta = file.getAbsolutePath();

            text = leer (archivoRuta);
        }
    }

    public static String leer (String ruta) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader reader = null;

            try {
                String encoding = ruta.endsWith(".sl") ? "latin1" : "UTF-8";
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), encoding));

                int $char;
                while (($char = reader.read()) > - 1)
                    text.append((char) $char);

            } finally {
                if (reader != null) reader.close ();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getArchivoRuta() {
        return archivoRuta;
    }

    public void setArchivoRuta(String archivoRuta) {
        this.archivoRuta = archivoRuta;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean archivoLeido () {
        return archivoRuta != null;
    }
}
