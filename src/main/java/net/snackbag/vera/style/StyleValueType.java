package net.snackbag.vera.style;

import net.snackbag.vera.core.VColor;

public enum StyleValueType {
    COLOR,
    STRING,
    INT,
    FLOAT;

    StyleValueType() {}

    public static StyleValueType get(Object val) {
        if (val instanceof VColor) return COLOR;
        else if (val instanceof String) return STRING;
        else if (val instanceof Integer) return INT;
        else if (val instanceof Float) return FLOAT;
        else throw new RuntimeException("%s isn't a valid style type".formatted(val.getClass().getName()));
    }
}
