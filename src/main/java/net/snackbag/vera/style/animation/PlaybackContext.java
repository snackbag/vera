package net.snackbag.vera.style.animation;

import net.minecraft.util.math.MathHelper;

public class PlaybackContext {
    public final CompiledAnimation animation;
    public final long startTime;

    private float prevWindingProgress = 0f;
    private long windingStartTime = -1;
    private int unwindStartRelativeTime = -1; // for the relative unwinding
    private boolean unwindingOrRewinding = false; // unwinding = false; rewinding = true

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

    /**
     * 0 = animation has full impact, 1 = animation has no impact
     * @return the current winding progress from 0 to 1
     */
    public float getWindingProgress() {
        if (windingStartTime == -1) return 0f;

        int unwindTime = animation.unwindTime;
        if (unwindTime == -1) unwindTime = unwindStartRelativeTime;

        float delta = (float) (System.currentTimeMillis() - windingStartTime) / unwindTime;
        if (unwindingOrRewinding) return MathHelper.clamp(prevWindingProgress - delta, 0f, 1f); // is rewinding
        else return MathHelper.clamp(prevWindingProgress + delta, 0f, 1f); // is unwinding
    }

    public void unwind() {
        if (!unwindingOrRewinding && windingStartTime != -1) return; // if already unwinding
        if (windingStartTime == -1) unwindStartRelativeTime = getRelativeTime();

        prevWindingProgress = getWindingProgress();
        windingStartTime = System.currentTimeMillis() - 1;
        unwindingOrRewinding = false;
    }

    public void rewind() {
        if (windingStartTime == -1 || unwindingOrRewinding) return; // if not unwinding OR if already rewinding

        prevWindingProgress = getWindingProgress();
        windingStartTime = System.currentTimeMillis();
        unwindingOrRewinding = true;
    }

    public void potentiallyResetWinding() {
        if (getWindingProgress() > 0.0f) return;

        prevWindingProgress = 0f;
        windingStartTime = -1;
        unwindStartRelativeTime = -1;
        unwindingOrRewinding = false;
    }
}
