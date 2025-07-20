package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VeraPipeline;
import net.snackbag.vera.util.Once;

public abstract class Composite {
    public Once<VeraPipeline> pipeline;
    public long frameTime = 0;

    /**
     * This method is called per-frame and is entirely independent of the given style. Hence, the name uniform.
     */
    public void generateUniforms() {}
    public abstract <T> T apply(Context<T> ctx, T in, boolean isNewFrame);

    public record Context<T>(AnimationEngine engine, String style, StyleValueType type, T original) {
    }
}
