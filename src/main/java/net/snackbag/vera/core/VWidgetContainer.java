package net.snackbag.vera.core;

import net.snackbag.vera.widget.VWidget;

import java.util.Collections;
import java.util.List;

public interface VWidgetContainer {
    List<VWidget<?>> getWidgets();
    void addWidget(VWidget<?> widget);
    void removeWidget(VWidget<?> widget);

    default List<VWidget<?>> getWidgetsReversed() {
        List<VWidget<?>> widgets = getWidgets();
        Collections.reverse(widgets);
        return widgets;
    }
}
