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
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionListener;

public class DialogoGuardarVarios extends JDialog implements ActionListener {
    private EditorUI editorUI;
    private JList <EditorDeTexto> jlArchivos;
    private JButton jbGuardar, jbGuardarTodo, jbNoGuardarNinguno, jbCancelar;

    static final int OPER_CANCELADA = 1;
    static final int OPER_TERMINADA = 0;

    private int returnValue = OPER_TERMINADA;

    private int cantItem;

    private DialogoGuardarVarios (JFrame parent, java.util.List <EditorDeTexto> listArchivos) {
        super (parent, "Guardar cambios", true);
        setSize (370, 170);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("icono/rabbit.png")).getImage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnValue = OPER_CANCELADA;
            }
        });

        editorUI = (EditorUI) parent;
        cantItem = listArchivos.size();

        GridBagConstraints conf = new GridBagConstraints();

        //Fila 0, Columna 0.
        conf.gridx = conf.gridy = 0;
        conf.gridheight = 4;
        conf.weightx = conf.weighty = 1.0;
        conf.fill = GridBagConstraints.BOTH;
        conf.insets = new Insets(10, 10, 10, 10);

        jlArchivos = new JList <> (new DefaultListModel<EditorDeTexto>());

        //Cargar lista.
        DefaultListModel <EditorDeTexto> listModel = (DefaultListModel <EditorDeTexto>)jlArchivos.getModel();
        for (EditorDeTexto editor : listArchivos)
            listModel.addElement(editor);

        jlArchivos.setSelectedIndex(0);
        add (new JScrollPane(jlArchivos), conf);

        //Fila 0, Columna 1.
        conf.gridx = 1;
        conf.gridheight = 1;
        conf.weighty = conf.weightx = 0.0;
        conf.fill = GridBagConstraints.HORIZONTAL;
        conf.insets = new Insets(10, 0, 10, 10);

        jbGuardar = new JButton("Guardar");
        jbGuardar.addActionListener(this);
        add (jbGuardar, conf);

        //Fila 1, Columna 1.
        conf.gridy = 1;
        conf.insets = new Insets(0, 0, 10, 10);

        jbGuardarTodo = new JButton("Guardar todo");
        jbGuardarTodo.addActionListener(this);
        add (jbGuardarTodo, conf);

        //Fila 2, Columna 1.
        conf.gridy = 2;

        jbNoGuardarNinguno = new JButton("Ignorar todo");
        jbNoGuardarNinguno.addActionListener(this);
        add (jbNoGuardarNinguno, conf);

        //Fila 3, Columna 1.
        conf.gridy = 3;

        jbCancelar = new JButton("Cancelar");
        jbCancelar.addActionListener(this);
        add(jbCancelar, conf);

        setVisible(true);
    }

    static int mostrarDialogo (JFrame parent, java.util.List <EditorDeTexto> listArchivos) {
        return new DialogoGuardarVarios(parent, listArchivos).returnValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbGuardar) {
            int cantItemSelec = jlArchivos.getSelectedIndices().length;

            //Compruebo si esta seleccionado uno o mas item.
            if (cantItemSelec != 0) {
                //Se obtiene la cantidad de item que no seran removidos.
                cantItem -= cantItemSelec;
                for (EditorDeTexto editor : jlArchivos.getSelectedValuesList()) {
                    editorUI.guardarArchivo(editor);

                    if (cantItem != 0)
                        ((DefaultListModel<EditorDeTexto>) jlArchivos.getModel()).removeElement(editor);
                }

                switch (cantItem) {
                    case 0:
                        dispose(); //Cerrar.
                        break;
                    case 1:
                        jbGuardarTodo.setEnabled(false);
                        jbNoGuardarNinguno.setText("No guardar");
                        break;
                }
            }

        } else {
            if (e.getSource() == jbGuardarTodo) {
                for (int i = 0; i < jlArchivos.getModel().getSize(); i++) {
                    EditorDeTexto editor = jlArchivos.getModel().getElementAt(i);
                    editorUI.guardarArchivo(editor);
                }

            } else {
                if (e.getSource() == jbCancelar) returnValue = OPER_CANCELADA;
            }

            dispose(); //Cerrar.
        }
    }
}
