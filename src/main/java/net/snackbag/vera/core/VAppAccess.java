package net.snackbag.vera.core;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface VAppAccess {
    @NotNull
    VeraApp get();
}
