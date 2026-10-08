package net.snackbag.vera.event;

import net.snackbag.vera.widget.VComboBox;

public class VComboBoxEvent {
    public record ItemAdded(VComboBox.Item item) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.ComboBox.ITEM_ADDED;
        }
    }

    public record ItemRemoved(int index) implements VEventContext {
        @Override
        public String eventName() {
            return VEvents.ComboBox.ITEM_REMOVED;
        }
    }
}
