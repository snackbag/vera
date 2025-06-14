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
