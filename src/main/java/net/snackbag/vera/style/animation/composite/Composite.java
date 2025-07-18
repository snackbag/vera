package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.VeraPipeline;
import net.snackbag.vera.util.Once;

public abstract class Composite {
    public Once<VeraPipeline> pipeline;
    public long frameTime = 0;

    public void generateUniforms() {}
    public abstract <T> T apply(AnimationEngine engine, String style, StyleValueType type, T in);
}
