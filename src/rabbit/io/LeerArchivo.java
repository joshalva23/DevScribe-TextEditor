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

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class LeerArchivo {
    private String nombreArchivo, archivoRuta, text;

    public LeerArchivo () {
        JFileChooser open = new JFileChooser();
        open.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                String path = f.getPath();
                return f.isDirectory() || path.endsWith(".sl");
            }

            @Override
            public String getDescription() {
                return "Archivo SL [.sl]";
            }
        });

        final int resp = open.showOpenDialog(null);
        if (resp == JFileChooser.APPROVE_OPTION) {
            File file = open.getSelectedFile();
            nombreArchivo = file.getName();
            archivoRuta = file.getAbsolutePath();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                String line;
                StringBuilder textArchivo = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    textArchivo.append(line);
                    textArchivo.append("\n");
                }

                text = textArchivo.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
