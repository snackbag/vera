package net.snackbag.vera.style.animation.easing;

import net.minecraft.util.math.MathHelper;

public class SineEasing {
    public static class In extends VEasing {
        public In() {
            super("sine-in");
        }

        @Override
        public float apply(float from, float to, float delta) {
            return (float) (1 - Math.cos((MathHelper.lerp(delta, from, to) * Math.PI) / 2));
        }
    }

    public static class Out extends VEasing {
        public Out() {
            super("sine-out");
        }

        @Override
        public float apply(float from, float to, float delta) {
            return (float) (Math.sin((MathHelper.lerp(delta, from, to) * Math.PI) / 2));
        }
    }

    public static class InOut extends VEasing {
        public InOut() {
            super("sine-in-out");
        }

        @Override
        public float apply(float from, float to, float delta) {
            return (float) (-(Math.cos(Math.PI * MathHelper.lerp(delta, from, to)) - 1) / 2);
        }
    }
}
