package net.snackbag.vera.event;

import net.snackbag.vera.style.animation.VAnimation;

public interface VAnimationUnwindEvent {
    void run(VAnimation animation);
}
