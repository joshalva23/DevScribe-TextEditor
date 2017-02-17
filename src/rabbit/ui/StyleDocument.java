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

import javax.swing.text.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleDocument extends DefaultStyledDocument {
    //Palabras reservadas del lenguaje SL.
    private final String reservedWord = "(and|archivo|caso|constantes|const|cadena|desde|eval|fin|hasta|inicio|libext|" +
            "lib|logico|matriz|mientras|not|numerico|or|paso|programa|subrutina|sub|ref|registro|repetir|retorna|salir|sino|" +
            "si|tipos|variables|var|vector)";

    private String text;
    private EditorColor color;

    StyleDocument () {
        color = new EditorColor(EditorColor.DARCULA);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);

        resaltarSintaxis();
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);

        resaltarSintaxis();
    }

    private void resaltarSintaxis () throws BadLocationException {
        setCharacterAttributes(0, getLength(), color.defaultText, true);

        updateText(); //Actualiza la variable que guarda el contenido del editor de texto.

        //Resaltar numeros.
        pintar ("\\d+", color.number);

        //Constantes definidos por defecto por SL.
        SimpleAttributeSet s = new SimpleAttributeSet(color.field);
        StyleConstants.setItalic(s, true);
        pintar ("\\b(TRUE|FALSE|SI|NO)\\b", s);

        //Resaltar nombre de rutinas.
        pintar ("\\b(sub|subrutina)\\s+(\\w+)", 2, color.method);

        //Resaltar palabras claves.
        pintar ("\\b" + reservedWord + "\\b", color.keyword);

        //Resaltar ',' y ';'
        pintar("[,;]", color.keyword);

        //Resaltar cadena de caracteres.
        pintar ("(\"[^\"]*\")*", color.string);

        //Resaltar comentarios.
        pintar ("(/\\*(\\s*|.*?)*\\*/)|(//.*)", color.comment);
    }

    private void pintar (final String regex, AttributeSet style) {
        pintar(regex, 0, style);
    }

    private void pintar (final String regex, int group, AttributeSet style) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find())
            setCharacterAttributes(matcher.start(group), matcher.end(group) - matcher.start(group), style, false);
    }

    private void updateText () {
        try {
            text = getText(0, getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
