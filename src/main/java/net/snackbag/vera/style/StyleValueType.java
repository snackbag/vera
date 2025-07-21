package net.snackbag.vera.style;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.core.v4.V4Color;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.style.animation.easing.VEasing;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.Nullable;

public enum StyleValueType {
    STRING("", (f, t, e, d) -> d > 0.5 ? t : f),
    IDENTIFIER(Identifier.of(MinecraftVera.MOD_ID, "empty"), (f, t, e, d) -> d > 0.5 ? t : f),
    INT(0, (from, to, easing, delta) -> easing.apply(from, to, delta)),
    FLOAT(0.0F, (from, to, easing, delta) -> easing.apply(from, to, delta)),

    COLOR(VColor.black(), (from, to, easing, delta) -> from.ease(easing, to, delta)),
    FONT(VFont.create(), (from, to, easing, delta) ->
            VFont.create().withColor(from.getColor().ease(easing, to.getColor(), delta))
                    .withSize(easing.apply(from.getSize(), to.getSize(), delta))
                    .withName(delta > 0.5 ? to.getName() : from.getName())),
    CURSOR(VCursorShape.DEFAULT, (f, t, e, d) -> d > 0.5 ? t : f),

    V4INT(new V4Int(0), (from, to, easing, delta) -> new V4Int(
            easing.apply(from.get1(), to.get1(), delta),
            easing.apply(from.get2(), to.get2(), delta),
            easing.apply(from.get3(), to.get3(), delta),
            easing.apply(from.get4(), to.get4(), delta)
    )),
    V4COLOR(new V4Color(VColor.black()), (from, to, easing, delta) -> new V4Color(
            from.get1().ease(easing, to.get1(), delta),
            from.get2().ease(easing, to.get2(), delta),
            from.get3().ease(easing, to.get3(), delta),
            from.get4().ease(easing, to.get4(), delta)
    ));

    public final Object standard;
    public final EaseContext<Object> animationTransition;

    <T> StyleValueType(T standard, EaseContext<T> animationTransition) {
        this.standard = standard;
        this.animationTransition = (EaseContext<Object>) animationTransition;
    }

    public static StyleValueType get(Object val, @Nullable StyleValueType bias) {
        if (val instanceof String s) {
            if (bias == IDENTIFIER && s.matches("^[\\w-./]*:[\\w-./]*$")) return IDENTIFIER;
            else if (bias == CURSOR && EnumUtils.getEnumIgnoreCase(VCursorShape.class, s) != null) return CURSOR;
            return STRING;
        }

        else if (val instanceof V4Color || (bias == V4COLOR && (val instanceof VColor[] || val instanceof VColor))) return V4COLOR;
        else if (val instanceof V4Int || (bias == V4INT && (val instanceof int[] || val instanceof Integer[] || val instanceof Integer))) return V4INT;
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

        else if (value instanceof int[] || value instanceof Integer[]) {
            Integer[] v = (Integer[]) value;

            return switch (v.length) {
                case 1 -> new V4Int(v[0]);
                case 2 -> new V4Int(v[0], v[1]);
                case 4 -> new V4Int(v[0], v[1], v[2], v[3]);
                default -> throw new RuntimeException("invalid V4Int format. Length must be 1, 2 or 4. Provided: %d".formatted(v.length));
            };
        }

        else if (value instanceof Integer v && to == V4INT) return new V4Int(v);

        else if (value instanceof VColor[] v) {
            return switch (v.length) {
                case 1 -> new V4Color(v[0]);
                case 2 -> new V4Color(v[0], v[1]);
                case 4 -> new V4Color(v[0], v[1], v[2], v[3]);
                default -> throw new RuntimeException("invalid V4Color format. Length must be 1, 2 or 4. Provided: %d".formatted(v.length));
            };
        }

        else if (value instanceof VColor v && to == V4COLOR) return new V4Color(v);

        else if (to == FLOAT && value instanceof Double v) return v.floatValue();
        return value;
    }

    @FunctionalInterface
    public interface EaseContext<T> {
        T apply(T in, T out, VEasing easing, float delta);
    }
}
