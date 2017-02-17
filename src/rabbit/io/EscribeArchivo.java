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

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscribeArchivo {
    private String nombreArchivo, arhivoRuta;

    public EscribeArchivo () {}

    public EscribeArchivo (String text) {
        guardarArchivoValidarExtension(null, text);
    }

    public EscribeArchivo (String archivoRuta, String text) {
        guardarArchivoValidarExtension(archivoRuta, text);
    }

    public boolean guardarArchivo (String archivoRuta, String text) {
        if (archivoRuta == null || text == null)
            throw new NullPointerException();

        try {
            //Guardar info del archivo.
            setArhivoRuta(archivoRuta);
            setNombreArchivo(new File(archivoRuta).getName());

            escribirArchivo(text, false);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean guardarArchivo (String text) {
        JFileChooser save = new JFileChooser();
        save.setFileFilter(new FileFilter() {
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

        final int resp = save.showSaveDialog(null); //Obtener ruta en donde se guardara el archivo.
        if (resp == JFileChooser.APPROVE_OPTION) {
            File file = save.getSelectedFile();
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) { //TODO : Que hace este metodo.?
                        //Guardar info del archivo.
                        setNombreArchivo(file.getName()); //Se guarda el nombre del archivo.
                        setArhivoRuta(file.getAbsolutePath()); //Se guarda la ruta del arhivo.

                        escribirArchivo(text, true);
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo. El archivo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return false;
    }

    public boolean guardarArchivoValidarExtension (String archivoRuta, String text) {
        if (archivoRuta == null) {
            JFileChooser save = new JFileChooser();
            save.setFileFilter(new FileFilter() {
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

            final int resp = save.showSaveDialog(null); //Obtener ruta en donde se guardara el archivo.
            if (resp == JFileChooser.APPROVE_OPTION) {
                File file = save.getSelectedFile();
                if (!file.exists()) {
                    try {
                        if (file.createNewFile()) { //TODO : Que hace este metodo.?
                            //Añadir extensión si no posee.
                            if (file.getAbsolutePath().endsWith(".sl")) {
                                setNombreArchivo(file.getName()); //Se guarda el nombre del archivo.
                                setArhivoRuta(file.getAbsolutePath()); //Se guarda la ruta del arhivo.
                            } else {
                                setNombreArchivo(file.getName() + ".sl");
                                setArhivoRuta(file.getAbsolutePath() + ".sl");
                            }
                            escribirArchivo(text, true);
                            return true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar el archivo. El archivo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            guardarArchivo(archivoRuta, text);
        }

        return false;
    }

    public void escribirArchivo (String text, boolean append) throws IOException {
        BufferedWriter writer = null;

        try {
            writer =  new BufferedWriter(new FileWriter(getArhivoRuta(), append));
            writer.write(text);

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            if (writer != null) writer.close();
        }
    }

    public boolean arhivoGuardado () {
        return nombreArchivo != null;
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
