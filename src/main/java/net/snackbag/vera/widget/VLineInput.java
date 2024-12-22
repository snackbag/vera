package net.snackbag.vera.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class VLineInput extends VWidget<VLineInput> {
    private VFont font;
    private String text;
    private @Nullable VColor cursorColor = null;
    private int cursorPos = 0;

    public VLineInput(VeraApp app) {
        super(0, 0, 100, 20, app);
        text = "";
        font = VFont.create();

        setHoverCursor(VCursorShape.TEXT);
    }

    @Override
    public void render() {
        Vera.renderer.drawText(app, x, y, 0, text, font);

        if (isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
            Vera.renderer.drawRect(app,
                    x + Vera.provider.getTextWidth(text.substring(0, cursorPos), font), y,
                    1, Vera.provider.getTextHeight(text, font), 0, getCursorColorSafe());
        }
    }

    public VFont getFont() {
        return font;
    }

    public void setFont(VFont font) {
        this.font = font;
    }

    public VFont.FontModifier modifyFont() {
        return new VFont.FontModifier(font, this::setFont);
    }

    public VColor.ColorModifier modifyFontColor() {
        return new VColor.ColorModifier(font.getColor(), (color) -> setFont(font.withColor(color)));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        fireEvent("vline-change");
    }

    public void onLineChanged(Runnable runnable) {
        registerEventExecutor("vline-change", runnable);
    }

    public void onCursorMove(Runnable runnable) {
        registerEventExecutor("vline-cursor-move", runnable);
    }

    public void onCursorMoveLeft(Runnable runnable) {
        registerEventExecutor("vline-cursor-move-left", runnable);
    }

    public void onCursorMoveRight(Runnable runnable) {
        registerEventExecutor("vline-cursor-move-right", runnable);
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !text.isEmpty() && cursorPos > 0) {
            StringBuilder builder = new StringBuilder(text);
            builder.deleteCharAt(cursorPos - 1);

            cursorPos -= 1;
            text = builder.toString();
            fireEvent("vline-change");
        } else if (isDown(GLFW.GLFW_KEY_LEFT) && isAltDown()) {
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");

            if (cursorPos <= 0) {
                cursorPos = 0;
                return;
            }

            int pos = cursorPos - 1;
            while (pos > 0 && !Character.isLetterOrDigit(text.charAt(pos))) {
                pos--;
            }

            while (pos > 0 && Character.isLetterOrDigit(text.charAt(pos - 1))) {
                pos--;
            }

            cursorPos = pos;
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isAltDown()) {
            if (cursorPos >= text.length()) cursorPos = text.length();

            int pos = cursorPos;

            while (pos < text.length() && !Character.isLetterOrDigit(text.charAt(pos))) {
                pos++;
            }

            while (pos < text.length() && Character.isLetterOrDigit(text.charAt(pos))) {
                pos++;
            }

            cursorPos = pos;
        } else if (isDown(GLFW.GLFW_KEY_LEFT) && isCtrlDown()) {
            cursorPos = 0;
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isCtrlDown()) {
            cursorPos = text.length();
        } else if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPos > 0) {
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
            cursorPos -= 1;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPos < text.length()) {
            cursorPos += 1;
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-right");
        }

        super.keyPressed(keyCode, scanCode, modifiers);
    }

    public int getCursorPos() {
        return cursorPos;
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    public @Nullable VColor getCursorColor() {
        return cursorColor;
    }

    public VColor getCursorColorSafe() {
        return cursorColor == null ? font.getColor() : cursorColor;
    }

    public void setCursorColor(@Nullable VColor cursorColor) {
        this.cursorColor = cursorColor;
    }

    @Override
    public int getHitboxWidth() {
        return Math.max(width, Vera.provider.getTextWidth(text, font));
    }

    @Override
    public int getHitboxHeight() {
        return Vera.provider.getTextHeight(text, font);
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (!Character.isISOControl(chr)) {
            String front = text.substring(0, cursorPos);
            String back = text.substring(cursorPos);

            text = front + chr + back;
            cursorPos += 1;
            fireEvent("vline-change");
        }
        super.charTyped(chr, modifiers);
    }

    private boolean isDown(int key) {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key);
    }

    private boolean isAltDown() {
        return isDown(GLFW.GLFW_KEY_LEFT_ALT) || isDown(GLFW.GLFW_KEY_RIGHT_ALT);
    }

    private boolean isCtrlDown() {
        return SystemUtils.IS_OS_MAC_OSX ?
                isDown(GLFW.GLFW_KEY_LEFT_SUPER) || isDown(GLFW.GLFW_KEY_RIGHT_SUPER) :
                isDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isDown(GLFW.GLFW_KEY_RIGHT_CONTROL);
    }
}
