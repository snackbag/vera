package net.snackbag.vera.style.animation.easing.sine;

import net.minecraft.util.math.MathHelper;
import net.snackbag.vera.style.animation.easing.VEasing;

public class SineInEasing extends VEasing {
    public SineInEasing() {
        super("sin-in");
    }

    @Override
    public float apply(float from, float to, float delta) {
        return (float) (1 - Math.cos((MathHelper.lerp(delta, from, to) * Math.PI) / 2));
    }
}
