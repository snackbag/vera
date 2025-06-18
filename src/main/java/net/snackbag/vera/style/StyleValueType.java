package net.snackbag.vera.style;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.Nullable;

public enum StyleValueType {
    STRING(""),
    IDENTIFIER(Identifier.of(MinecraftVera.MOD_ID, "empty")),
    INT(0),
    FLOAT(0.0F),

    COLOR(VColor.black()),
    FONT(VFont.create()),
    CURSOR(VCursorShape.DEFAULT);

    public final Object standard;

    StyleValueType(Object standard) {
        this.standard = standard;
    }

    public static StyleValueType get(Object val, @Nullable StyleValueType bias) {
        if (val instanceof String s) {
            if (bias == IDENTIFIER && s.matches("^[\\w-./]*:[\\w-./]*$")) return IDENTIFIER;
            else if (bias == CURSOR && EnumUtils.getEnumIgnoreCase(VCursorShape.class, s) != null) return CURSOR;
            return STRING;
        }

        else if (val instanceof Identifier) return IDENTIFIER;
        else if (val instanceof VCursorShape) return CURSOR;
        else if (val instanceof Integer) return INT;
        else if (val instanceof Float || val instanceof Double) return FLOAT;
        else if (val instanceof VColor) return COLOR;
        else if (val instanceof VFont) return FONT;
        else throw new RuntimeException("%s isn't a valid style type".formatted(val.getClass().getName()));
    }

    public static Object convert(Object value, StyleValueType to) {
        if (value instanceof String v) {
            if (to == IDENTIFIER) return new Identifier(v);
            else if (to == CURSOR) return EnumUtils.getEnumIgnoreCase(VCursorShape.class, v);
        }

        else if (to == FLOAT && value instanceof Double v) return v.floatValue();
        return value;
    }
}
