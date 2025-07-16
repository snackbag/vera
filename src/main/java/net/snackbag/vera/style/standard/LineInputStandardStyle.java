package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VLineInput;

public class LineInputStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VLineInput.class, "select-color", VColor.of(0, 120, 215, 0.2f));
        sheet.setKey(VLineInput.class, "background-color", VColor.transparent());
        sheet.setKey(VLineInput.class, "cursor", VCursorShape.TEXT, StyleState.HOVERED);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("select-color", StyleValueType.COLOR);
        sheet.reserveType("background-color", StyleValueType.COLOR);
        sheet.reserveType("cursor", StyleValueType.CURSOR);
    }
}
