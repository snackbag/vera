package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VTabWidget;

public class TabWidgetStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VTabWidget.Tab.class, "font", VFont.create());
        sheet.setKey(VTabWidget.Tab.class, "font-selected", VFont.create());
        sheet.setKey(VTabWidget.Tab.class, "background", VColor.white());
        sheet.setKey(VTabWidget.Tab.class, "background-selected", VColor.white().sub(40));
        sheet.setKey(VTabWidget.Tab.class, "cursor", VCursorShape.POINTING_HAND);
        sheet.setKey(VTabWidget.Tab.class, "padding", new V4Int(0, 4));
        sheet.setKey(VTabWidget.Tab.class, "padding-selected", new V4Int(0, 4));
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("font", StyleValueType.FONT);
        sheet.reserveType("font-selected", StyleValueType.FONT);
        sheet.reserveType("background", StyleValueType.FILL);
        sheet.reserveType("background-selected", StyleValueType.FILL);
        sheet.reserveType("cursor", StyleValueType.CURSOR);
        sheet.reserveType("cursor-selected", StyleValueType.CURSOR);
        sheet.reserveType("padding", StyleValueType.V4INT);
        sheet.reserveType("padding-selected", StyleValueType.V4INT);
    }
}
