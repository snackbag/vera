package net.snackbag.vera.style.animation.easing;

import net.snackbag.vera.Vera;
import net.snackbag.vera.style.animation.easing.sine.SineInEasing;
import net.snackbag.vera.style.animation.easing.sine.SineInOutEasing;
import net.snackbag.vera.style.animation.easing.sine.SineOutEasing;

public class VEasings {
    public static final ImmediateEasing IMMEDIATE = new ImmediateEasing();
    public static final LinearEasing LINEAR = new LinearEasing();

    public static final SineInEasing SIN_IN = new SineInEasing();
    public static final SineOutEasing SIN_OUT = new SineOutEasing();
    public static final SineInOutEasing SIN_IN_OUT = new SineInOutEasing();

    public static VEasing getDefault() {
        return LINEAR;
    }

    public static VEasing getIgnoreCase(String name) {
        return Vera.registrar.getEasingIgnoreCase(name);
    }
}
