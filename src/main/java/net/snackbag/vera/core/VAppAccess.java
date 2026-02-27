package net.snackbag.vera.core;

import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface VAppAccess {
    @NotNull
    VeraApp get();

    List<VWidget<?>> getWidgets();
    void removeWidget(VWidget<?> widget);
    void addWidget(VWidget<?> widget);

    default List<VWidget<?>> getWidgetsReversed() {
        List<VWidget<?>> widgets = getWidgets();
        Collections.reverse(widgets);
        return widgets;
    }
}
