package net.snackbag.vera.event;

import net.snackbag.vera.widget.VWidget;

public class VTabWidgetEvent {
    public record TabNameChanged(String name) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.TabWidget.TAB_NAME_CHANGED;
        }
    }

    public record WidgetAdded(VWidget<?> widget) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.TabWidget.WIDGET_ADDED;
        }
    }

    public record TabAdded(String name) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.TabWidget.TAB_ADDED;
        }
    }
}
