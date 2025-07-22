package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VRect;

public class RectStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VRect.class, "background-color", VColor.black());
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("background-color", StyleValueType.COLOR);
    }
}
