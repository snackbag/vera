package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VLabel;

public class LabelStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VLabel.class, "background-color", VColor.transparent());
        sheet.setKey(VLabel.class, "font", VFont.create());
    }
}
