package net.snackbag.vera.style.animation.pipeline;

import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.AnimationEngine;
import net.snackbag.vera.style.animation.pipeline.composite.Composite;

import java.util.ArrayList;
import java.util.List;

public class VeraPipeline {
    public final VeraApp app;
    private final List<Composite> passes = new ArrayList<>();

    public VeraPipeline(VeraApp app) {
        this.app = app;
    }

    public <T> T applyCompositions(AnimationEngine engine, String style, StyleValueType type, T in) {
        for (Composite pass : passes) {
            in = pass.apply(engine, style, type, in);
        }

        return in;
    }
}
