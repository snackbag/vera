package net.snackbag.vera.core;

public enum VMouseButton {
    LEFT,
    MIDDLE,
    RIGHT;

    public static VMouseButton fromInt(int button) {
        return switch (button) {
            case 0 -> LEFT;
            case 1 -> RIGHT;
            case 2 -> MIDDLE;
            default -> throw new IllegalArgumentException("Invalid button type: %d".formatted(button));
        };
    }
}
