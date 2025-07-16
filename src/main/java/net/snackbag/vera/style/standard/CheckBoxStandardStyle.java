package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VCheckBox;

public class CheckBoxStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VCheckBox.class, "overlay", VColor.transparent());
        sheet.setKey(VCheckBox.class, "cursor", VCursorShape.POINTING_HAND, StyleState.HOVERED);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("overlay", StyleValueType.COLOR);
        sheet.reserveType("cursor", StyleValueType.CURSOR);
        sheet.reserveType("src", StyleValueType.IDENTIFIER);
        sheet.reserveType("src-checked", StyleValueType.IDENTIFIER);
    }
}
