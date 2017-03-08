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

import rabbit.io.ConfDeUsuario;
import rabbit.ui.EditorColor;

import javax.swing.text.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static rabbit.io.ConfDeUsuario.KEY_TEMA;

public class DocumentoEstilo extends DefaultStyledDocument {
    //Palabras reservadas del lenguaje SL.
    private final String reservedWord = "(and|archivo|caso|constantes|const|cadena|desde|eval|fin|hasta|inicio|libext|" +
            "lib|logico|matriz|mientras|not|numerico|or|paso|programa|subrutina|sub|ref|registro|repetir|retorna|salir|sino|" +
            "si|tipos|variables|var|vector)";

    private String text;
    private EditorColor color;
    private JTextComponent textComponent;
    private int [] limite = new int [2];

    public DocumentoEstilo (JTextComponent textComponent) {
        color = new EditorColor(ConfDeUsuario.getInt (KEY_TEMA));
        this.textComponent = textComponent;
    }

    public void actualizarColorDocumento(EditorColor color) {
        this.color = color;
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);

        if (len != 0) {
            int posc = offs != 0 ? offs - 1 : 0;
            linea(posc, posc); //Obtengo los limites de la linea.

            //Guardo la linea alterada.
            text = getText(limite [0], (limite [1] - limite [0]) + 1);

            if (text.trim().length() > 0) resaltarSintaxis();
        }
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);

        if (str.length() == 1) {
            switch (str) {
                case "(":
                    insertString(offs + 1, ")", a);
                    textComponent.setCaretPosition(offs + 1);
                    break;

                case "[":
                    insertString(offs + 1, "]", a);
                    textComponent.setCaretPosition(offs + 1);
                    break;

                case "{":
                    insertString(offs + 1, "}", a);
                    textComponent.setCaretPosition(offs + 1);
                    break;

                case "\"":
                    insertString(offs + 1, ".\"", a);
                    textComponent.setCaretPosition(offs + 1);
                    break;
            }
        }

        linea(offs, (offs + str.length()) - 1);
        text = getText(limite [0], (limite [1] - limite [0]) +  1);

        if (text.trim().length() > 0) resaltarSintaxis();
    }

    private void resaltarSintaxis () throws BadLocationException {
        setCharacterAttributes(limite[0], text.length(), color.defaultText, false);

        //Resaltar numeros enteros y decimal.
        pintar ("\\d+(?:\\.{1}\\d+)?", color.number);

        //Constantes definidos por defecto por SL.
        SimpleAttributeSet s = new SimpleAttributeSet(color.field);
        StyleConstants.setItalic(s, true);
        pintar ("\\b(TRUE|FALSE|SI|NO)\\b", s);

        //Resaltar nombre de rutinas.
        pintar ("\\b(?:sub|subrutina)\\s+(\\w+)", 1, color.method);

        //Resaltar palabras claves.
        pintar ("\\b" + reservedWord + "\\b", color.keyword);

        //Resaltar ',' y ';'
        pintar("[,;]", color.keyword);

        //Resaltar cadena de caracteres.
        pintar ("\".*?\"|'.*?'", color.string);

        //Resaltar comentarios.
        pintar ("(?:(?s)/\\*.*?\\*/)|(?://.*)", color.comment);
    }

    private void pintar (final String regex, AttributeSet style) {
        pintar(regex, 0, style);
    }

    private void pintar (final String regex, int group, AttributeSet style) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find())
            setCharacterAttributes(limite [0] + matcher.start(group), matcher.group(group).length(), style, false);
    }

    private void linea (int l1, int l2) throws BadLocationException {
        limite [0] = poscInicioLinea(l1);
        limite [1] = poscFinalLinea(l2);
    }

    private int poscInicioLinea (int posc) throws BadLocationException {
        String text = texto();

        //Compruebo si el valor del parametro no es el primer elemento.
        if (posc != 0) {
            for (int i = posc; i >= 0; i --) {
                if (text.charAt(i) == '\n')
                    return i;
            }
        }

        return 0;
    }

    private int poscFinalLinea (int posc) throws BadLocationException {
        String text = texto();
        int length = text.length();

        //Compruebo si el valor del parametro no es el ultimo elemento.
        if (posc != (length == 0 ? 0 : length - 1)) {
            for (int i = posc; i < length; i ++) {
                if (text.charAt(i) == '\n')
                    return i;
            }
        }

        return length == 0 ? 0 : length - 1;
    }

    private String texto () {
        try {
            return getText(0, getLength());

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return null;
    }
}