package net.snackbag.vera.style.animation.easing;

import net.snackbag.vera.Vera;

public abstract class VEasing {
    public VEasing(String name) {
        if (Vera.registrar.getEasingIgnoreCase(name) != null) return;
        Vera.registrar.registerEasing(name, this);
    }

    public abstract float apply(float from, float to, float delta);
    public abstract int apply(int from, int to, float delta);
}
