package net.snackbag.vera.style;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.core.*;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.Nullable;

public enum StyleValueType {
    STRING(""),
    IDENTIFIER(Identifier.of(MinecraftVera.MOD_ID, "empty")),
    INT(0),
    FLOAT(0.0F),

    COLOR(VColor.black()),
    FONT(VFont.create()),
    CURSOR(VCursorShape.DEFAULT),

    V4INT(new V4Int(0)),
    V4COLOR(new V4Color(VColor.black()));

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

        else if (val instanceof V4Color || val instanceof VColor[]) return V4COLOR;
        else if (val instanceof V4Int || val instanceof int[]) return V4INT;
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

        else if (value instanceof int[] v) {
            return switch (v.length) {
                case 1 -> new V4Int(v[0]);
                case 2 -> new V4Int(v[0], v[1]);
                case 4 -> new V4Int(v[0], v[1], v[2], v[3]);
                default -> throw new RuntimeException("invalid V4Int format. Length must be 1, 2 or 4. Provided: %d".formatted(v.length));
            };
        }

        else if (value instanceof VColor[] v) {
            return switch (v.length) {
                case 1 -> new V4Color(v[0]);
                case 2 -> new V4Color(v[0], v[1]);
                case 4 -> new V4Color(v[0], v[1], v[2], v[3]);
                default -> throw new RuntimeException("invalid V4Color format. Length must be 1, 2 or 4. Provided: %d".formatted(v.length));
            };
        }

        else if (to == FLOAT && value instanceof Double v) return v.floatValue();
        return value;
    }
}
