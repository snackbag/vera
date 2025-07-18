package net.snackbag.vera.style.animation.easing;

public class VLinearEasing extends VEasing {
    protected VLinearEasing() {}

    @Override
    public float apply(float from, float to, float delta) {
        return from + delta * (to - from);
    }
}
