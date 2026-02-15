package net.snackbag.vera.style.animation.easing;

public class VImmediateEasing extends VEasing {
    public VImmediateEasing() {
        super("immediate");
    }

    @Override
    public float apply(float from, float to, float delta) {
        return to;
    }

    @Override
    public int apply(int from, int to, float delta) {
        return to;
    }
}
