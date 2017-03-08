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

package rabbit.document.tool;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.event.*;

/*
 *  Track the movement of the Caret by painting a background line at the
 *  current caret position.
 */
public class PintarLinea extends MouseAdapter implements Highlighter.HighlightPainter, CaretListener, MouseMotionListener {
    private JTextComponent component;
    private Rectangle lastView;
    private Color color;

    public PintarLinea(JTextComponent component) {
        this(component, component.getSelectionColor());
    }

    /*
     *  Manually control the line color.
     *
     *  @param component text component that requires background line painting.
     *  @param color the color of the background line.
     */
    public PintarLinea(JTextComponent component, Color color) {
        this.component = component;
        setColor(color);

        // Add listeners so we know when to change highlighting.
        component.addCaretListener(this);
        component.addMouseListener(this);
        component.addMouseMotionListener(this);

        // Turn highlighting on by adding a dummy highlight.
        try {
            component.getHighlighter().addHighlight(0, 0, this);

        } catch(BadLocationException ble) {}
    }

    /*
     *	You can reset the line color at any time.
     *
     *  @param color the color of the background line.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    // Paint the background highlight
    public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
        try {
            Rectangle r = c.modelToView(c.getCaretPosition());
            g.setColor(color);
            g.fillRect(0, r.y, c.getWidth(), r.height);

            if (lastView == null) lastView = r;

        } catch (BadLocationException ex) {}
    }

    /*
    *   Caret position has changed, remove the highlight.
    */
    private void resetHighlight() {
        // Use invokeLater to make sure updates to the Document are completed,
        // otherwise Undo processing causes the modelToView method to loop.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    int offset =  component.getCaretPosition();
                    Rectangle currentView = component.modelToView(offset);

                    // Remove the highlighting from the previously highlighted line.
                    if (lastView != null && lastView.y != currentView.y) {
                        component.repaint(0, lastView.y, component.getWidth(), lastView.height);
                        lastView = currentView;
                    }
                } catch(BadLocationException ex) {}
            }
        });
    }

    // Implement CaretListener.
    public void caretUpdate(CaretEvent e) {
        resetHighlight();
    }

    // Implement MouseListener.
    public void mousePressed(MouseEvent e) {
        resetHighlight();
    }

    // Implement MouseMotionListener.
    public void mouseDragged(MouseEvent e) {
        resetHighlight();
    }
}
