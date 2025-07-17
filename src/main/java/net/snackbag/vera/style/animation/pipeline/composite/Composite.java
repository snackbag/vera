package net.snackbag.vera.style.animation.pipeline.composite;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.pipeline.VeraPipeline;

public abstract class Composite {
    protected VeraPipeline pipeline;

    public void generateUniforms() {}
    public abstract <T> T apply(AnimationEngine engine, String style, StyleValueType type, T in);

    protected boolean isOf(StyleValueType type, String style, ) {

    }
}
