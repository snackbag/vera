package net.snackbag.vera.style.animation;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.composite.Composite;

import java.util.ArrayList;
import java.util.List;

public class VeraPipeline {
    public final VeraApp app;
    private final List<Composite> passes = new ArrayList<>();

    public VeraPipeline(VeraApp app) {
        this.app = app;
    }

    public void addPass(Composite pass) {
        setupPass(pass);
        this.passes.add(pass);
    }

    public void addPass(int index, Composite pass) {
        setupPass(pass);
        this.passes.add(index, pass);
    }

    private void setupPass(Composite pass) {
        boolean unique = pass.pipeline.setSafe(this);
        if (!unique) throw new UnsupportedOperationException("Cannot setup composite pass if composite is already bound to pipeline");
    }

    public <T> T applyComposites(AnimationEngine engine, String style, StyleValueType type, T in) {
        Composite.Context<T> ctx = new Composite.Context<T>(engine,style, type, in);

        for (Composite pass : passes) {
            if (pass.frameTime != Vera.renderCacheId) {
                pass.frameTime = Vera.renderCacheId;
                pass.generateUniforms();
            }

            in = pass.apply(ctx);
        }

        return in;
    }
}
