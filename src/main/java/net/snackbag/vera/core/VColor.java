package net.snackbag.vera.core;

import java.util.function.Consumer;

public class VColor {
    private final int red;
    private final int green;
    private final int blue;
    private final float opacity;

    private VColor(int red, int green, int blue) {
        this(red, green, blue, 1f);
    }

    private VColor(int red, int green, int blue, float opacity) {
        this.red = Math.max(Math.min(255, red), 0);
        this.green = Math.max(Math.min(255, green), 0);
        this.blue = Math.max(Math.min(255, blue), 0);

        this.opacity = Math.max(Math.min(1f, opacity), 0f);
    }

    public static VColor of(int red, int green, int blue) {
        return new VColor(red, green, blue);
    }

    public static VColor of(int red, int green, int blue, float opacity) {
        return new VColor(red, green, blue, opacity);
    }

    public int red() {
        return red;
    }

    public int green() {
        return green;
    }

    public int blue() {
        return blue;
    }

    public float opacity() {
        return opacity;
    }

    public int oneRed() {
        return red / 255;
    }

    public int oneGreen() {
        return green / 255;
    }

    public int oneBlue() {
        return blue / 255;
    }

    public boolean isTransparent() {
        return opacity == 0;
    }

    public boolean sameColors(int red, int green, int blue) {
        return this.red == red && this.green == green && this.blue == blue;
    }

    public boolean sameColors(VColor color) {
        return sameColors(color.red, color.green, color.blue);
    }

    public boolean same(int red, int green, int blue, float opacity) {
        return sameColors(red, green, blue) && this.opacity == opacity;
    }

    public boolean same(VColor color) {
        return same(color.red, color.green, color.blue, color.opacity);
    }

    public int toInt() {
        int alpha = (int) (opacity * 255);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public String toString() {
        return "rgba(" + red + ", " + green + ", " + blue + ", " + opacity + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VColor color)) {
            return false;
        }

        return same(color);
    }

    public VColor withRed(int red) {
        return new VColor(red, green, blue, opacity);
    }

    public VColor withGreen(int green) {
        return new VColor(red, green, blue, opacity);
    }

    public VColor withBlue(int blue) {
        return new VColor(red, green, blue, opacity);
    }

    public VColor withOpacity(float opacity) {
        return new VColor(red, green, blue, opacity);
    }

    public VColor add(int all) {
        return new VColor(Math.min(red + all, 255), Math.min(green + all, 255), Math.min(blue + all, 255));
    }

    public VColor add(int red, int green, int blue) {
        return new VColor(Math.min(this.red + red, 255), Math.min(this.green + green, 255), Math.min(this.blue + blue, 255));
    }

    public VColor sub(int all) {
        return new VColor(Math.max(red - all, 0), Math.max(green - all, 0), Math.max(blue - all, 0));
    }

    public VColor sub(int red, int green, int blue) {
        return new VColor(Math.max(this.red - red, 0), Math.max(this.green - green, 0), Math.max(this.blue - blue, 0));
    }

    public static VColor transparent() {
        return new VColor(0, 0, 0, 0);
    }

    public static VColor white() {
        return new VColor(255, 255, 255, 1);
    }

    public static VColor black() {
        return new VColor(0, 0, 0, 1);
    }

    public static class ColorModifier {
        private VColor color;
        private final Consumer<VColor> colorUpdater;

        public ColorModifier(VColor color, Consumer<VColor> colorUpdater) {
            this.color = color;
            this.colorUpdater = colorUpdater;
        }

        public ColorModifier rgb(int r, int g, int b) {
            color = new VColor(r, g, b, color.opacity);
            colorUpdater.accept(color);
            return this;
        }

        public ColorModifier rgba(int r, int g, int b, float a) {
            color = new VColor(r, g, b, a);
            colorUpdater.accept(color);
            return this;
        }

        public ColorModifier red(int r) {
            color = color.withRed(r);
            colorUpdater.accept(color);
            return this;
        }

        public ColorModifier green(int g) {
            color = color.withGreen(g);
            colorUpdater.accept(color);
            return this;
        }

        public ColorModifier blue(int b) {
            color = color.withBlue(b);
            colorUpdater.accept(color);
            return this;
        }

        public ColorModifier opacity(float o) {
            color = color.withOpacity(o);
            colorUpdater.accept(color);
            return this;
        }
    }
}
