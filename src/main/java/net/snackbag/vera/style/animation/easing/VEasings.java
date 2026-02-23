package net.snackbag.vera.style.animation.easing;

import net.snackbag.vera.Vera;

public class VEasings {
    public static final ImmediateEasing IMMEDIATE = new ImmediateEasing();
    public static final LinearEasing LINEAR = new LinearEasing();

    public static final SineEasing.In SINE_IN = new SineEasing.In();
    public static final SineEasing.Out SINE_OUT = new SineEasing.Out();
    public static final SineEasing.InOut SINE_IN_OUT = new SineEasing.InOut();

    public static VEasing getDefault() {
        return LINEAR;
    }

    public static VEasing getIgnoreCase(String name) {
        return Vera.registrar.getEasingIgnoreCase(name);
    }
}
