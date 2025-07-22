package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VeraPipeline;
import net.snackbag.vera.util.Once;
import net.snackbag.vera.widget.VWidget;

public abstract class Composite {
    public Once<VeraPipeline> pipeline = new Once<>();
    public long frameTime = 0;

    /**
     * This method is called per-frame and is entirely independent of the given style. Hence, the name uniform.
     */
    public void generateUniforms() {}

    public void applyWidget(VWidget<?> widget) {}

    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        return in;
    }

    public record Context<T>(AnimationEngine engine, String style, StyleValueType type, T original) {
    }
}
