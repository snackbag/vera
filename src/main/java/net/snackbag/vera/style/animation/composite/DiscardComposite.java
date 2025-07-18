package net.snackbag.vera.style.animation.composite;

import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.AnimationEngine;

public class DiscardComposite extends Composite {
    @Override
    public <T> T apply(AnimationEngine engine, String style, StyleValueType type, T in) {
        return in;
    }
}
