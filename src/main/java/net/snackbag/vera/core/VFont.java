package net.snackbag.vera.core;

import net.snackbag.vera.Vera;

public class VFont {
    private final String name;
    private final int size;
    private final VColor color;

    public VFont(String name, int size, VColor color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    public VFont(String name, int size) {
        this(name, size, VColor.black());
    }

    public VFont(String name) {
        this(name, 16, VColor.black());
    }

    public static VFont create() {
        return new VFont(Vera.provider.getDefaultFontName());
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public VColor getColor() {
        return color;
    }

    public VFont withSize(int size) {
        return new VFont(name, size, color);
    }

    public VFont withColor(VColor color) {
        return new VFont(name, size, color);
    }

    public VFont withName(String name) {
        return new VFont(name, size, color);
    }
}
