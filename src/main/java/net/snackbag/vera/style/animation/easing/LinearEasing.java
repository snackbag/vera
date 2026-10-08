package net.snackbag.vera.style.animation.easing;

public class LinearEasing extends VEasing {
    protected LinearEasing() {
        super("linear");
    }

    @Override
    public float apply(float from, float to, float delta) {
        return from + delta * (to - from);
    }
}
