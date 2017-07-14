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
import rabbit.ui.CuadroDeDialogo;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class EscribeArchivo {
    private EditorUI parent;
    private File file;

    public EscribeArchivo (EditorUI parent) {
        this.parent = parent;
    }

    public EscribeArchivo (String text) {
        guardarArchivoValidarExtension(null, text);
    }

    public boolean guardarArchivo (File file, String text) {
        if (file == null || text == null)
            throw new NullPointerException();

        try {
            this.file = file;

            String encoding = file.getName().endsWith(".sl") ? "latin1" : "UTF-8";
            escribirArchivo(text, encoding);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean guardarArchivo (String text) {
        JFileChooser save = new JFileChooser();
        save.setPreferredSize(new Dimension(900, 500));
        save.setFileFilter(new FiltroDeArchivo());

        final int resp = save.showSaveDialog(parent); //Obtener ruta en donde se guardara el archivo.
        if (resp == JFileChooser.APPROVE_OPTION) {
            File file = save.getSelectedFile();
            if (!file.exists()) {
                guardarArchivo(file, text);

            } else {
                mostrarMensDeError();
            }
        }

        return false;
    }

    public boolean guardarArchivoValidarExtension (File archivo, String text) {
        if (archivo == null) {
            JFileChooser save = new JFileChooser();
            save.setDialogTitle("Nuevo archivo SL");
            save.setPreferredSize(new Dimension(900, 500));
            save.setFileFilter(new FiltroDeArchivo());

            final int resp = save.showSaveDialog(parent); //Obtener ruta en donde se guardara el archivo.
            if (resp == JFileChooser.APPROVE_OPTION) {
                File file = save.getSelectedFile();
                if (!file.exists()) {
                    //Añadir extensión si no posee.
                    if (file.getName().endsWith(".sl")) {
                        this.file = file;

                    } else {
                        this.file = new File (file.getAbsolutePath() + ".sl");
                    }

                    try {
                        escribirArchivo(text, "latin1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    mostrarMensDeError();
                }
            }
        } else {
            guardarArchivo(archivo, text);
            return true;
        }

        return false;
    }

    public void escribirArchivo (String text, String encoding) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
            writer.write(text);

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            if (writer != null) writer.close();
        }
    }

    private void mostrarMensDeError () {
        String [] opcion = {"Aceptar"};
        CuadroDeDialogo.mostrar(parent, "Error al crear el archivo. El archivo ya existe.", "Error", opcion, CuadroDeDialogo.ERROR);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
