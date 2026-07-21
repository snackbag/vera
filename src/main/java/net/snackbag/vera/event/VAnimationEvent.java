package net.snackbag.vera.event;

import net.snackbag.vera.style.animation.CompiledAnimation;
import net.snackbag.vera.style.animation.VAnimation;

public class VAnimationEvent {
    public record Begin(VAnimation animation) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Animation.BEGIN;
        }
    }

    public record Finish(CompiledAnimation animation, long beginTime) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.Animation.FINISH;
        }
    }
}
