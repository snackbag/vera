package net.snackbag.vera.core;

public class VColor {
    private final int red;
    private final int green;
    private final int blue;
    private final float opacity;

    private VColor(int red, int green, int blue) {
        this(red, green, blue, 1f);
    }

    private VColor(int red, int green, int blue, float opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;

        this.opacity = opacity;
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

    public static VColor transparent() {
        return new VColor(0, 0, 0, 0);
    }

    public static VColor white() {
        return new VColor(255, 255, 255, 1);
    }

    public static VColor black() {
        return new VColor(0, 0, 0, 1);
    }
}
