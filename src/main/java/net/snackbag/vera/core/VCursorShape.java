package net.snackbag.vera.core;

import org.lwjgl.glfw.GLFW;

public enum VCursorShape {
    DEFAULT(GLFW.GLFW_ARROW_CURSOR),
    TEXT(GLFW.GLFW_IBEAM_CURSOR),
    CROSSHAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
    POINTING_HAND(GLFW.GLFW_POINTING_HAND_CURSOR),
    NOT_ALLOWED(GLFW.GLFW_NOT_ALLOWED_CURSOR),

    HORIZONTAL_RESIZE(GLFW.GLFW_HRESIZE_CURSOR),
    VERTICAL_RESIZE(GLFW.GLFW_VRESIZE_CURSOR),

    ALL_RESIZE(GLFW.GLFW_RESIZE_ALL_CURSOR),
    EAST_WEST_RESIZE(GLFW.GLFW_RESIZE_EW_CURSOR),
    NORTH_SOUTH_RESIZE(GLFW.GLFW_RESIZE_NS_CURSOR),
    NW_SE_RESIZE(GLFW.GLFW_RESIZE_NESW_CURSOR),
    NE_SW_RESIZE(GLFW.GLFW_RESIZE_NWSE_CURSOR);

    private final int glfwCursorShape;
    private final long glfwCursor;

    VCursorShape(int glfwCursor) {
        this.glfwCursorShape = glfwCursor;
        this.glfwCursor = GLFW.glfwCreateStandardCursor(glfwCursor);
    }

    public int getGLFWCursorShape() {
        return glfwCursorShape;
    }

    public long getGLFWCursor() {
        return glfwCursor;
    }
}
