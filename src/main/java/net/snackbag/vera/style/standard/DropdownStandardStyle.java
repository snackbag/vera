package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VDropdown;

public class DropdownStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VDropdown.class, "background-color", VColor.white());
        sheet.setKey(VDropdown.class, "cursor", VCursorShape.POINTING_HAND, StyleState.HOVERED);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("background-color", StyleValueType.COLOR);
        sheet.reserveType("cursor", StyleValueType.CURSOR);
    }
}
