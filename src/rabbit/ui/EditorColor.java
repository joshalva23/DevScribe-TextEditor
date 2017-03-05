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
import javax.swing.text.*;

public class EditorColor {
    static final int INTELLIJ = 0;
    static final int DARCULA = 1;

    static int tema;

    AttributeSet defaultText, keyword, number, string, comment, field, method;

    EditorColor (final int tema) {
        EditorColor.tema = tema;

        StyleContext sc = StyleContext.getDefaultStyleContext();

        switch (tema) {
            case DARCULA :
                defaultText = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0xA9B7C6));
                keyword = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0xCC7832));
                number = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0x6897BB));
                string = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0x6A8759));
                comment = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0x808080));
                field = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0x9876AA));
                method = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color(0xFFC66D));
                break;

            case  INTELLIJ :
                defaultText = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color (0x000000));
                keyword = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color (0x000080));
                number = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color (0x0000FF));
                string = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color (0x008000));
                SimpleAttributeSet commentNew = new SimpleAttributeSet();
                StyleConstants.setForeground(commentNew, new Color (0x808080));
                StyleConstants.setItalic(commentNew, true);
                StyleConstants.setBold(commentNew, false);

                comment = commentNew;

                field = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color (0x660E7A));
                method = sc.addAttribute(sc.getEmptySet(), StyleConstants.Foreground, new Color (0x000000));
                break;
        }
    }
}
