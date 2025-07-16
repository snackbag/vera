package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VTabWidget;

public class TabWidgetStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VTabWidget.class, "font", VFont.create());
        sheet.setKey(VTabWidget.class, "cursor", VCursorShape.POINTING_HAND, StyleState.HOVERED);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("font", StyleValueType.FONT);
    }
}
