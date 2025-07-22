package net.snackbag.vera.event;

// Sorted by :sparkle: the feeling that it looks nice :sparkle:
public class Events {
    // Animation
    public static class Animation {
        public static final String BEGIN = "animation-begin";
        public static final String FINISH = "animation-finish";
        public static final String UNWIND_BEGIN = "animation-unwind-begin"; // TODO: implement
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
        public static final String DRAG_LEFT_CLICK = "mouse-drag-left";
        public static final String DRAG_RIGHT_CLICK = "mouse-drag-right";
        public static final String DRAG_MIDDLE_CLICK = "mouse-drag-middle";

        public static final String FOCUS_STATE_CHANGE = "focus-state-change";
        public static final String FILES_DROPPED = "files-dropped";
    }

    // Checkbox
    public static class CheckBox {
        public static final String CHECKED = "vcheckbox-checked";
    }

    // Dropdown
    public static class Dropdown {
        public static final String ITEM_SWITCH = "vdropdown-item-switch";
        public static final String SELECTOR_OPEN = "vdropdown-selector-open";
        public static final String SELECTOR_CLOSE = "vdropdown-selector-close";
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
        public static final String TAB_HOVER_CHANGE = "vtabwidget-tab-hover-change";
        public static final String TAB_LEFT_CLICK = "vtabwidget-tab-left-click";
        public static final String TAB_LEFT_CLICK_RELEASE = "vtabwidget-tab-left-click-release";
        public static final String TAB_MIDDLE_CLICK = "vtabwidget-tab-middle-click";
        public static final String TAB_MIDDLE_CLICK_RELEASE = "vtabwidget-tab-middle-click-release";
        public static final String TAB_RIGHT_CLICK = "vtabwidget-tab-right-click";
        public static final String TAB_RIGHT_CLICK_RELEASE = "vtabwidget-tab-right-click-release";
    }
}
