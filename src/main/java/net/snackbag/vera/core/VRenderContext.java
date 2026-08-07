package net.snackbag.vera.core;

import net.snackbag.mcvera.impl.MCVeraRenderer;
import net.snackbag.vera.Vera;
import net.snackbag.vera.util.VGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class VRenderContext {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final float rotation;
    public final float scale;
    public final boolean hasTransparency;

    public final VRenderContext parent;
    private final Deque<VGeometry.IntRect> clipStack = new ArrayDeque<>();

    public VRenderContext(
            int x, int y,
            int width, int height,
            float rotation, float scale,
            boolean hasTransparency
    ) {
        this(null, x, y, width, height, rotation, scale, hasTransparency);
    }

    public VRenderContext(
            @Nullable VRenderContext parent,
            int x, int y,
            int width, int height,
            float rotation, float scale,
            boolean hasTransparency
    ) {
        this.parent = parent;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.scale = scale;
        this.hasTransparency = hasTransparency;
    }

    public VRenderContext makeChild(int x, int y, int width, int height) {
        return new VRenderContext(this, x, y, width, height, rotation, scale, hasTransparency);
    }

    public void withClip(int x, int y, int width, int height, Runnable exec) {
        pushClip(x, y, width, height);
        try {
            exec.run();
        } finally {
            popClip();
        }
    }

    public void pushClip(int x, int y, int width, int height) {
        clipStack.push(new VGeometry.IntRect(
                this.x + x, this.y + y,
                this.x + x + width, this.y + y + height
        ));
        MCVeraRenderer.drawContext.enableScissor(
                this.x + x, this.y + y,
                this.x + x + width, this.y + y + height
        );
    }

    public @Nullable VGeometry.IntRect peekClip() {
        return clipStack.peek();
    }

    public void popClip() {
        if (clipStack.isEmpty()) {
            throw new IndexOutOfBoundsException("Cannot pop clip from VRenderContext, because clip stack is empty.");
        }

        clipStack.pop();
        MCVeraRenderer.drawContext.disableScissor();
    }

    public void resetClips() {
        for (VGeometry.IntRect ignored : clipStack) MCVeraRenderer.drawContext.disableScissor();
        clipStack.clear();
    }

    public boolean isVisible(int x, int y, int width, int height) {
        return isVisible(new VGeometry.IntRect(
                this.x + x, this.y + y,
                this.width + x + width, this.height + y + height
        ));
    }

    public boolean isVisible(VGeometry.IntRect rect) {
        if (rect.isEmpty()) return false;

        VGeometry.IntRect screen = new VGeometry.IntRect(0, 0, Vera.getScreenWidth(), Vera.getScreenHeight());
        if (!rect.intersects(screen)) return false;

        if (parent != null && !parent.isVisible(rect)) return false;

        VGeometry.IntRect clip = peekClip();
        return clip == null || rect.intersects(clip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, rotation, scale, hasTransparency);
    }

    @Override
    public String toString() {
        return "VRenderContext[" +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "width=" + width + ", " +
                "height=" + height + ", " +
                "rotation=" + rotation + ", " +
                "scale=" + scale + ", " +
                "hasTransparency=" + hasTransparency + ']';
    }


    public record ClipInstance(int x, int y, int width, int height) {
    }
}
