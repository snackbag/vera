package net.snackbag.vera.style.animation.easing;

import net.snackbag.vera.Vera;

public class VEasings {
    public static final ImmediateEasing IMMEDIATE = new ImmediateEasing();
    public static final LinearEasing LINEAR = new LinearEasing();

    public static VEasing getDefault() {
        return LINEAR;
    }

    public static VEasing getIgnoreCase(String name) {
        return Vera.registrar.getEasingIgnoreCase(name);
    }
}
