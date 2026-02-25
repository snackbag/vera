package net.snackbag.vera.util;

import net.snackbag.mcvera.impl.MCVeraRenderer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class VRenderContext {
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final float rotation;
    public final float scale;
    public final boolean hasTransparency;

    public VRenderContext(
            int x, int y,
            int width, int height,
            float rotation, float scale,
            boolean hasTransparency
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.scale = scale;
        this.hasTransparency = hasTransparency;
    }

    private final ArrayList<ClipInstance> clipStack = new ArrayList<>();

    public void withClip(int x, int y, int width, int height, Runnable exec) {
        pushClip(x, y, width, height);
        exec.run();
        popClip();
    }

    public void pushClip(int x, int y, int width, int height) {
        clipStack.add(new ClipInstance(x, y, width, height));
        MCVeraRenderer.drawContext.enableScissor(
                this.x + x, this.y + y,
                this.x + x + width, this.y + y + height
        );
    }

    public @Nullable ClipInstance peekClip() {
        if (clipStack.isEmpty()) return null;
        else return clipStack.get(clipStack.size() - 1);
    }

    public void popClip() {
        if (clipStack.isEmpty()) {
            throw new IndexOutOfBoundsException("Cannot pop clip from VRenderContext, because there is nothing in the stack.");
        }

        clipStack.remove(clipStack.size() - 1);
        MCVeraRenderer.drawContext.disableScissor();
    }

    public void resetClips() {
        for (ClipInstance ignored : clipStack) MCVeraRenderer.drawContext.disableScissor();
        clipStack.clear();
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
