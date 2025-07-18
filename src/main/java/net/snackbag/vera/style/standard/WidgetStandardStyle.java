package net.snackbag.vera.style.standard;

import net.snackbag.vera.core.V4Color;
import net.snackbag.vera.core.V4Int;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VWidget;

public class WidgetStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VWidget.class, "overlay", VColor.transparent());
        sheet.setKey(VWidget.class, "cursor", VCursorShape.DEFAULT);

        // Border
        sheet.setKey(VWidget.class, "border-color", new V4Color(VColor.black()));
        sheet.setKey(VWidget.class, "border-size", new V4Int(0));
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("overlay", StyleValueType.COLOR);
        sheet.reserveType("cursor", StyleValueType.CURSOR);
        sheet.reserveType("border-color", StyleValueType.V4COLOR);
        sheet.reserveType("border-size", StyleValueType.V4INT);

        // TODO: add background-color
        // TODO: add padding
    }
}
