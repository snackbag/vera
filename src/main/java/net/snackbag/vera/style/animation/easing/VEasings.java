package net.snackbag.vera.style.animation.easing;

import net.snackbag.vera.Vera;

public class VEasings {
    public static final VLinearEasing LINEAR = new VLinearEasing();

    public static VEasing getIgnoreCase(String name) {
        return Vera.registrar.getEasingIgnoreCase(name);
    }
}
