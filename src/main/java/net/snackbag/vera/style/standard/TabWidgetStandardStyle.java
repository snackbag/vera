package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VTabWidget;

public class TabWidgetStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VTabWidget.class, "background-color", VColor.white());
        sheet.setKey(VTabWidget.class, "background-color-selected", VColor.white().sub(40));
        sheet.setKey(VTabWidget.class, "font", VFont.create());
        sheet.setKey(VTabWidget.class, "cursor", VCursorShape.POINTING_HAND, StyleState.HOVERED);

        sheet.setKey(VTabWidget.class, "item-spacing-left", 4);
        sheet.setKey(VTabWidget.class, "item-spacing-right", 4);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("font", StyleValueType.FONT);
        sheet.reserveType("background-color", StyleValueType.COLOR);
        sheet.reserveType("background-color-selected", StyleValueType.COLOR);

        sheet.reserveType("item-spacing-left", StyleValueType.INT);
        sheet.reserveType("item-spacing-right", StyleValueType.INT);
    }
}
