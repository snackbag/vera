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
        int relative = Math.toIntExact(System.currentTimeMillis() - startTime);

        return switch (animation.loopMode) {
            case NONE -> relative;
            case FORWARD_REPEAT -> relative % animation.duration;
        };
    }

    public float getProgress() {
        return Math.min((float) getRelativeTime() / animation.duration, 1.0f);
    }

    public int getCurrentLoopNumber() {
        return (int) Math.ceil((float) (System.currentTimeMillis() - startTime) / animation.duration);
    }
}
