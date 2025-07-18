package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.V4Int;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VDropdown;

public class DropdownStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VDropdown.class, "background-color", VColor.white());
        sheet.setKey(VDropdown.class, "cursor", VCursorShape.POINTING_HAND, StyleState.HOVERED);
        sheet.setKey(VDropdown.class, "font", VFont.create());
        sheet.setKey(VDropdown.class, "padding", new V4Int(5, 10));
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("background-color", StyleValueType.COLOR);
        sheet.reserveType("font", StyleValueType.FONT);
        sheet.reserveType("padding", StyleValueType.V4INT);
    }
}
