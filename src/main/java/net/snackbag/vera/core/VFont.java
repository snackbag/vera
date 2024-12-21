package net.snackbag.vera.core;

import net.snackbag.vera.Vera;

import java.util.function.Consumer;

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

    public static class FontModifier {
        private VFont font;
        private final Consumer<VFont> fontUpdater;

        public FontModifier(VFont font, Consumer<VFont> fontUpdater) {
            this.font = font;
            this.fontUpdater = fontUpdater;
        }

        public FontModifier color(VColor color) {
            font = font.withColor(color);
            fontUpdater.accept(font);
            return this;
        }

        public FontModifier colorRed(int red) {
            font = font.withColor(font.getColor().withRed(red));
            fontUpdater.accept(font);
            return this;
        }

        public FontModifier colorGreen(int green) {
            font = font.withColor(font.getColor().withGreen(green));
            fontUpdater.accept(font);
            return this;
        }

        public FontModifier colorBlue(int blue) {
            font = font.withColor(font.getColor().withBlue(blue));
            fontUpdater.accept(font);
            return this;
        }

        public FontModifier colorOpacity(float opacity) {
            font = font.withColor(font.getColor().withOpacity(opacity));
            fontUpdater.accept(font);
            return this;
        }

        public FontModifier name(String name) {
            font = font.withName(name);
            fontUpdater.accept(font);
            return this;
        }

        public FontModifier size(int size) {
            font = font.withSize(size);
            fontUpdater.accept(font);
            return this;
        }
    }
}
