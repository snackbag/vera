package net.snackbag.vera.style.standard;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;

public class ImageStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("src", StyleValueType.IDENTIFIER);
    }
}
