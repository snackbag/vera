package net.snackbag.vera.style;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;

public enum StyleValueType {
    STRING(""),
    INT(0),
    FLOAT(0.0F),

    COLOR(VColor.black()),
    FONT(VFont.create());

    public final Object standard;

    StyleValueType(Object standard) {
        this.standard = standard;
    }

    public static StyleValueType get(Object val) {
        if (val instanceof String) return STRING;
        else if (val instanceof Integer) return INT;
        else if (val instanceof Float) return FLOAT;
        else if (val instanceof VColor) return COLOR;
        else if (val instanceof VFont) return FONT;
        else throw new RuntimeException("%s isn't a valid style type".formatted(val.getClass().getName()));
    }
}
