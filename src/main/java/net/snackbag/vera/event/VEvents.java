package net.snackbag.vera.event;

// Sorted by :sparkle: the feeling that it looks nice :sparkle:
public class VEvents {
    // Animation
    public static class Animation {
        public static final String BEGIN = "animation-begin";
        public static final String FINISH = "animation-finish";
    }

    // Element
    public static class Element {
        public static final String MESSAGE = "elem-message";
        public static final String LAYOUT_SWAP = "elem-layout-swap";
        public static final String LAYOUT_REMOVE = "elem-layout-remove";
    }

    // Widget
    public static class Widget {
        public static final String HOVER = "hover";
        public static final String HOVER_LEAVE = "hover-leave";

        public static final String LEFT_CLICK = "left-click";
        public static final String LEFT_CLICK_RELEASE = "left-click-release";
        public static final String MIDDLE_CLICK = "middle-click";
        public static final String MIDDLE_CLICK_RELEASE = "middle-click-release";
        public static final String RIGHT_CLICK = "right-click";
        public static final String RIGHT_CLICK_RELEASE = "right-click-release";

        public static final String SCROLL = "mouse-scroll";
        public static final String MOUSE_MOVE = "mouse-move";
        public static final String MOUSE_DRAG = "mouse-drag";

        public static final String TRANSPARENCY_STATE_CHANGED = "transparency-state-change";

        public static final String FOCUS_STATE_CHANGE = "focus-state-change";
        public static final String FILES_DROPPED = "files-dropped";
    }

    // Checkbox
    public static class CheckBox {
        public static final String CHECK_STATE_CHANGED = "vcheckbox-check-state-changed";
    }

    // ComboBox
    public static class ComboBox {
        public static final String ITEM_TEXT_CHANGED = "vcombobox-item-text-changed";
        public static final String SELECTION_CHANGED = "vcombobox-item-selection-changed";
        public static final String ITEM_ADDED = "vcombobox-item-added";
        public static final String ITEM_REMOVED = "vcombobox-item-removed";
    }

    // Line input
    public static class LineInput {
        public static final String CHANGE = "vline-change";
        public static final String CURSOR_MOVE = "vline-cursor-move";
        public static final String CURSOR_MOVE_LEFT = "vline-cursor-move-left";
        public static final String CURSOR_MOVE_RIGHT = "vline-cursor-move-right";
        public static final String ADD_CHAR_LIMITED = "vline-add-char-limited";
    }

    // Tabs
    public static class TabWidget {
        public static final String TAB_NAME_CHANGED = "vtabwidget-tab-name-change";
        public static final String TAB_ADDED = "vtabwidget-tab-added";
        public static final String WIDGET_ADDED = "vtabwidget-tab-widget-added";
    }

    // Scroll Box
    public static class ScrollBox {
        public static final String SCROLLED_X = "vscrollbox-scrolled-x";
        public static final String SCROLLED_Y = "vscrollbox-scrolled-y";
    }
}
