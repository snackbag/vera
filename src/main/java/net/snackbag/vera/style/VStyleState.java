package net.snackbag.vera.style;

import org.jetbrains.annotations.Nullable;

public enum VStyleState {
    DEFAULT("default"),
    HOVERED("hover", DEFAULT),

    CLICKED("clicked", HOVERED),
    LEFT_CLICKED("left-click", CLICKED),
    MIDDLE_CLICKED("middle-click", CLICKED),
    RIGHT_CLICKED("right-click", CLICKED),

    LC_DRAGGING("lc-drag", LEFT_CLICKED),
    LC_DRAG_TOP("lc-drag-top", LC_DRAGGING),
    LC_DRAG_BOTTOM("lc-drag-bottom", LC_DRAGGING),
    LC_DRAG_LEFT("lc-drag-left", LC_DRAGGING),
    LC_DRAG_RIGHT("lc-drag-right", LC_DRAGGING),

    MC_DRAGGING("mc-drag", MIDDLE_CLICKED),
    MC_DRAG_TOP("mc-drag-top", MC_DRAGGING),
    MC_DRAG_BOTTOM("mc-drag-bottom", MC_DRAGGING),
    MC_DRAG_LEFT("mc-drag-left", MC_DRAGGING),
    MC_DRAG_RIGHT("mc-drag-right", MC_DRAGGING),

    RC_DRAGGING("rc-drag", RIGHT_CLICKED),
    RC_DRAG_TOP("rc-drag-top", RC_DRAGGING),
    RC_DRAG_BOTTOM("rc-drag-bottom", RC_DRAGGING),
    RC_DRAG_LEFT("rc-drag-left", RC_DRAGGING),
    RC_DRAG_RIGHT("rc-drag-right", RC_DRAGGING);

    public final String identifier;
    public final @Nullable VStyleState fallback;

    VStyleState(String identifier) {
        this(identifier, null);
    }

    VStyleState(String identifier, @Nullable VStyleState fallback) {
        this.identifier = identifier;
        this.fallback = fallback;
    }

    public boolean inherits(VStyleState state) {
        VStyleState next = this;

        while (next != null) {
            if (next == state) return true;
            next = next.fallback;
        }

        return false;
    }
}
