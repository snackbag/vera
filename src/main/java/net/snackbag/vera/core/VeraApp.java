package net.snackbag.vera.core;

import net.snackbag.vera.VeraProvider;
import net.snackbag.vera.widget.VWidget;

import java.util.ArrayList;
import java.util.List;

public abstract class VeraApp {
    private final VeraProvider provider;
    private final List<VWidget> widgets;

    public VeraApp(VeraProvider provider) {
        this.provider = provider;
        this.widgets = new ArrayList<>();

        provider.handleAppInitialization(this);
    }

    public VeraProvider getProvider() {
        return provider;
    }

    public abstract void init();

    public List<VWidget> getWidgets() {
        return new ArrayList<>(widgets);
    }

    public void addWidget(VWidget widget) {
        this.widgets.add(widget);
    }
}
