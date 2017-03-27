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

import rabbit.io.ConfDeUsuario;
import rabbit.io.EscribeArchivo;
import rabbit.io.LeerArchivo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import static rabbit.io.ConfDeUsuario.*;

public class EditorUI extends JFrame {
    private JTabbedPane jTabbedPane;

    private JMenuItem jmiNuevoArchivo, jmiAbrirArchivo, jmiGuardar, jmiGuardarComo, jmiGuardarTodo, jmiCerrarPestania,
                    jmiSalir, jmiDeshacer, jmiRehacer, jmiCopiar, jmiCortar, jmiPegar, jmiSelecTodo, jmiAumentarFuente,
                    jmiDisminuirFuente, jmiTamanioOriginal, jmiAyuda, jmiAcercaDeRabbit;

    private JCheckBoxMenuItem jcbGuiasDeIndentacion, jcbNumDeLineas, jcbToolBar, jcbIntelliJ, jcbDarcula;

    private JButton jbNuevoArchivo, jbGuardarTodo, jbRehacer, jbDeshacer, jbCopiar, jbPegar, jbCortar,
            jbAyuda, jbAbrirArchivoNuevo;

    private EventosMenuItem e;

    private JToolBar jtb;

    public EditorUI () {
        super ("Rabbit");
        //Estado de la ventana maximizado.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Se configura el tamaño de la ventana al tamaño de la pantalla.
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("icono/rabbit.png")).getImage());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarPrograma();
            }
        });

        e = new EventosMenuItem();

        jTabbedPane = new JTabbedPane();
        jTabbedPane.setFocusable(false);
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        add (jTabbedPane, BorderLayout.CENTER);

        jtb = new JToolBar();
        jtb.setFloatable(false);
        jtb.setFocusable(false);
        jtb.setBorder(BorderFactory.createEtchedBorder());
        jtb.setVisible(ConfDeUsuario.getBoolean(KEY_BARRA_HERRAMIENTAS));
        confBarraDeHerramientas ();

        add (jtb, BorderLayout.NORTH);

        setJMenuBar(confBarraDeMenu());

        setVisible(true);
    }

    private void confBarraDeHerramientas () {
        EventosBarraHerr e = new EventosBarraHerr();

        jbNuevoArchivo = new JButton(new ImageIcon(getClass().getResource("icono/archivo.png")));
        jbNuevoArchivo.setToolTipText("Nuevo archivo (Ctrl+N)");
        jbNuevoArchivo.setFocusable(false);
        jbNuevoArchivo.addActionListener(e);

        jbAbrirArchivoNuevo = new JButton(new ImageIcon(getClass().getResource("icono/abrir.png")));
        jbAbrirArchivoNuevo.setToolTipText("Abrir (Ctrl+O)");
        jbAbrirArchivoNuevo.setFocusable(false);
        jbAbrirArchivoNuevo.addActionListener(e);

        jbGuardarTodo = new JButton(new ImageIcon(getClass().getResource("icono/guardarTodo.png")));
        jbGuardarTodo.setToolTipText("Guardar todo (Ctrl+T)");
        jbGuardarTodo.setFocusable(false);
        jbGuardarTodo.addActionListener(e);

        jbCortar = new JButton(new ImageIcon(getClass().getResource("icono/cortar.png")));
        jbCortar.setToolTipText("Cortar (Ctrl+X)");
        jbCortar.setFocusable(false);
        jbCortar.addActionListener(e);

        jbCopiar = new JButton (new ImageIcon(getClass().getResource("icono/copiar.png")));
        jbCopiar.setToolTipText("Copiar (Ctrl+C)");
        jbCopiar.setFocusable(false);
        jbCopiar.addActionListener(e);

        jbPegar = new JButton (new ImageIcon(getClass().getResource("icono/pegar.png")));
        jbPegar.setToolTipText("Pegar (Ctrl+V)");
        jbPegar.setFocusable(false);
        jbPegar.addActionListener(e);

        jbRehacer = new JButton(new ImageIcon(getClass().getResource("icono/rehacer.png")));
        jbRehacer.setToolTipText("Rehacer (Ctrl+Y)");
        jbRehacer.setFocusable(false);
        jbRehacer.addActionListener(e);

        jbDeshacer = new JButton (new ImageIcon(getClass().getResource("icono/deshacer.png")));
        jbDeshacer.setToolTipText("Deshacer (Ctrl+Z)");
        jbDeshacer.setFocusable(false);
        jbDeshacer.addActionListener(e);

        jbAyuda = new JButton(new ImageIcon(getClass().getResource("icono/ayuda.png")));
        jbAyuda.setToolTipText("Ayuda");
        jbAyuda.setFocusable(false);
        jbAyuda.addActionListener(e);

        jtb.add(jbNuevoArchivo);
        jtb.add(jbAbrirArchivoNuevo);
        jtb.add(jbGuardarTodo);
        jtb.addSeparator();
        jtb.add(jbDeshacer);
        jtb.add(jbRehacer);
        jtb.addSeparator();
        jtb.add(jbCortar);
        jtb.add(jbCopiar);
        jtb.add(jbPegar);
        jtb.addSeparator();
        jtb.add(jbAyuda);
    }

    public void actualizarMenuItem (boolean undo, boolean redo) {
        jmiRehacer.setEnabled(redo);
        jmiDeshacer.setEnabled(undo);

        jbRehacer.setEnabled(redo);
        jbDeshacer.setEnabled(undo);
    }

    private JMenuBar confBarraDeMenu () {
        JMenuBar jMenuBar = new JMenuBar();

        //Menus.
        JMenu jmArchivo = new JMenu("Archivo");
        JMenu jmEditar = new JMenu("Editar");
        JMenu jmVer = new JMenu("Ver");
        JMenu jmAyuda = new JMenu("Ayuda");

        //Configuración de los menus.
        confMenuArchivo(jmArchivo);
        confMenuEditar(jmEditar);
        confMenuVer(jmVer);
        confMenuAyuda(jmAyuda);

        //Inserción de los menus a la barra de menus.
        jMenuBar.add(jmArchivo);
        jMenuBar.add(jmEditar);
        jMenuBar.add(jmVer);
        jMenuBar.add(jmAyuda);

        activarMenuItem(false); //Se desabilitan los menu item al inicio.

        return jMenuBar;
    }

    private void confMenuEditar (JMenu menu) {
        jmiDeshacer = new JMenuItem("Deshacer");
        jmiDeshacer.addActionListener(e);
        jmiDeshacer.setActionCommand("jmiDeshacer");
        jmiDeshacer.setIcon(new ImageIcon(getClass().getResource("icono/deshacer.png")));
        jmiDeshacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));

        jmiRehacer = new JMenuItem("Rehacer");
        jmiRehacer.addActionListener(e);
        jmiRehacer.setActionCommand("jmiRehacer");
        jmiRehacer.setIcon(new ImageIcon(getClass().getResource("icono/rehacer.png")));
        jmiRehacer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

        //Se desabilita los menus item 'Rehacer y Deshacer'.
        actualizarMenuItem(false, false);

        jmiCopiar = new JMenuItem("Copiar");
        jmiCopiar.addActionListener(e);
        jmiCopiar.setActionCommand("jmiCopiar");
        jmiCopiar.setIcon(new ImageIcon(getClass().getResource("icono/copiar.png")));
        jmiCopiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        jmiCortar = new JMenuItem("Cortar");
        jmiCortar.addActionListener(e);
        jmiCortar.setActionCommand("jmiCortar");
        jmiCortar.setIcon(new ImageIcon(getClass().getResource("icono/cortar.png")));
        jmiCortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        jmiPegar = new JMenuItem("Pegar");
        jmiPegar.addActionListener(e);
        jmiPegar.setActionCommand("jmiPegar");
        jmiPegar.setIcon(new ImageIcon(getClass().getResource("icono/pegar.png")));
        jmiPegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        jmiSelecTodo = new JMenuItem("Seleccionar todo");
        jmiSelecTodo.addActionListener(e);
        jmiSelecTodo.setActionCommand("jmiSelecTodo");
        jmiSelecTodo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        //Menu Editar, carga de menu item.
        menu.add(jmiRehacer);
        menu.add(jmiDeshacer);
        menu.addSeparator();
        menu.add(jmiCopiar);
        menu.add(jmiCortar);
        menu.add(jmiPegar);
        menu.add(jmiSelecTodo);
    }

    private void confMenuVer (JMenu menu) {
        int fontSize = ConfDeUsuario.getInt(KEY_FUENTE_TAMANIO);

        jmiAumentarFuente = new JMenuItem("Aumentar fuente");
        jmiAumentarFuente.setIcon(new ImageIcon(getClass().getResource("icono/zoomAum.png")));
        jmiAumentarFuente.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        jmiAumentarFuente.setActionCommand("jmiAumentarFuente");
        jmiAumentarFuente.setEnabled(fontSize != 30);
        jmiAumentarFuente.addActionListener(e);

        jmiDisminuirFuente = new JMenuItem("Disminuir fuente");
        jmiDisminuirFuente.setIcon(new ImageIcon(getClass().getResource("icono/zoomDism.png")));
        jmiDisminuirFuente.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LESS, ActionEvent.CTRL_MASK));
        jmiDisminuirFuente.setActionCommand("jmiDisminuirFuente");
        jmiDisminuirFuente.setEnabled(fontSize != 8);
        jmiDisminuirFuente.addActionListener(e);

        jmiTamanioOriginal = new JMenuItem("Restablecer fuente");
        jmiTamanioOriginal.setActionCommand("jmiTamanioOriginal");
        jmiTamanioOriginal.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        jmiTamanioOriginal.setEnabled(fontSize != 12);
        jmiTamanioOriginal.addActionListener(e);

        jcbGuiasDeIndentacion = new JCheckBoxMenuItem ("Guías de indentación", ConfDeUsuario.getBoolean(KEY_GUIAS_IDENTACION));
        jcbGuiasDeIndentacion.setActionCommand("jcbGuiasDeIndentacion");
        jcbGuiasDeIndentacion.addActionListener(e);

        jcbNumDeLineas = new JCheckBoxMenuItem("Numero de línea", ConfDeUsuario.getBoolean(KEY_NUM_LINEA));
        jcbNumDeLineas.setActionCommand("jcbNumDeLineas");
        jcbNumDeLineas.addActionListener(e);

        jcbToolBar = new JCheckBoxMenuItem("Barra de herramientas", ConfDeUsuario.getBoolean(KEY_BARRA_HERRAMIENTAS));
        jcbToolBar.setActionCommand("jcbToolBar");
        jcbToolBar.addActionListener(e);

        //Menu Ver, carga de menu item.
        JMenu jmTema = new JMenu("Tema de sintaxis");
        confMenuTema(jmTema);

        menu.add(jmiAumentarFuente);
        menu.add(jmiDisminuirFuente);
        menu.add(jmiTamanioOriginal);
        menu.addSeparator();
        menu.add(jmTema);
        menu.addSeparator();
        menu.add(jcbGuiasDeIndentacion);
        menu.add(jcbNumDeLineas);
        menu.add(jcbToolBar);
    }

    private void confMenuTema (JMenu menu) {
        ButtonGroup group = new ButtonGroup();
        boolean temaIntellij = ConfDeUsuario.getInt(KEY_TEMA) == EditorColor.INTELLIJ;

        jcbIntelliJ = new JCheckBoxMenuItem("IntelliJ", temaIntellij);
        jcbIntelliJ.setActionCommand("jcbIntelliJ");
        jcbIntelliJ.addActionListener(e);
        group.add(jcbIntelliJ);

        jcbDarcula = new JCheckBoxMenuItem("Darcula", !temaIntellij);
        jcbDarcula.setActionCommand("jcbDarcula");
        jcbDarcula.addActionListener(e);
        group.add(jcbDarcula);

        menu.add(jcbIntelliJ);
        menu.add(jcbDarcula);
    }

    private void confMenuAyuda (JMenu menu) {
        jmiAyuda = new JMenuItem("Ayuda");
        jmiAyuda.setIcon(new ImageIcon(getClass().getResource("icono/ayuda.png")));
        jmiAyuda.addActionListener(e);
        jmiAyuda.setActionCommand("jmiAyuda");

        jmiAcercaDeRabbit = new JMenuItem("Acerca de Rabbit");
        jmiAcercaDeRabbit.addActionListener(e);
        jmiAcercaDeRabbit.setActionCommand("jmiAcercaDeRabbit");

        menu.add(jmiAyuda);
        menu.addSeparator();
        menu.add(jmiAcercaDeRabbit);
    }

    private void confMenuArchivo (JMenu menu) {
        jmiNuevoArchivo = new JMenuItem("Nuevo archivo SL");
        jmiNuevoArchivo.addActionListener(e);
        jmiNuevoArchivo.setActionCommand("jmiNuevoArchivo");
        jmiNuevoArchivo.setIcon(new ImageIcon(getClass().getResource("icono/archivo.png")));
        jmiNuevoArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        jmiAbrirArchivo = new JMenuItem("Abrir archivo");
        jmiAbrirArchivo.addActionListener(e);
        jmiAbrirArchivo.setActionCommand("jmiAbrirArchivo");
        jmiAbrirArchivo.setIcon(new ImageIcon(getClass().getResource("icono/abrir.png")));
        jmiAbrirArchivo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        jmiGuardar = new JMenuItem("Guardar");
        jmiGuardar.addActionListener(e);
        jmiGuardar.setActionCommand("jmiGuardar");
        jmiGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        jmiGuardarComo = new JMenuItem("Guardar como");
        jmiGuardarComo.addActionListener(e);

        jmiGuardarTodo = new JMenuItem("Guardar todo");
        jmiGuardarTodo.addActionListener(e);
        jmiGuardarTodo.setActionCommand("jmiGuardarTodo");
        jmiGuardarTodo.setIcon(new ImageIcon(getClass().getResource("icono/guardarTodo.png")));
        jmiGuardarTodo.setAccelerator (KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));

        jmiCerrarPestania = new JMenuItem("Cerrar pestaña");
        jmiCerrarPestania.addActionListener(e);
        jmiCerrarPestania.setActionCommand("jmiCerrarPestania");
        jmiCerrarPestania.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        jmiSalir = new JMenuItem("Salir");
        jmiSalir.setActionCommand("jmiSalir");
        jmiSalir.addActionListener(e);

        //Menu archivo, carga de menu item.
        menu.add(jmiNuevoArchivo);
        menu.add(jmiAbrirArchivo);
        menu.addSeparator();
        menu.add(jmiGuardar);
        menu.add(jmiGuardarComo);
        menu.add(jmiGuardarTodo);
        menu.addSeparator();
        menu.add(jmiCerrarPestania);
        menu.add(jmiSalir);
    }

    private JPanel insertTab (EditorDeTexto editor) {
        JPanel jpTab = new JPanel();
        jpTab.setOpaque(false);
        jpTab.setLayout(new BoxLayout(jpTab, BoxLayout.X_AXIS));

        //Etiqueta que guarda el nombre del archivo.
        JLabel nombreArchivo = new JLabel(editor.getNombreArchivo());

        final JLabel jlCerrar = new JLabel (new ImageIcon(getClass().getResource("icono/c1.png")));
        //Se guarda la ruta del archivo como identificador para cerrar la pestaña.
        jlCerrar.setName(editor.getArchivoRuta());
        jlCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    JLabel jlCerrar = (JLabel) e.getSource();

                    EditorDeTexto editorDeTexto;
                    //Se busca la pestaña que contiene el editor de texto con la ruta obtenida de la pestaña que
                    // se desea cerrar.
                    for (int i = 0; i < jTabbedPane.getTabCount(); i ++) {
                        editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);

                        if (editorDeTexto.getArchivoRuta().equals(jlCerrar.getName())) {
                            tabClose(editorDeTexto);
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                jlCerrar.setIcon (new ImageIcon(getClass().getResource("icono/c2.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jlCerrar.setIcon (new ImageIcon(getClass().getResource("icono/c1.png")));
            }
        });
        jpTab.add(nombreArchivo);
        jpTab.add(Box.createHorizontalStrut(5));
        jpTab.add(jlCerrar);

        return jpTab; //Retorna la pestaña configurada.
    }

    private void tabClose (EditorDeTexto editor) {
        if (editor.archivoModificado()) {
            switch (mostrarDialogoGuardarCambios(editor.getNombreArchivo())) {
                case 0 :
                    //TODO : Debo comprobar si se pudo guardar para cerrarlo.
                    new EscribeArchivo(this).guardarArchivo(editor.getArchivoRuta(), editor.getText());
                    break;

                case 2 : //Cancelar
                    return;
            }
        }

        jTabbedPane.remove(editor); //Se cierra la pestaña.
        if (jTabbedPane.getTabCount() == 0) {
            activarMenuItem(false);
            actualizarMenuItem(false, false);
        }
    }

    private void tabUpdate (String title, int index) {
        //Extraigo el label del panel.
        JPanel panel = (JPanel) jTabbedPane.getTabComponentAt(index);
        JLabel jlNombreArchivo = (JLabel) panel.getComponent(0);
        //Actualizo el nombre del archivo.
        jlNombreArchivo.setText(title);
    }

    private void insertarNuevoEditor (EditorDeTexto editor) {
        jTabbedPane.add(editor);

        int index = jTabbedPane.getTabCount() - 1;
        jTabbedPane.setTabComponentAt(index, insertTab(editor));
        jTabbedPane.setToolTipTextAt(index, editor.getArchivoRuta());
        jTabbedPane.setSelectedIndex(index);

        if (jTabbedPane.getTabCount() == 1) activarMenuItem(true);
    }

    private void activarMenuItem (boolean state) {
        //Cambiar estado de los menus item.
        jmiGuardar.setEnabled(state);
        jmiGuardarComo.setEnabled(state);
        jmiGuardarTodo.setEnabled(state);
        jmiCerrarPestania.setEnabled(state);

        jmiCortar.setEnabled(state);
        jmiCopiar.setEnabled(state);
        jmiPegar.setEnabled(state);
        jmiSelecTodo.setEnabled(state);

        //Cambiar estado de los botones de la barra de herramientas.
        jbGuardarTodo.setEnabled(state);
        jbCortar.setEnabled(state);
        jbCopiar.setEnabled(state);
        jbPegar.setEnabled(state);

        if (state) {
            //Se obtiene el tamaño de la fuente de la configuración del usuario guardado.
            int fontSize = ConfDeUsuario.getInt(KEY_FUENTE_TAMANIO);

            jmiAumentarFuente.setEnabled(fontSize != 30);
            jmiDisminuirFuente.setEnabled(fontSize != 8);
            jmiTamanioOriginal.setEnabled(fontSize != 12);

        } else {
            jmiDisminuirFuente.setEnabled(false);
            jmiAumentarFuente.setEnabled(false);
            jmiTamanioOriginal.setEnabled(false);
        }
    }

    public void guardarArchivo (EditorDeTexto editor) {
        new EscribeArchivo(this).guardarArchivo(editor.getArchivoRuta(), editor.getText());
    }

    private void cerrarPrograma () {
        java.util.List <EditorDeTexto> list = new ArrayList<>();
        EditorDeTexto editorDeTexto;
        for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
            //Obtengo la información de los archivos abiertos.
            editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);
            if (editorDeTexto.archivoModificado())
                list.add(editorDeTexto);
        }

        switch (list.size()) {
            case 1 :
                EditorDeTexto editor = list.get(0);
                switch (mostrarDialogoGuardarCambios(editor.getNombreArchivo())) {
                    case 0: //Guardar el archivo.
                        //TODO : Debo comprobar si se pudo guardar para cerrarlo.
                        new EscribeArchivo(this).guardarArchivo(editor.getArchivoRuta(), editor.getText());
                        break;

                    case 2: //Cancelar
                        return;
                }

            case 0 :
                System.exit(0);

            default :
                int resp = DialogoGuardarVarios.mostrarDialogo(EditorUI.this, list);
                if (resp == DialogoGuardarVarios.OPER_TERMINADA)
                    System.exit(0);
        }
    }

    private int mostrarDialogoGuardarCambios (String nombreArchivo) {
        String message = "El archivo " + nombreArchivo + " se ha modificado. Guardar los cambios?";
        String [] bt = {"Guardar", "No Guardar", "Cancelar"};

        return CuadroDeDialogo.mostrar(this, message, "Guardar cambios", bt, CuadroDeDialogo.INFORMACION);
    }

    private void nuevoArchivo () {
        EscribeArchivo writer = new EscribeArchivo(EditorUI.this);
        if (writer.guardarArchivoValidarExtension(null, "inicio\n  cls()\n  \nfin"))
            insertarNuevoEditor(new EditorDeTexto(writer.getArhivoRuta(), null, EditorUI.this));
    }

    private void abrirArchivo () {
        LeerArchivo read = new LeerArchivo(EditorUI.this);
        if (read.archivoLeido()) { //Compruebo si el archivo se pudo leer.
            EditorDeTexto editorDeTexto;
            int i, tabCount = jTabbedPane.getTabCount();

            for (i = 0; i < tabCount; i ++) {
                editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);
                //Compruebo si el arhivo que se desea abrir ya esta abierto.
                if (editorDeTexto.getArchivoRuta().equals(read.getArchivoRuta())) {
                    if (editorDeTexto.archivoModificado())
                        editorDeTexto.setText(read.getText()); //Recargar el archivo.

                    jTabbedPane.setSelectedIndex(i);
                    break;
                }
            }
            if (tabCount == i) //Compruebo si el archivo no ha sido abierto.
                insertarNuevoEditor(new EditorDeTexto(read.getArchivoRuta(), read.getText(), EditorUI.this));
        }
    }

    private void guardarTodo () {
        EditorDeTexto editorDeTexto;
        for (int i = 0; i < jTabbedPane.getTabCount(); i ++) {
            editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);

            if (editorDeTexto.archivoModificado())
                new EscribeArchivo(EditorUI.this).guardarArchivo(editorDeTexto.getArchivoRuta(), editorDeTexto.getText());
        }
    }

    private void actTemaDeTodosLosEditores (int tema) {
        EditorDeTexto editorDeTexto;
        for (int i = 0; i < jTabbedPane.getTabCount(); i ++) {
            editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);

            editorDeTexto.actualizarTema(tema);
        }
    }

    //Actualiza la fuente de todos los editores abiertos.
    private void actFuentDeTodosLosEditores () {
        EditorDeTexto editorDeTexto;
        for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
            editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);

            editorDeTexto.actualizarFuente();
        }
    }

    private class EventosMenuItem implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "jmiNuevoArchivo" :
                    nuevoArchivo();
                    break;

                case "jmiAbrirArchivo" :
                    abrirArchivo();
                    break;

                default :
                    EditorDeTexto editor = (EditorDeTexto) jTabbedPane.getSelectedComponent();

                    switch (e.getActionCommand()) {
                        case "jmiGuardar" :
                            guardarArchivo(editor);
                            break;

                        case "jmiGuardarComo" :
                            EscribeArchivo escribe = new EscribeArchivo(EditorUI.this);
                            if (escribe.guardarArchivo(editor.getText())) {
                                //Se actualiza el nombre de la pestaña.
                                tabUpdate(escribe.getNombreArchivo(), jTabbedPane.getSelectedIndex());
                                //Se agrega la ruta del archivo guardado.
                                editor.setArchivoRuta(escribe.getArhivoRuta());
                                //Nuevo nombre de archivo.
                                editor.setNombreArchivo(escribe.getNombreArchivo());

                                //Se actualiza el identificador del archivo.
                                JPanel panel = (JPanel) jTabbedPane.getTabComponentAt(jTabbedPane.getSelectedIndex());
                                panel.getComponent(2).setName(escribe.getArhivoRuta());
                            }
                            break;

                        case "jmiGuardarTodo" :
                            guardarTodo();
                            break;

                        case "jmiCerrarPestania" :
                            tabClose(editor);
                            break;

                        case "jmiSalir" :
                            cerrarPrograma();
                            break;

                        case "jmiCopiar" :
                            editor.copiar();
                            break;

                        case "jmiCortar" :
                            editor.cortar();
                            break;

                        case "jmiPegar" :
                            editor.pegar();
                            break;

                        case "jmiSelecTodo" :
                            editor.seleccTodo();
                            break;

                        case "jmiRehacer" :
                            editor.rehacer();
                            break;

                        case "jmiDeshacer" :
                            editor.deshacer();
                            break;

                        case "jcbIntelliJ" :
                            if (jcbIntelliJ.isSelected() && EditorColor.tema == EditorColor.DARCULA) {
                                ConfDeUsuario.putInt(KEY_TEMA, EditorColor.INTELLIJ); //Se guarda nuevo tema.

                                actTemaDeTodosLosEditores(EditorColor.INTELLIJ);
                            }
                            break;

                        case "jcbDarcula" :
                            if (jcbDarcula.isSelected() && EditorColor.tema == EditorColor.INTELLIJ) {
                                ConfDeUsuario.putInt(KEY_TEMA, EditorColor.DARCULA); //Se guarda nuevo tema.

                                actTemaDeTodosLosEditores(EditorColor.DARCULA);
                            }
                            break;

                        case "jmiAumentarFuente" :
                            if (EditorDeTexto.fontSize < 30) {
                                EditorDeTexto.fontSize ++;

                                actFuentDeTodosLosEditores();

                                if (EditorDeTexto.fontSize == 30) jmiAumentarFuente.setEnabled(false);

                                //Se guarda nuevo tamaño de fuente.
                                ConfDeUsuario.putInt(KEY_FUENTE_TAMANIO, EditorDeTexto.fontSize);
                            }

                            if (!jmiDisminuirFuente.isEnabled()) jmiDisminuirFuente.setEnabled(true);

                            //Configurar estado del menu item 'Restablecer tamaño'
                            if (EditorDeTexto.fontSize != 12) {
                                if (!jmiTamanioOriginal.isEnabled()) jmiTamanioOriginal.setEnabled(true);

                            } else {
                                jmiTamanioOriginal.setEnabled(false);
                            }
                            break;

                        case "jmiDisminuirFuente" :
                            if (EditorDeTexto.fontSize > 8) {
                                EditorDeTexto.fontSize --;

                                actFuentDeTodosLosEditores();

                                if (EditorDeTexto.fontSize == 8) jmiDisminuirFuente.setEnabled(false);

                                //Se guarda nuevo tamaño de fuente.
                                ConfDeUsuario.putInt(KEY_FUENTE_TAMANIO, EditorDeTexto.fontSize);
                            }

                            if (!jmiAumentarFuente.isEnabled()) jmiAumentarFuente.setEnabled(true);

                            //Configurar estado del menu item 'Restablecer tamaño'
                            if (EditorDeTexto.fontSize != 12) {
                                if (!jmiTamanioOriginal.isEnabled()) jmiTamanioOriginal.setEnabled(true);

                            } else {
                                jmiTamanioOriginal.setEnabled(false);
                            }
                            break;

                        case "jmiTamanioOriginal" :
                            EditorDeTexto.fontSize = 12;

                            actFuentDeTodosLosEditores();

                            jmiTamanioOriginal.setEnabled(false);
                            if (!jmiAumentarFuente.isEnabled()) jmiAumentarFuente.setEnabled(true);
                            if (!jmiDisminuirFuente.isEnabled()) jmiDisminuirFuente.setEnabled(true);

                            //Se guarda nuevo tamaño de fuente.
                            ConfDeUsuario.putInt(KEY_FUENTE_TAMANIO, EditorDeTexto.fontSize);
                            break;

                        case "jcbNumDeLineas" :
                            boolean state = jcbNumDeLineas.isSelected();

                            ConfDeUsuario.putBoolean(KEY_NUM_LINEA, state);

                            EditorDeTexto editorDeTexto;
                            for (int i = 0; i < jTabbedPane.getTabCount(); i ++) {
                                editorDeTexto = (EditorDeTexto) jTabbedPane.getComponentAt(i);

                                editorDeTexto.habilitarNumLineas(state);
                            }
                            break;

                        case "jcbToolBar" :
                            boolean est = jcbToolBar.isSelected();

                            jtb.setVisible(est);

                            //Guardo configuración de usuario.
                            ConfDeUsuario.putBoolean(KEY_BARRA_HERRAMIENTAS, est);
                            break;

                        case "jmiAyuda" :
                            new Ayuda(EditorUI.this);
                            break;
                    }
            }
        }
    }

    private class EventosBarraHerr implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == jbNuevoArchivo) {
                nuevoArchivo();

            } else if (e.getSource() == jbAbrirArchivoNuevo) {
                abrirArchivo();

            } else {
                EditorDeTexto editor = (EditorDeTexto) jTabbedPane.getSelectedComponent();

                if (e.getSource() == jbGuardarTodo)
                    guardarTodo();

                else if (e.getSource() == jbDeshacer)
                    editor.deshacer();

                else if (e.getSource() == jbRehacer)
                    editor.rehacer();

                else if (e.getSource() == jbCortar)
                    editor.cortar();

                else if (e.getSource () == jbCopiar)
                    editor.copiar();

                else if (e.getSource() == jbPegar)
                    editor.pegar();

                else if (e.getSource() == jbAyuda)
                    new Ayuda(EditorUI.this);
            }
        }
    }
}