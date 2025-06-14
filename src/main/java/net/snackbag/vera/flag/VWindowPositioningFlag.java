package net.snackbag.vera.flag;

public enum VWindowPositioningFlag {
    /**
     * Renders below everything, also under the spyglass
     */
    BELOW_ALL,

    /**
     * Renders under the HUD
     */
    UNDER_HUD,

    /**
     * Renders on the same level as the HUD
     */
    HUD,

    /**
     * Renders over the HUD and under normal GUI
     */
    HUD_OVERLAY,

    /**
     * Renders on the same layer as the GUI
     */
    GUI,

    /**
     * Renders over the GUI but under any other render events
     */
    GUI_OVERLAY,

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
