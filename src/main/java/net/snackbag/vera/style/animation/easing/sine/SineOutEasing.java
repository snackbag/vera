package net.snackbag.vera.style.animation.easing.sine;

import net.minecraft.util.math.MathHelper;
import net.snackbag.vera.style.animation.easing.VEasing;

public class SineOutEasing extends VEasing {
    public SineOutEasing() {
        super("sin-out");
    }

    @Override
    public float apply(float from, float to, float delta) {
        return (float) (Math.sin((MathHelper.lerp(delta, from, to) * Math.PI) / 2));
    }
}
