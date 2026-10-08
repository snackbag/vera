package net.snackbag.vera.style.standard;

import net.snackbag.vera.InternalVera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VImage;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VScrollBox;

public class ScrollBoxStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VScrollBox.class, "border-size", 1);
        sheet.setKey(VScrollBox.class, "background", VColor.white());

        sheet.setKey(VScrollBox.Bar.class, "scroll-increment", new VImage(InternalVera.id("widgets/scrollbox/increment.png")));
        sheet.setKey(VScrollBox.Bar.class, "scroll-thumb", new VImage(InternalVera.id("widgets/scrollbox/thumb.png")));
        sheet.setKey(VScrollBox.Bar.class, "scroll-track", VColor.MC_GRAY);
        sheet.setKey(VScrollBox.Bar.class, "scroll-decrement", new VImage(InternalVera.id("widgets/scrollbox/decrement.png")));
        sheet.setKey(VScrollBox.Bar.class, "scroll-width", 6);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("background", StyleValueType.FILL);

        sheet.reserveType("scroll-increment", StyleValueType.FILL);
        sheet.reserveType("scroll-thumb", StyleValueType.FILL);
        sheet.reserveType("scroll-track", StyleValueType.FILL);
        sheet.reserveType("scroll-decrement", StyleValueType.FILL);
        sheet.reserveType("scroll-width", StyleValueType.INT);
    }
}
