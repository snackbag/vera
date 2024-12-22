package net.snackbag.vera.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.VCharLimitedEvent;
import net.snackbag.vera.modifier.VPaddingWidget;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class VLineInput extends VWidget<VLineInput> implements VPaddingWidget {
    private String text;
    private String placeholderText;
    private VFont font;
    private VFont placeholderFont;

    private @Nullable VColor cursorColor;
    private int cursorPos;
    private @Nullable Integer maxChars;

    private VColor backgroundColor;
    private V4Int padding;

    public VLineInput(VeraApp app) {
        super(0, 0, 100, 20, app);

        this.text = "";
        this.placeholderText = "";
        this.font = VFont.create();
        this.placeholderFont = VFont.create().withColor(VColor.black().withOpacity(0.5f));
        this.cursorColor = null;
        this.cursorPos = 0;
        this.maxChars = null;

        this.backgroundColor = VColor.transparent();
        this.padding = new V4Int(4);

        setHoverCursor(VCursorShape.TEXT);
    }

    @Override
    public void render() {
        Vera.renderer.drawRect(
                app,
                getHitboxX() + app.getX(),
                getHitboxY() + app.getY(),
                getHitboxWidth(),
                getHitboxHeight(),
                rotation,
                backgroundColor
        );

        if (text.isEmpty()) Vera.renderer.drawText(app, x, y, 0, placeholderText, placeholderFont);
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

    public VFont getPlaceholderFont() {
        return placeholderFont;
    }

    public void setPlaceholderFont(VFont placeholderFont) {
        this.placeholderFont = placeholderFont;
    }

    public VFont.FontModifier modifyFont() {
        return new VFont.FontModifier(font, this::setFont);
    }

    public VColor.ColorModifier modifyFontColor() {
        return new VColor.ColorModifier(font.getColor(), (color) -> setFont(font.withColor(color)));
    }

    public VFont.FontModifier modifyPlaceholderFont() {
        return new VFont.FontModifier(font, this::setPlaceholderFont);
    }

    public VColor.ColorModifier modifyPlaceholderFontColor() {
        return new VColor.ColorModifier(font.getColor(), (color) -> setFont(font.withColor(color)));
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(VColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public VColor.ColorModifier modifyBackgroundColor() {
        return new VColor.ColorModifier(backgroundColor, this::setBackgroundColor);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        fireEvent("vline-change");
    }

    public @Nullable Integer getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(@Nullable Integer maxChars) {
        this.maxChars = maxChars;
    }

    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
    }

    public String getPlaceholderText() {
        return placeholderText;
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

    public void onAddCharLimited(VCharLimitedEvent runnable) {
        registerEventExecutor("vline-add-char-limited", args -> runnable.run((char) args[0]));
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (text == null) {
            text = "";
            return;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && isAltDown() && cursorPos > 0) {
            // Alt + Backspace: Delete the word to the left
            int newCursorPos = Math.max(0, jumpToWordStart(cursorPos));
            deleteText(newCursorPos, cursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_DELETE && isAltDown() && cursorPos < text.length()) {
            // Alt + Delete: Delete the word to the right
            int newCursorPos = Math.min(text.length(), jumpToWordEnd(cursorPos));
            deleteText(cursorPos, newCursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE && isCtrlDown() && cursorPos > 0) {
            // Ctrl + Backspace: Delete all text to the left of the cursor
            deleteText(0, cursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_DELETE && isCtrlDown() && cursorPos < text.length()) {
            // Ctrl + Delete: Delete all text to the right of the cursor
            deleteText(cursorPos, text.length());
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE && cursorPos > 0) {
            // Regular Backspace: Delete one character to the left
            deleteText(cursorPos - 1, cursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_DELETE && cursorPos < text.length()) {
            // Regular Delete: Delete one character to the right
            deleteText(cursorPos, cursorPos + 1);
        } else if (isDown(GLFW.GLFW_KEY_LEFT) && isAltDown() && cursorPos > 0) {
            // Alt + Left: Jump to the start of the word to the left
            cursorPos = Math.max(0, jumpToWordStart(cursorPos));
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isAltDown() && cursorPos < text.length()) {
            // Alt + Right: Jump to the end of the word to the right
            cursorPos = Math.min(text.length(), jumpToWordEnd(cursorPos));
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-right");
        } else if (isDown(GLFW.GLFW_KEY_LEFT) && isCtrlDown()) {
            // Ctrl + Left: Move to the start of the text
            cursorPos = 0;
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isCtrlDown()) {
            // Ctrl + Right: Move to the end of the text
            cursorPos = text.length();
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-right");
        } else if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPos > 0) {
            // Left Arrow: Move cursor one character left
            cursorPos = Math.max(0, cursorPos - 1);
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPos < text.length()) {
            // Right Arrow: Move cursor one character right
            cursorPos = Math.min(text.length(), cursorPos + 1);
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
    public V4Int getPadding() {
        return padding;
    }

    @Override
    public void setPadding(V4Int padding) {
        this.padding = padding;
    }

    @Override
    public int getHitboxWidth() {
        return Math.max(width, Vera.provider.getTextWidth(text, font)) + padding.get3() + padding.get4();
    }

    @Override
    public int getHitboxHeight() {
        return Vera.provider.getTextHeight(text, font) + padding.get1() + padding.get2();
    }


    @Override
    public int getHitboxX() {
        return x - padding.get4();
    }

    @Override
    public int getHitboxY() {
        return y - padding.get1();
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (maxChars != null && text.length() >= maxChars) {
            fireEvent("vline-add-char-limited", chr);
            return;
        }

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
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) {
            return false;
        }
        return InputUtil.isKeyPressed(client.getWindow().getHandle(), key);
    }

    private boolean isAltDown() {
        return isDown(GLFW.GLFW_KEY_LEFT_ALT) || isDown(GLFW.GLFW_KEY_RIGHT_ALT);
    }

    private boolean isCtrlDown() {
        return SystemUtils.IS_OS_MAC_OSX ?
                isDown(GLFW.GLFW_KEY_LEFT_SUPER) || isDown(GLFW.GLFW_KEY_RIGHT_SUPER) :
                isDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isDown(GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    private int jumpToWordStart(int position) {
        if (text == null || position <= 0) {
            return 0;
        }

        int pos = Math.min(position - 1, text.length() - 1);

        while (pos > 0 && !Character.isLetterOrDigit(text.charAt(pos))) {
            pos--;
        }

        while (pos > 0 && Character.isLetterOrDigit(text.charAt(pos - 1))) {
            pos--;
        }

        return pos;
    }

    private int jumpToWordEnd(int position) {
        if (text == null || position >= text.length()) {
            return text == null ? 0 : text.length();
        }

        int pos = position;

        while (pos < text.length() && !Character.isLetterOrDigit(text.charAt(pos))) {
            pos++;
        }

        while (pos < text.length() && Character.isLetterOrDigit(text.charAt(pos))) {
            pos++;
        }

        return pos;
    }

    private void deleteText(int start, int end) {
        if (text == null || start < 0 || end > text.length() || start >= end) {
            return;
        }

        StringBuilder builder = new StringBuilder(text);
        builder.delete(start, end);
        text = builder.toString();
        cursorPos = Math.min(start, text.length());
        fireEvent("vline-change");
    }
}
