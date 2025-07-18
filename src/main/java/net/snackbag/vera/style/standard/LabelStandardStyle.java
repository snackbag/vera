package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.V4Int;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VLabel;

public class LabelStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VLabel.class, "background-color", VColor.transparent());
        sheet.setKey(VLabel.class, "font", VFont.create());
        sheet.setKey(VLabel.class, "padding", new V4Int(4));
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("background-color", StyleValueType.COLOR);
        sheet.reserveType("font", StyleValueType.FONT);
        sheet.reserveType("padding", StyleValueType.V4INT);
    }
}
