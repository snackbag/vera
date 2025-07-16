package net.snackbag.vera.style.standard;

import net.snackbag.vera.style.VStyleSheet;

public interface VStandardStyle {
    void apply(VStyleSheet sheet);
    default void reserve(VStyleSheet sheet) {}
}
