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

package rabbit.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;

public class EditorDeTexto extends JPanel {
    private String archivoRuta;
    private String nombreArchivo;
    private JTextPane textPane;

    EditorDeTexto (String archivoRuta, String text) {
        this (new File(archivoRuta), text);
    }

    EditorDeTexto (String text) {
        this ((File)null, text);
    }

    public EditorDeTexto (File file, String text) {
        setLayout(new GridLayout(1,1));

        this.archivoRuta = file == null ? null : file.getAbsolutePath();
        nombreArchivo = file == null ? null : file.getName();

        textPane = new JTextPane(new StyleDocument());

        if (text == null) {
            textPane.setText("inicio\n cls()\n \nfin"); //Texto que tendra por defecto.
            textPane.setCaretPosition(15); //Se configura la posición del cursor.

        } else {
            textPane.setText(text);
            textPane.setCaretPosition(text.length());
        }

        confEditorDeTexto();

        final JPopupMenu menuEmergente = popupMenuInsert();
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3)
                    menuEmergente.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        add (new JScrollPane(textPane));
    }

    EditorDeTexto () {
        this ((File)null, null);
    }

    public void setArchivoRuta (String archivoRuta) {
        this.archivoRuta = archivoRuta;
    }

    public String getArchivoRuta () {
        return archivoRuta;
    }

    public void setText (String text) {
        textPane.setText(text);
    }

    public String getText () {
        return textPane.getText();
    }

    public boolean existeArchivo () {
        return archivoRuta != null;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    private void confEditorDeTexto () {
        //Se configura el fondo y el formato de acuerdo al tema elegido.
        switch (EditorColor.tema) {
            case EditorColor.DARCULA :
                textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                textPane.setBackground(new Color (0x2B2B2B));
                textPane.setCaretColor(new Color (0xA9B7C6));
                break;

            case EditorColor.INTELLIJ :
                textPane.setFont(new Font (Font.MONOSPACED, Font.BOLD, 12));
                textPane.setBackground(new Color(0xF0F0F0));
                break;
        }
    }

    private JPopupMenu popupMenuInsert () {
        JPopupMenu popupMenu = new JPopupMenu();

        //Creación de los menu item.
        JMenuItem jmCopiar = new JMenuItem("Copiar");
        JMenuItem jmCortar = new JMenuItem("Cortar");
        JMenuItem jmPegar = new JMenuItem("Pegar");
        JMenuItem jmSelecTodo = new JMenuItem("Seleccionar todo");

        popupMenu.add(jmCopiar);
        popupMenu.add(jmCortar);
        popupMenu.add(jmPegar);
        popupMenu.addSeparator();
        popupMenu.add(jmSelecTodo);

        return popupMenu;
    }
}
