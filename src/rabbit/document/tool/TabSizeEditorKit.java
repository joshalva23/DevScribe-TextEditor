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

import javax.swing.text.*;

public class TabSizeEditorKit extends StyledEditorKit {
    private static final int TAB_SIZE = 27;

    @Override
    public ViewFactory getViewFactory() {
        return new MyViewFactory();
    }

    private static class MyViewFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName))
                    return new LabelView(elem);

                else if (kind.equals(AbstractDocument.ParagraphElementName))
                    return new CustomTabParagraphView(elem);

                else if (kind.equals(AbstractDocument.SectionElementName))
                    return new BoxView(elem, View.Y_AXIS);

                else if (kind.equals(StyleConstants.ComponentElementName))
                    return new ComponentView(elem);

                else if (kind.equals(StyleConstants.IconElementName))
                    return new IconView(elem);
            }

            return new LabelView(elem);
        }
    }

    private static class CustomTabParagraphView extends ParagraphView {
        public CustomTabParagraphView(Element elem) {
            super(elem);
        }

        @Override
        public float nextTabStop(float x, int tabOffset) {
            TabSet tabs = getTabSet();
            if(tabs == null)
                return getTabBase() + (((int)x / TAB_SIZE + 1) * TAB_SIZE);

            return super.nextTabStop(x, tabOffset);
        }
    }
}
