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
    private String nombreArchivo, arhivoRuta;

    private EditorUI parent;

    public EscribeArchivo (EditorUI parent) {
        this.parent = parent;
    }

    public EscribeArchivo (String text) {
        guardarArchivoValidarExtension(null, text);
    }

    public boolean guardarArchivo (String archivoRuta, String text) {
        if (archivoRuta == null || text == null)
            throw new NullPointerException();

        try {
            //Guardar info del archivo.
            setArhivoRuta(archivoRuta);
            setNombreArchivo(new File(archivoRuta).getName());

            String encoding = archivoRuta.endsWith(".sl") ? "latin1" : "UTF-8";
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
                guardarArchivo(file.getAbsolutePath(), text);

            } else {
                JOptionPane.showMessageDialog(parent, "Error al guardar el archivo. El archivo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return false;
    }

    public boolean guardarArchivoValidarExtension (String archivoRuta, String text) {
        if (archivoRuta == null) {
            JFileChooser save = new JFileChooser();
            save.setDialogTitle("Nuevo archivo SL");
            save.setPreferredSize(new Dimension(900, 500));
            save.setFileFilter(new FiltroDeArchivo());

            final int resp = save.showSaveDialog(parent); //Obtener ruta en donde se guardara el archivo.
            if (resp == JFileChooser.APPROVE_OPTION) {
                File file = save.getSelectedFile();
                if (!file.exists()) {
                    try {
                        if (file.createNewFile()) {
                            //Añadir extensión si no posee.
                            if (file.getAbsolutePath().endsWith(".sl")) {
                                setNombreArchivo(file.getName()); //Se guarda el nombre del archivo.
                                setArhivoRuta(file.getAbsolutePath()); //Se guarda la ruta del arhivo.
                            } else {
                                setNombreArchivo(file.getName() + ".sl");
                                setArhivoRuta(file.getAbsolutePath() + ".sl");
                            }

                            escribirArchivo(text, "latin1");
                            return true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String [] opcion = {"Aceptar"};
                    CuadroDeDialogo.mostrar(parent, "Error al guardar el archivo. El archivo ya existe.", "Error", opcion);
                }
            }
        } else {
            guardarArchivo(archivoRuta, text);
            return true;
        }

        return false;
    }

    public void escribirArchivo (String text, String enconding) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getArhivoRuta()), enconding));
            writer.write(text);

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            if (writer != null) writer.close();
        }
    }

    public void setNombreArchivo (String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreArchivo () {
        return nombreArchivo;
    }

    public String getArhivoRuta() {
        return arhivoRuta;
    }

    public void setArhivoRuta(String arhivoRuta) {
        this.arhivoRuta = arhivoRuta;
    }
}
