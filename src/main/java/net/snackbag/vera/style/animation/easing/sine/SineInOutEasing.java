package net.snackbag.vera.style.animation.easing.sine;

import net.minecraft.util.math.MathHelper;
import net.snackbag.vera.style.animation.easing.VEasing;

public class SineInOutEasing extends VEasing {
    public SineInOutEasing() {
        super("sin-in-out");
    }

    @Override
    public float apply(float from, float to, float delta) {
        return (float) (-(Math.cos(Math.PI * MathHelper.lerp(delta, from, to)) - 1) / 2);
    }
}
