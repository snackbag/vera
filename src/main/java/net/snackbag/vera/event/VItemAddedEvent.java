package net.snackbag.vera.event;

import net.snackbag.vera.widget.VComboBox;

public interface VItemAddedEvent {
    void run(VComboBox.Item item);
}
