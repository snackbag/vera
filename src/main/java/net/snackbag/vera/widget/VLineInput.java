package net.snackbag.vera.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.VCharLimitedEvent;
import net.snackbag.vera.modifier.VPaddingWidget;
import net.snackbag.vera.style.StyleState;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class VLineInput extends VWidget<VLineInput> implements VPaddingWidget {
    private String text;
    private String placeholderText;
    private VFont font;
    private VFont placeholderFont;

    private int cursorPos;
    private TextSelection textSelection;
    private int maxChars;

    private V4Int padding;

    public VLineInput(VeraApp app) {
        super(0, 0, 100, 20, app);

        this.text = "";
        this.placeholderText = "";
        this.font = VFont.create();
        this.placeholderFont = VFont.create().withColor(VColor.black().withOpacity(0.5f));
        this.cursorPos = 0;
        this.textSelection = new TextSelection();
        setStyle("select-color", VColor.of(0, 120, 215, 0.2f));
        this.maxChars = -1;

        setStyle("background-color", VColor.transparent());
        this.padding = new V4Int(4);

        setStyle("cursor", StyleState.HOVERED, VCursorShape.TEXT);
    }

    @Override
    public void render() {
        StyleState state = createStyleState();

        VColor backgroundColor = getStyle("background-color", state);
        VColor textSelectionColor = getStyle("select-color", state);

        Vera.renderer.drawRect(
                app,
                getHitboxX() + app.getX(),
                getHitboxY() + app.getY(),
                getHitboxWidth(),
                getHitboxHeight(),
                rotation,
                backgroundColor
        );

        // Render text selection background
        if (!textSelection.isClear()) {
            int selStart = Math.min(textSelection.startPos, textSelection.endPos);
            int selEnd = Math.max(textSelection.startPos, textSelection.endPos);
            String beforeSelection = text.substring(0, selStart);
            String selectedText = text.substring(selStart, selEnd);

            int selectionX = x + Vera.provider.getTextWidth(beforeSelection, font);
            Vera.renderer.drawRect(
                    app,
                    selectionX,
                    y,
                    Vera.provider.getTextWidth(selectedText, font),
                    Vera.provider.getTextHeight(text, font),
                    0,
                    textSelectionColor
            );
        }

        if (text.isEmpty()) Vera.renderer.drawText(app, x, y, 0, placeholderText, placeholderFont);
        else Vera.renderer.drawText(app, x, y, 0, text, font);

        if (isFocused() && textSelection.isClear() && (System.currentTimeMillis() / 500) % 2 == 0) {
            Vera.renderer.drawRect(
                    app,
                    x + Vera.provider.getTextWidth(text.substring(0, cursorPos), font),
                    y,
                    1,
                    Vera.provider.getTextHeight(text, font),
                    0,
                    getCursorColorSafe()
            );
        }
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        if (event.equals("left-click")) {
            textSelection.clear();

            if (Vera.getMouseX() < x) cursorPos = 0;
            else if (Vera.getMouseX() > x + Vera.provider.getTextWidth(text, font)) cursorPos = text.length();
        }

        super.handleBuiltinEvent(event, args);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        fireEvent("vline-change");
    }

    public boolean isSelectingText() {
        return !textSelection.isClear();
    }

    public void clearTextSelection() {
        textSelection.clear();
    }

    public @Nullable Integer getTextSelectionStart() {
        return textSelection.isClear() ? null : textSelection.startPos;
    }

    public @Nullable Integer getTextSelectionEnd() {
        return textSelection.isClear() ? null : textSelection.endPos;
    }

    public void setTextSelection(int start, int end) {
        textSelection.setStartPos(start);
        textSelection.setEndPos(end);
    }

    public int getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(@Nullable Integer maxChars) {
        this.maxChars = maxChars == null ? -1 : maxChars;
    }

    public void setMaxChars(int maxChars) {
        setMaxChars(Integer.valueOf(maxChars));
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

        // Handle select all
        if (isCtrlDown() && keyCode == GLFW.GLFW_KEY_A) {
            selectAll();
            return;
        }

        // Handle copy
        if (isCtrlDown() && keyCode == GLFW.GLFW_KEY_C && !textSelection.isClear()) {
            String selectedText = getSelectedText();
            MinecraftClient.getInstance().keyboard.setClipboard(selectedText);
            return;
        }

        // Handle paste
        if (isCtrlDown() && keyCode == GLFW.GLFW_KEY_V) {
            String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();
            if (!clipboard.isEmpty()) {
                if (!textSelection.isClear()) {
                    replaceSelectedText(clipboard);
                } else {
                    insertText(clipboard);
                }
            }
            return;
        }

        // Handle cut
        if (isCtrlDown() && keyCode == GLFW.GLFW_KEY_X && !textSelection.isClear()) {
            String selectedText = getSelectedText();
            MinecraftClient.getInstance().keyboard.setClipboard(selectedText);
            deleteSelectedText();
            return;
        }

        // Handle selection with arrow keys
        if ((keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT) && isShiftDown()) {
            handleSelectionKeyPress(keyCode);
            return;
        }

        // Handle deletion of selected text
        if (!textSelection.isClear() &&
                (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE)) {
            deleteSelectedText();
            return;
        }

        // Clear selection on cursor movement without shift
        if ((keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT) && !isShiftDown()) {
            clearTextSelection();
        }

        // Handle word deletion
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && isAltDown() && cursorPos > 0) {
            int newCursorPos = Math.max(0, jumpToWordStart(cursorPos));
            deleteText(newCursorPos, cursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_DELETE && isAltDown() && cursorPos < text.length()) {
            int newCursorPos = Math.min(text.length(), jumpToWordEnd(cursorPos));
            deleteText(cursorPos, newCursorPos);
        }
        // Handle line deletion
        else if (keyCode == GLFW.GLFW_KEY_BACKSPACE && isCtrlDown() && cursorPos > 0) {
            deleteText(0, cursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_DELETE && isCtrlDown() && cursorPos < text.length()) {
            deleteText(cursorPos, text.length());
        }
        // Handle single character deletion
        else if (keyCode == GLFW.GLFW_KEY_BACKSPACE && cursorPos > 0) {
            deleteText(cursorPos - 1, cursorPos);
        } else if (keyCode == GLFW.GLFW_KEY_DELETE && cursorPos < text.length()) {
            deleteText(cursorPos, cursorPos + 1);
        }
        // Handle word navigation
        else if (isDown(GLFW.GLFW_KEY_LEFT) && isAltDown() && cursorPos > 0) {
            cursorPos = Math.max(0, jumpToWordStart(cursorPos));
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isAltDown() && cursorPos < text.length()) {
            cursorPos = Math.min(text.length(), jumpToWordEnd(cursorPos));
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-right");
        }
        // Handle line navigation
        else if (isDown(GLFW.GLFW_KEY_LEFT) && isCtrlDown()) {
            cursorPos = 0;
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isCtrlDown()) {
            cursorPos = text.length();
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-right");
        }
        // Handle character navigation
        else if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPos > 0) {
            cursorPos = Math.max(0, cursorPos - 1);
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-left");
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPos < text.length()) {
            cursorPos = Math.min(text.length(), cursorPos + 1);
            fireEvent("vline-cursor-move");
            fireEvent("vline-cursor-move-right");
        }

        super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void handleSelectionKeyPress(int keyCode) {
        if (textSelection.isClear()) {
            textSelection.startPos = cursorPos;
        }

        int newPos = cursorPos;
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (isAltDown()) {
                newPos = Math.max(0, jumpToWordStart(cursorPos));
            } else if (isCtrlDown()) {
                newPos = 0;
            } else {
                newPos = Math.max(0, cursorPos - 1);
            }
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (isAltDown()) {
                newPos = Math.min(text.length(), jumpToWordEnd(cursorPos));
            } else if (isCtrlDown()) {
                newPos = text.length();
            } else {
                newPos = Math.min(text.length(), cursorPos + 1);
            }
        }

        cursorPos = newPos;
        textSelection.endPos = newPos;
        fireEvent("vline-cursor-move");
    }

    private void insertText(String insertion) {
        if (maxChars > -1 && text.length() + insertion.length() > maxChars) {
            fireEvent("vline-add-char-limited", insertion.charAt(0));
            return;
        }

        String front = text.substring(0, cursorPos);
        String back = text.substring(cursorPos);
        text = front + insertion + back;
        cursorPos += insertion.length();
        fireEvent("vline-change");
    }

    private void deleteSelectedText() {
        if (textSelection.isClear()) return;

        int start = Math.min(textSelection.startPos, textSelection.endPos);
        int end = Math.max(textSelection.startPos, textSelection.endPos);

        String front = text.substring(0, start);
        String back = text.substring(end);
        text = front + back;
        cursorPos = start;
        clearTextSelection();
        fireEvent("vline-change");
    }

    private void replaceSelectedText(String replacement) {
        if (textSelection.isClear()) return;

        int start = Math.min(textSelection.startPos, textSelection.endPos);
        int end = Math.max(textSelection.startPos, textSelection.endPos);

        if (maxChars > -1 && text.length() - (end - start) + replacement.length() > maxChars) {
            fireEvent("vline-add-char-limited", replacement.charAt(0));
            return;
        }

        String front = text.substring(0, start);
        String back = text.substring(end);
        text = front + replacement + back;
        cursorPos = start + replacement.length();
        clearTextSelection();
        fireEvent("vline-change");
    }


    private String getSelectedText() {
        if (textSelection.isClear()) return "";
        int start = Math.min(textSelection.startPos, textSelection.endPos);
        int end = Math.max(textSelection.startPos, textSelection.endPos);
        return text.substring(start, end);
    }

    public int getCursorPos() {
        return cursorPos;
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    public VColor getCursorColorSafe() {
        VColor style = getStyleOrDefault("cursor-color", null);

        return style == null ? font.getColor() : style;
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
        if (!Character.isISOControl(chr)) {
            if (!textSelection.isClear()) {
                // Replace selected text with the typed character
                int start = Math.min(textSelection.startPos, textSelection.endPos);
                int end = Math.max(textSelection.startPos, textSelection.endPos);

                if (maxChars > -1 && text.length() - (end - start) + 1 > maxChars) {
                    fireEvent("vline-add-char-limited", chr);
                    return;
                }

                String front = text.substring(0, start);
                String back = text.substring(end);

                text = front + chr + back;
                cursorPos = start + 1;
                clearTextSelection();
                fireEvent("vline-change");
            } else {
                // Normal character insertion
                if (maxChars > -1 && text.length() >= maxChars) {
                    fireEvent("vline-add-char-limited", chr);
                    return;
                }

                String front = text.substring(0, cursorPos);
                String back = text.substring(cursorPos);

                text = front + chr + back;
                cursorPos += 1;
                fireEvent("vline-change");
            }
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

    private boolean isShiftDown() {
        return isDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
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

    public void selectAll() {
        textSelection.startPos = 0;
        textSelection.endPos = text.length();
        cursorPos = text.length();
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

    public static class TextSelection {
        private @Nullable Integer startPos;
        private @Nullable Integer endPos;

        public TextSelection() {
            this.startPos = null;
            this.endPos = null;
        }

        public @Nullable Integer getStartPos() {
            return startPos;
        }

        public @Nullable Integer getEndPos() {
            return endPos;
        }

        public void setStartPos(int startPos) {
            this.startPos = startPos;
        }

        public void setEndPos(int endPos) {
            this.endPos = endPos;
        }

        public void clear() {
            this.startPos = null;
            this.endPos = null;
        }

        public boolean isClear() {
            return startPos == null || endPos == null;
        }
    }
}
