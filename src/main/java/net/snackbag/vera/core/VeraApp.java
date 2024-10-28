package net.snackbag.vera.core;

import net.snackbag.vera.VeraProvider;

public abstract class VeraApp {
    private final VeraProvider provider;

    public VeraApp(VeraProvider provider) {
        this.provider = provider;
    }

    public abstract void init();
}
