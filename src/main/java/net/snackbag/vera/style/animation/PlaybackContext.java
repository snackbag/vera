package net.snackbag.vera.style.animation;

// TODO: winding
public class PlaybackContext {
    public final CompiledAnimation animation;
    public final long startTime;

    public PlaybackContext(CompiledAnimation animation, long startTime) {
        this.animation = animation;
        this.startTime = startTime;
    }

    public int getRelativeTime() {
        return Math.toIntExact(System.currentTimeMillis() - startTime) % animation.duration; // TODO: loop modes
    }

    public float getProgress() {
        return (float) getRelativeTime() / animation.duration;
    }
}
