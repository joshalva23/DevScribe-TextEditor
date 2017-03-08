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

import rabbit.document.tool.DocumentoEstilo;
import rabbit.document.tool.PintarLinea;
import rabbit.document.tool.TabSizeEditorKit;
import rabbit.io.ConfDeUsuario;
import rabbit.io.LeerArchivo;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;

import static rabbit.io.ConfDeUsuario.*;

public class EditorDeTexto extends JPanel {
    private String archivoRuta, nombreArchivo;

    private JTextPane textPane;
    private JPopupMenu menuEmergente;
    private JMenuItem jmiCortar, jmiCopiar, jmiPegar, jmiSelecTodo;

    private JPanel jpEditor;
    private JPanel jpNumLineas;
    private JPanel jpPosicionCursor;

    private EditorUI editorUI;
    private UndoManager undoManager;
    private DocumentoEstilo docEstilo;
    private PintarLinea pintarLinea;

    static int fontSize;
    private int cantFila;

    static {
        //Se obtiene el tamaño de la fuente guardado.
        fontSize = ConfDeUsuario.getInt(KEY_FUENTE_TAMANIO);
    }

    EditorDeTexto (String archivoRuta, String text, EditorUI editorUI) {
        this (new File(archivoRuta), text, editorUI);
    }

    public EditorDeTexto (File file, String text, final EditorUI editorUI) {
        setLayout(new GridBagLayout());

        undoManager = new UndoManager();
        this.editorUI = editorUI;

        archivoRuta = file.getAbsolutePath();
        nombreArchivo = file.getName();

        textPane = new JTextPane();
        docEstilo = new DocumentoEstilo(textPane);
        textPane.setEditorKit(new TabSizeEditorKit());
        textPane.setStyledDocument(docEstilo);
        textPane.setSelectedTextColor(null);
        textPane.setFocusable(true);
        pintarLinea = new PintarLinea(textPane);

        if (text == null) {
            textPane.setText("inicio\n\tcls()\n\t\nfin"); //Texto que tendra por defecto.
            textPane.setCaretPosition(17); //Se configura la posición del cursor.

        } else {
            textPane.setText(text);
            textPane.setCaretPosition(0);
        }

        textPane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                actualizarNumLineas ();
                actualizarPosCursor(textPane.getCaretPosition());
            }
        });

        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3)
                    menuEmergente.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        textPane.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                //Se actualiza el estado de los menus item 'Rehacer y Deshacer'.
                EditorDeTexto.this.editorUI.actualizarMenuItem (undoManager.canUndo(), undoManager.canRedo());
            }
        });

        confMenuEmergente();
        confPanelDeLineas();
        confPanelEditor();
        confPanelPosicionCursor ();
        confEstiloYFormato();
        actualizarPosCursor(textPane.getCaretPosition());

        GridBagConstraints conf = new GridBagConstraints();

        //Configuración del componente en la fila 0 columna 0.
        conf.gridx = conf.gridy = 0;
        conf.weightx = conf.weighty = 1.0;
        conf.fill = GridBagConstraints.BOTH;

        JScrollPane scroll = new JScrollPane(jpEditor);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 0, new Color (0x6D6D6D)));
        add(scroll, conf);

        //Configuración del componente en la fila 1 columna 0.
        conf.gridy = 1;
        conf.weighty = 0.0;
        conf.fill = GridBagConstraints.HORIZONTAL;

        add (jpPosicionCursor, conf);
    }

    private void actualizarNumLineas () {
        int cantFilaActual = cantFila();

        if (cantFila != cantFilaActual) {
            //Compruebo si ha aumentado la cantidad de filas.
            if (cantFila < cantFilaActual) {
                //Agregar nuevos numeros de lineas.
                for (int i = cantFila + 1; i <= cantFilaActual; i ++)
                    jpNumLineas.add(crearEtiquetaNum(i));

            } else {
                //Las cantidad de lineas ha disminuido, remover las etiquetas sobrantes.
                for (int i = cantFila - 1; i >= cantFilaActual; i --) {
                    jpNumLineas.remove(i); //Remover elemento de la posición indicada.
                    jpNumLineas.updateUI();
                }
            }
            //Actualiza nueva cantidad de cantFilaActual.
            cantFila = cantFilaActual;
        }
    }

    private void actualizarPosCursor (int caretPosc) {
        //Extraigo el label que contiene el panel 'jpPosicionCursor'.
        JLabel jlPosCursor = (JLabel) jpPosicionCursor.getComponent(1);

        int fila = obtenerFila(caretPosc);
        int colum = obtenerColum(caretPosc);

        //Actualizo posición del cursor.
        jlPosCursor.setText(fila + " : " + colum);
    }

    private void confPanelEditor () {
        jpEditor = new JPanel(new GridBagLayout());

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0 columna 0.
        conf.gridx = conf.gridy = 0;
        conf.weighty = 1.0;
        conf.fill = GridBagConstraints.VERTICAL;
        conf.insets = new Insets(3, 8, 0, 8);

        jpEditor.add(jpNumLineas, conf);

        //Fila 0 columna 1.
        conf.gridx = 1;
        conf.weightx = 1.0;
        conf.fill = GridBagConstraints.BOTH;
        conf.insets = new Insets(0, 0, 0, 0);

        jpEditor.add(textPane, conf);
    }

    private void confPanelPosicionCursor () {
        jpPosicionCursor = new JPanel();
        jpPosicionCursor.setLayout(new BoxLayout(jpPosicionCursor, BoxLayout.X_AXIS));

        jpPosicionCursor.add(Box.createHorizontalGlue());
        jpPosicionCursor.add(new JLabel());
        jpPosicionCursor.add(Box.createRigidArea(new Dimension(10, 18)));
    }

    private void confPanelDeLineas () {
//        cantFila = obtenerFila(textPane.getText().length());
        cantFila = cantFila();

        jpNumLineas = new JPanel();
        jpNumLineas.setOpaque(false);
        jpNumLineas.setVisible(ConfDeUsuario.getBoolean(KEY_NUM_LINEA));
        jpNumLineas.setLayout(new BoxLayout(jpNumLineas, BoxLayout.Y_AXIS));

        for (int i = 1; i <= cantFila; i ++)
            jpNumLineas.add(crearEtiquetaNum(i));
    }

    private JLabel crearEtiquetaNum (int num) {
        JLabel jlNum = new JLabel("" + num);
        jlNum.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        jlNum.setForeground(new Color (0x808080));

        return jlNum;
    }

    void copiar () {
        textPane.copy();
    }

    void cortar () {
        textPane.cut();
    }

    void pegar () {
        textPane.paste();
    }

    void seleccTodo () {
        textPane.selectAll();
    }

    public void setArchivoRuta (String archivoRuta) {
        this.archivoRuta = archivoRuta;
    }

    public String getArchivoRuta () {
        return archivoRuta;
    }

    public void setText (String text) {
        int caretPos = 0;

        //Compruebo si el archivo va ha ser recargado para guardar la posición del cursor.
        if (text.length() >= textPane.getText().length())
            caretPos = textPane.getCaretPosition();

        textPane.setText(text);
        textPane.setCaretPosition(caretPos);
    }

    public String getText () {
        return textPane.getText();
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void rehacer () {
        undoManager.redo();
        editorUI.actualizarMenuItem (undoManager.canUndo(), undoManager.canRedo());
    }

    public void deshacer () {
        undoManager.undo();
        editorUI.actualizarMenuItem (undoManager.canUndo(), undoManager.canRedo());
    }

    //Se configura el color de fondo y el formato de los componentes de acuerdo al tema elegido.
    private void confEstiloYFormato () {
        switch (EditorColor.tema) {
            case EditorColor.DARCULA :
                textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
                textPane.setBackground(new Color (0x2B2B2B));
                textPane.setCaretColor(new Color (0xA9B7C6));
                textPane.setSelectionColor(new Color(0x214283));

                pintarLinea.setColor(new Color(0x3A3A3A));

                jpEditor.setBackground(new Color(0x343434));
                break;

            case EditorColor.INTELLIJ :
                textPane.setFont(new Font (Font.MONOSPACED, Font.BOLD, fontSize));
                textPane.setBackground(new Color(0xF0F0F0));
                textPane.setCaretColor(Color.BLACK);
                textPane.setSelectionColor(new Color(0xB0C5E3));

                pintarLinea.setColor(new Color(0xFFFFDC));

                jpEditor.setBackground(new Color(0xE7E3E3));
                break;
        }
    }

    private void confMenuEmergente () {
        menuEmergente = new JPopupMenu();

        //Creación de los menu item.
        jmiCopiar = new JMenuItem("Copiar");
        jmiCortar = new JMenuItem("Cortar");
        jmiPegar = new JMenuItem("Pegar");
        jmiSelecTodo = new JMenuItem("Seleccionar todo");

        jmiCopiar.addActionListener(eventosMenu);
        jmiCortar.addActionListener(eventosMenu);
        jmiPegar.addActionListener(eventosMenu);
        jmiSelecTodo.addActionListener(eventosMenu);

        menuEmergente.add(jmiCopiar);
        menuEmergente.add(jmiCortar);
        menuEmergente.add(jmiPegar);
        menuEmergente.addSeparator();
        menuEmergente.add(jmiSelecTodo);
    }

    public String toString () {
        return nombreArchivo;
    }

    public boolean equals (Object o) {
        if (o instanceof EditorDeTexto) {
            if (archivoRuta.equals(((EditorDeTexto)o).getArchivoRuta()))
                return true;
        }

        return false;
    }

    public int hashCode () {
        return archivoRuta.hashCode();
    }

    public boolean archivoModificado () {
        String textGuardado = LeerArchivo.leer(archivoRuta);

        return !textGuardado.equals(textPane.getText());
    }

    public void actualizarTema (int tema) {
        docEstilo.actualizarColorDocumento(new EditorColor(tema));
        //Guardo la posición antes de recargar el texto.
        int posCaret = textPane.getCaretPosition();
        textPane.setText(textPane.getText());
        textPane.setCaretPosition(posCaret);

        confEstiloYFormato();
    }

    public void actualizarFuente () {
        Font font;

        //Cambia tamaño del panel de texto.
        if (EditorColor.tema == EditorColor.INTELLIJ)
            font = new Font(Font.MONOSPACED, Font.BOLD, fontSize);
        else
            font = new Font(Font.MONOSPACED, Font.PLAIN, fontSize);

        textPane.setFont(font);
        //Cambia tamaño de las etiquetas de los numeros.
        for (Component component : jpNumLineas.getComponents())
            component.setFont(font);
    }

    public void habilitarNumLineas (boolean state) {
        jpNumLineas.setVisible(state);
    }

    private int obtenerFila (int posc) {
        int numFila = 0;

        try {
            for (int offset = posc; offset >= 0;) {
                offset = Utilities.getRowStart(textPane, offset) - 1;
                numFila ++;
            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return numFila;
    }

    private int obtenerColum (int posc) {
        int colNum = 0;

        try {
            colNum = posc - Utilities.getRowStart(textPane, posc) + 1;

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return colNum;
    }

    private int cantFila () {
        int cantFila = 0;
        String text = textPane.getText();

        for (int i = text.length() - 1; i >= 0; i --) {
            if (text.charAt(i) == '\n') {
                cantFila ++;
            }
        }

        return cantFila + 1;
    }

    //Clase anónima para el manejo de los eventos de los menu item.
    private ActionListener eventosMenu = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jmiCortar)
                cortar();

            else if (e.getSource() == jmiCopiar)
                copiar();

            else if (e.getSource() == jmiPegar)
                pegar();

            else if (e.getSource() == jmiSelecTodo)
                seleccTodo();
        }
    };
}
