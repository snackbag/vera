package net.snackbag.vera.core;

import net.snackbag.vera.style.animation.easing.VEasing;

public interface VFill {
    static VFill empty() {
        return new VFill() {
            @Override
            public void renderQuad(VRenderContext ctx, int x, int y, int width, int height) {}

            @Override
            public VFill ease(VEasing easing, VFill target, float delta) {
                return target;
            }

            @Override
            public boolean isVisible() {
                return true;
            }
        };
    }

    void renderQuad(VRenderContext ctx, int x, int y, int width, int height);
    VFill ease(VEasing easing, VFill target, float delta);
    boolean isVisible();
}
