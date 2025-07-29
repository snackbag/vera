package net.snackbag.vera.flag;

public enum VWindowPositioningFlag {
    /**
     * Deepest render position, renders even under the vignette
     */
    BELOW_VIGNETTE,

    /**
     * Renders below everything, also under the spyglass
     */
    BELOW_OVERLAYS,

    /**
     * Renders under the HUD
     */
    BELOW_HUD,

    /**
     * Renders on the same level as the HUD
     */
    HUD,

    /**
     * Renders over the HUD and under normal GUI
     */
    ABOVE_HUD,

    /**
     * Renders on the same layer as the GUI
     * <p>
     * <strong>Watch out:</strong> if the UI doesn't require a mouse to exist, it will not render if no other app that
     * does require a mouse is present. In case you 100% need it, it is recommended to either use {@link #ABOVE_HUD} or
     * {@link #ABOVE_GUI}
     */
    GUI,

    /**
     * Renders over the GUI but under any other render events
     */
    ABOVE_GUI,

    /**
     * (default)<br>
     * Renders over all other render events
     */
    SCREEN,

    /**
     * Should only be used when wanting to 100% override something
     */
    TOP
}
