package net.snackbag.vera.style.animation.easing;

public class ImmediateEasing extends VEasing {
    public ImmediateEasing() {
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
