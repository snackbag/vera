package net.snackbag.vera.event;

import net.snackbag.vera.style.animation.VAnimation;

public interface VAnimationFinishEvent {
    void run(VAnimation animation, long beginTime);
}
