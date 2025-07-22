package net.snackbag.vera.style.animation.easing;

public abstract class VEasing {
    public abstract float apply(float from, float to, float delta);
    public abstract int apply(int from, int to, float delta);
}
