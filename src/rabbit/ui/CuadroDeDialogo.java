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
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CuadroDeDialogo extends JDialog implements ActionListener {
    private int indexBoton;
    public static final int INFORMACION = 0, ERROR = 1, ADVERTENCIA = 2;

    private CuadroDeDialogo(JFrame padre, String mens, String titulo, String[] opciones, int tipoMens) {
        super(padre, true);
        setTitle(titulo);
        setResizable(false);
        setLayout(new GridBagLayout());
        setIconImage(new ImageIcon(getClass().getResource("icono/rabbit.png")).getImage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                indexBoton = 2;
                dispose();
            }
        });

        GridBagConstraints conf = new GridBagConstraints();
        //Componente de la fila 0 columna 0.
        conf.gridx = conf.gridy = 0;
        conf.weighty = 1.0;
        conf.insets = new Insets(30, 10, 30, 10);

        JLabel icono;
        switch (tipoMens) {
            case INFORMACION:
                icono = new JLabel(new ImageIcon(getClass().getResource("icono/dialogoPreg.png")));
                break;
            case ERROR:
                icono = new JLabel(new ImageIcon(getClass().getResource("icono/dialogoError.png")));
                break;
            default:
                icono = new JLabel(new ImageIcon(getClass().getResource("icono/dialogoAdvertencia.png")));
                break;
        }
        add(icono, conf);

        //Componente de la fila 0 columna 1.
        conf.gridx = 1;
        conf.insets = new Insets(30, 0, 30, 10);

        JLabel mensaje = new JLabel(mens);
        add(mensaje, conf);

        //Componente de la fila 1 columna 0.
        conf.gridx = 0;
        conf.gridy = 1;
        conf.gridwidth = 2;
        conf.weighty = 0.0;
        conf.weightx = 1.0;
        conf.insets = new Insets(0, 10, 10, 10);
        conf.fill = GridBagConstraints.HORIZONTAL;

        add(panelBotones(opciones), conf);

        pack();
        setLocationRelativeTo(padre); //Localiza el cuadro de dialogo en el medio de su contenedor.
        setVisible(true);
    }

    private JPanel panelBotones(String[] opciones) {
        JPanel panel = new JPanel();

        JButton jb;
        for (int i = 0; i < opciones.length; i++) {
            jb = new JButton(opciones[i]);
            jb.addActionListener(this);
            jb.setActionCommand("" + i);
            panel.add(jb);
        }

        return panel;
    }

    public static int mostrar(JFrame padre, String mens, String titulo, String[] opciones, int tipoMens) {
        return new CuadroDeDialogo(padre, mens, titulo, opciones, tipoMens).indexBoton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        indexBoton = Integer.parseInt(e.getActionCommand());
        dispose();
    }
}