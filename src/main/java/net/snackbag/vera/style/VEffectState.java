package net.snackbag.vera.style;

import org.jetbrains.annotations.Nullable;

public enum VEffectState {
    DEFAULT("default"),
    HOVERED("hover", DEFAULT),

    CLICKED("clicked", HOVERED),
    LEFT_CLICKED("left-click", CLICKED),
    MIDDLE_CLICKED("middle-click", CLICKED),
    RIGHT_CLICKED("right-click", CLICKED),

    LC_DRAGGING("lc-drag", LEFT_CLICKED),
    MC_DRAGGING("mc-drag", MIDDLE_CLICKED),
    RC_DRAGGING("rc-drag", RIGHT_CLICKED);

    public final String identifier;
    public final @Nullable VEffectState fallback;

    VEffectState(String identifier) {
        this(identifier, null);
    }

    VEffectState(String identifier, @Nullable VEffectState fallback) {
        this.identifier = identifier;
        this.fallback = fallback;
    }

    public boolean inherits(VEffectState state) {
        VEffectState next = this;

        while (next != null) {
            if (next == state) return true;
            next = next.fallback;
        }

        return false;
    }

    public VStyleState asStyleState() {
        return new VStyleState(this, null);
    }
}
