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

import rabbit.io.EscribeArchivo;
import rabbit.io.LeerArchivo;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import static javax.swing.UIManager.*;

public class EditorUI extends JFrame {
    private JTabbedPane jTabbedPane;

    public EditorUI () {
        super ("Rabbit");
        setIconImage(new ImageIcon(getClass().getResource("icono/rabbit.png")).getImage());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane = new JTabbedPane();
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        add (jTabbedPane, BorderLayout.CENTER);

        setJMenuBar(confMenuBar());

        setVisible(true);
    }

    private JMenuBar confMenuBar () {
        //Clase que manejara los eventos de los menu item.
        Eventos eventos = new Eventos ();

        JMenuBar jMenuBar = new JMenuBar();
        //Menus.
        JMenu jmArchivo = new JMenu("Archivo");
        JMenu jmEditar = new JMenu("Editar");
        JMenu jmVer = new JMenu("Ver");
        JMenu jmAyuda = new JMenu("Ayuda");
        JMenuItem jmiNuevoArchivo = new JMenuItem("Nuevo archivo");
        jmiNuevoArchivo.addActionListener(eventos);
        jmiNuevoArchivo.setActionCommand("jmiNuevoArchivo");

        //Menu item del menu Archivo.
        JMenuItem jmiAbrirArchivo = new JMenuItem("Abrir archivo");
        jmiAbrirArchivo.setActionCommand("jmiAbrirArchivo");
        jmiAbrirArchivo.addActionListener(eventos);
        JMenuItem jmiGuardar = new JMenuItem("Guardar");
        jmiGuardar.setActionCommand("jmiGuardar");
        jmiGuardar.addActionListener(eventos);
        JMenuItem jmiGuardarComo = new JMenuItem("Guardar como");
        jmiGuardarComo.setActionCommand("jmiGuardarComo");
        jmiGuardarComo.addActionListener(eventos);
        JMenuItem jmiGuardarTodo = new JMenuItem("Guardar todo");
        JMenuItem jmiCerrarPestania = new JMenuItem("Cerrar pestaña");
        jmiCerrarPestania.addActionListener(eventos);
        jmiCerrarPestania.setActionCommand("jmiCerrarPestaña");
        JMenuItem jmiSalir = new JMenuItem("Salir");
        jmiSalir.addActionListener(eventos);
        jmiSalir.setActionCommand("jmiSalir");

        //Menu item del menu Editar.
        JMenuItem jmiDeshacer = new JMenuItem("Deshacer");
        jmiDeshacer.addActionListener(eventos);
        jmiDeshacer.setActionCommand("jmiDeshacer");
        JMenuItem jmiRehacer = new JMenuItem("Rehacer");
        jmiRehacer.addActionListener(eventos);
        jmiRehacer.setActionCommand("jmiRehacer");

        JMenuItem jmiCopiar = new JMenuItem("Copiar");
        jmiCopiar.addActionListener(eventos);
        jmiCopiar.setActionCommand("jmiCopiar");
        JMenuItem jmiCortar = new JMenuItem("Cortar");
        jmiCortar.addActionListener(eventos);
        jmiCortar.setActionCommand("jmiCortar");

        JMenuItem jmiPegar = new JMenuItem("Pegar");
        jmiPegar.addActionListener(eventos);
        jmiPegar.setActionCommand("jmiPegar");
        JMenuItem jmiSelecTodo = new JMenuItem("Seleccionar todo");
        jmiSelecTodo.addActionListener(eventos);
        jmiSelecTodo.setActionCommand("jmiSelecTodo");

        //Menu archivo, carga de menu item.
        jmArchivo.add(jmiNuevoArchivo);
        jmArchivo.add(jmiAbrirArchivo);
        jmArchivo.addSeparator();
        jmArchivo.add(jmiGuardar);
        jmArchivo.add(jmiGuardarComo);
        jmArchivo.add(jmiGuardarTodo);
        jmArchivo.addSeparator();
        jmArchivo.add(jmiCerrarPestania);
        jmArchivo.add(jmiSalir);

        //Menu Editar, carga de menu item.
        jmEditar.add(jmiRehacer);
        jmEditar.add(jmiDeshacer);
        jmEditar.addSeparator();
        jmEditar.add(jmiCopiar);
        jmEditar.add(jmiCortar);
        jmEditar.add(jmiPegar);
        jmEditar.add(jmiSelecTodo);

        jMenuBar.add(jmArchivo);
        jMenuBar.add(jmEditar);
        jMenuBar.add(jmVer);
        jMenuBar.add(jmAyuda);

        return jMenuBar;
    }

    private JPanel insertTab (final String fileName) {
        JPanel jpTab = new JPanel();
        jpTab.setOpaque(false);
        jpTab.setLayout(new BoxLayout(jpTab, BoxLayout.X_AXIS));

        JLabel nombreArchivo; //Etiqueta que guarda el nombre del archivo.

        if (fileName == null)
            nombreArchivo = new JLabel("Sin nombre"); //Nombre por defecto.
        else
            nombreArchivo = new JLabel(fileName);

        JLabel jlCerrar = new JLabel (new ImageIcon(getClass().getResource("icono/cerrar.png")));
        jlCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EditorDeTexto editor = (EditorDeTexto) jTabbedPane.getSelectedComponent();

                if (editor.existeArchivo()) {
                    //TODO : Debo saber si se ha modificado el archivo.
                    String message = "El archivo " + editor.getNombreArchivo() + " se ha modificado. Guardar ?";
                    switch (showOptionDialog(message, "Guardar cambios")){
                        case 0 : //Guardar el archivo.
                            //TODO : El debo comprobar si se pudo guardar para cerrarlo, o que salga un mensaje para reemplazarlo.
                            new EscribeArchivo().guardarArchivo(editor.getArchivoRuta(), editor.getText());

                        case 1 : //No guardar.
                            jTabbedPane.remove(editor);
                            break;
                    }
                } else {
                    String menssage = "Desea guardar el archivo ?";
                    switch (showOptionDialog(menssage, "Guardar")) {
                        case 0 : //Guardar el archivo.
                            new EscribeArchivo(editor.getText());

                        case 1 : //No guardar.
                            jTabbedPane.remove(editor);
                            break;
                    }
                }
            }
        });

        jpTab.add(nombreArchivo);
        jpTab.add(Box.createHorizontalStrut(5));
        jpTab.add(jlCerrar);

        return jpTab; //Retorna la pestaña configurada.
    }

    private int showOptionDialog (String message, String title) {
        Object [] option = {"Guardar", "No guardar", "Cancelar"};
        return JOptionPane.showOptionDialog(null, message, title, JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, option, null);
    }

    public static void main (String [] args) throws Exception {
        setLookAndFeel (getSystemLookAndFeelClassName ());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EditorUI();
            }
        });
    }

    //Clase para escuchar los eventos de los menu item.
    private class Eventos implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorDeTexto editor = (EditorDeTexto) jTabbedPane.getSelectedComponent();

            if (e.getActionCommand().equals("jmiNuevoArchivo")) {
                jTabbedPane.add(new EditorDeTexto());
                jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount() - 1, insertTab(null));
                jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount() - 1);

            } else if (e.getActionCommand().equals("jmiCerrarPestaña")) {
                jTabbedPane.remove(jTabbedPane.getSelectedComponent());

            } else if (e.getActionCommand().equals("jmiSalir")) {
                System.exit(0);

            } else if (e.getActionCommand().equals("jmiRehacer")) {

            } else if (e.getActionCommand().equals("jmiCopiar")) {

            } else if (e.getActionCommand().equals("jmiCortar")) {

            } else if (e.getActionCommand().equals("jmiPegar")) {

            } else if (e.getActionCommand().equals("jmiSelectTodo")) {

            } else if (e.getActionCommand().equals("jmiGuardar")) {
                if (editor.existeArchivo()) {
                    new EscribeArchivo().guardarArchivoValidarExtension(editor.getArchivoRuta(), editor.getText());

                } else {
                    EscribeArchivo writer = new EscribeArchivo();
                    if (writer.guardarArchivoValidarExtension(editor.getArchivoRuta(), editor.getText())) {
                        //Se agrega la ruta del archivo guardado.
                        editor.setArchivoRuta(writer.getArhivoRuta());
                        //Extraigo el label del panel.
                        JPanel panel = (JPanel) jTabbedPane.getTabComponentAt(jTabbedPane.getSelectedIndex());
                        JLabel jlNombreArchivo = (JLabel) panel.getComponent(0);
                        //Pongo el nombre del archivo en el label.
                        jlNombreArchivo.setText(writer.getNombreArchivo());
                    }
                }
            } else if (e.getActionCommand().equals("jmiGuardarComo")){
                EscribeArchivo escribe = new EscribeArchivo();
                if (escribe.guardarArchivo(editor.getText())) {
                    //Se agrega la ruta del archivo guardado.
                    editor.setArchivoRuta(escribe.getArhivoRuta());
                    JPanel panel = (JPanel) jTabbedPane.getTabComponentAt(jTabbedPane.getSelectedIndex());
                    JLabel jlNombreArchivo = (JLabel) panel.getComponent(0);
                    //Se cambia el nombre del archivo.
                    jlNombreArchivo.setText(escribe.getNombreArchivo());
                }

            } else if (e.getActionCommand().equals("jmiAbrirArchivo")) {
                LeerArchivo read = new LeerArchivo();
                if (read.archivoLeido()) {
                    jTabbedPane.add(new EditorDeTexto(read.getArchivoRuta(), read.getText()));
                    jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount() - 1, insertTab(read.getNombreArchivo()));
                    jTabbedPane.setSelectedIndex(jTabbedPane.getTabCount() - 1);
                }
            }
        }
    }
}
