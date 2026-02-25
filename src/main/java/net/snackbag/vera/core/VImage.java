package net.snackbag.vera.core;

import net.minecraft.util.Identifier;
import net.snackbag.vera.Vera;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.animation.easing.VEasing;

public class VImage implements VFill {
    public final Identifier src;
    public final VColor tint;

    public VImage(String src, VColor tint) {
        this(new Identifier(src), tint);
    }

    public VImage(String src) {
        this(new Identifier(src));
    }

    public VImage(Identifier src, VColor tint) {
        this.src = src;
        this.tint = tint;
    }

    public VImage(Identifier src) {
        this(src, VColor.white());
    }

    @Override
    public void renderQuad(VRenderContext ctx, int x, int y, int width, int height) {
        Vera.renderer.drawImage(ctx, x, y, width, height, src);
    }

    @Override
    public VFill ease(VEasing easing, VFill targetRaw, float delta) {
        if (!(targetRaw instanceof VImage target)) {
            throw new ClassCastException("Cannot ease two different types of VFill to image.");
        }

        return new VImage(
                (Identifier) StyleValueType.IDENTIFIER.animationTransition.apply(src, target.src, easing, delta),
                (VColor) tint.ease(easing, target.tint, delta)
        );
    }
}
