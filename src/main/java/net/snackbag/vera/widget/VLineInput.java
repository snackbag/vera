package net.snackbag.vera.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VCharLimitedEvent;
import net.snackbag.vera.modifier.VHasFont;
import net.snackbag.vera.modifier.VHasPlaceholderFont;
import net.snackbag.vera.style.VStyleState;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class VLineInput extends VWidget<VLineInput> implements VHasFont, VHasPlaceholderFont {
    private String text;
    private String placeholderText;

    private int cursorPos;
    private TextSelection textSelection;
    private int maxChars;
    private long timeSinceLastInput;
    private int textViewport = 0;

    public VLineInput(VAppAccess app) {
        super(0, 0, 100, 20, app);

        this.text = "";
        this.placeholderText = "";
        this.cursorPos = 0;
        this.textSelection = new TextSelection();
        this.maxChars = -1;
        this.timeSinceLastInput = System.currentTimeMillis();
    }

    @Override
    public void renderContent(VRenderContext ctx) {
        VStyleState state = createStyleState();

        VFont font = getStyle("font", state);
        VFont placeholderFont = getStyle("placeholder-font", state);
        VFill background = getStyle("background", state);
        VFill textSelectionFill = getStyle("select", state);
        V4Int padding = getStyle("padding", state);
        String rText = getTextInViewport();

        // background
        Vera.renderer.drawFill(ctx, 0, 0, getEffectiveWidth(), getEffectiveHeight(), background);

        // text selection
        int textHeight = Vera.provider.getTextHeight(text, font);
        int textWidth = Vera.provider.getTextWidth(rText, font);
        int textX = Vera.provider.getTextWidth(text, font) < width
                ? padding.get3()
                : padding.get3() - textWidth + width;
        int textY = padding.get1() + height / 2 - textHeight / 2;

        if (!textSelection.isClear()) {
            int selStart = Math.min(textSelection.startPos, textSelection.endPos);
            int selEnd   = Math.max(textSelection.startPos, textSelection.endPos);

            // clamp selection to the visible viewport
            int visStart = Math.max(selStart, textViewport);
            int visEnd   = Math.min(selEnd, getTextViewportEnd());

            if (visStart < visEnd) {
                String beforeSel  = rText.substring(0, visStart - textViewport);
                String selInView  = rText.substring(visStart - textViewport, visEnd - textViewport);

                int startX        = textX + Vera.provider.getTextWidth(beforeSel, font);
                int selTextWidth  = Vera.provider.getTextWidth(selInView, font);
                int selTextHeight = Vera.provider.getTextHeight(selInView, font);

                Vera.renderer.drawFill(ctx, startX, textY, selTextWidth, selTextHeight, textSelectionFill);
            }
        }

        // text
        if (text.isEmpty()) Vera.renderer.drawText(ctx, textX, textY, placeholderText, placeholderFont);
        else Vera.renderer.drawText(ctx, textX, textY, rText, font);

        // cursor
        if (isFocused() && textSelection.isClear() && ((System.currentTimeMillis() - timeSinceLastInput) / 500) % 2 == 0) {
            int cursorX = textX + Vera.provider.getTextWidth(
                    rText.substring(0, Math.min(
                            cursorPos - textViewport,
                            rText.length()
                    )), font
            );
            Vera.renderer.drawRect(ctx, cursorX, textY, 1, textHeight, getCursorColorSafe());
        }
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        super.handleBuiltinEvent(event, args);

        if (event.equals(VEvents.Widget.LEFT_CLICK)) {
            textSelection.clear();
            setCursorPos(getCharPosAtX(getRelativeMouseX()));
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.timeSinceLastInput = System.currentTimeMillis();
        events.fire(VEvents.LineInput.CHANGE);
    }

    private String getTextInViewport() {
        return text.substring(textViewport, getTextViewportEnd());
    }

    public long getTimeSinceLastInput() {
        return timeSinceLastInput;
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
        events.register(VEvents.LineInput.CHANGE, runnable);
    }

    public void onCursorMove(Runnable runnable) {
        events.register(VEvents.LineInput.CURSOR_MOVE, runnable);
    }

    public void onCursorMoveLeft(Runnable runnable) {
        events.register(VEvents.LineInput.CURSOR_MOVE_LEFT, runnable);
    }

    public void onCursorMoveRight(Runnable runnable) {
        events.register(VEvents.LineInput.CURSOR_MOVE_RIGHT, runnable);
    }

    public void onAddCharLimited(VCharLimitedEvent runnable) {
        events.register(VEvents.LineInput.ADD_CHAR_LIMITED, args -> runnable.run((char) args[0]));
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
            setCursorPos(Math.max(0, jumpToWordStart(cursorPos)));
            events.fire(VEvents.LineInput.CURSOR_MOVE_LEFT);
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isAltDown() && cursorPos < text.length()) {
            setCursorPos(Math.min(text.length(), jumpToWordEnd(cursorPos)));
            events.fire(VEvents.LineInput.CURSOR_MOVE_LEFT);
        }
        // Handle line navigation
        else if (isDown(GLFW.GLFW_KEY_LEFT) && isCtrlDown()) {
            setCursorPos(0);
            events.fire(VEvents.LineInput.CURSOR_MOVE_LEFT);
        } else if (isDown(GLFW.GLFW_KEY_RIGHT) && isCtrlDown()) {
            setCursorPos(text.length());
            events.fire(VEvents.LineInput.CURSOR_MOVE_RIGHT);
        }
        // Handle character navigation
        else if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPos > 0) {
            setCursorPos(Math.max(0, cursorPos - 1));
            events.fire(VEvents.LineInput.CURSOR_MOVE_LEFT);
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPos < text.length()) {
            setCursorPos(Math.min(text.length(), cursorPos + 1));
            events.fire(VEvents.LineInput.CURSOR_MOVE_RIGHT);
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

        setCursorPos(newPos);
        textSelection.endPos = newPos;
    }

    private void insertText(String insertion) {
        if (maxChars > -1 && text.length() + insertion.length() > maxChars) {
            events.fire(VEvents.LineInput.ADD_CHAR_LIMITED, insertion.charAt(0));
            return;
        }

        String front = text.substring(0, cursorPos);
        String back = text.substring(cursorPos);
        setText(front + insertion + back);
        setCursorPos(cursorPos + insertion.length());
    }

    private void deleteSelectedText() {
        if (textSelection.isClear()) return;

        int start = Math.min(textSelection.startPos, textSelection.endPos);
        int end = Math.max(textSelection.startPos, textSelection.endPos);

        String front = text.substring(0, start);
        String back = text.substring(end);
        setText(front + back);
        setCursorPos(start);
//        setTextViewport(textViewport - (end - start));
        clearTextSelection();
    }

    private void replaceSelectedText(String replacement) {
        if (textSelection.isClear()) return;

        int start = Math.min(textSelection.startPos, textSelection.endPos);
        int end = Math.max(textSelection.startPos, textSelection.endPos);

        if (maxChars > -1 && text.length() - (end - start) + replacement.length() > maxChars) {
            events.fire(VEvents.LineInput.ADD_CHAR_LIMITED, replacement.charAt(0));
            return;
        }

        String front = text.substring(0, start);
        String back = text.substring(end);
        setText(front + replacement + back);
        setCursorPos(start + replacement.length());
//        setTextViewport(textViewport - (end - start));
        clearTextSelection();
    }


    private String getSelectedText() {
        if (textSelection.isClear()) return "";
        int start = Math.min(textSelection.startPos, textSelection.endPos);
        int end = Math.max(textSelection.startPos, textSelection.endPos);
        return text.substring(start, end);
    }

    public void setTextViewport(int textViewport) {
        this.textViewport = MathHelper.clamp(textViewport, 0, text.length());
    }

    public int getTextViewportBegin() {
        return textViewport;
    }

    private int getTextViewportEnd() {
        VStyleState state = createStyleState();
        VFont font = getStyle("font", state);

        StringBuilder buf = new StringBuilder();
        for (int i = textViewport; i < text.length(); i++) {
            buf.append(text.charAt(i));
            if (Vera.provider.getTextWidth(buf.toString(), font) <= width) continue;
            return i;
        }

        return text.length();
    }

    private void updateTextViewport() {
        textViewport = MathHelper.clamp(textViewport, 0, text.length());

        // Cursor is before the viewport: snap left
        if (cursorPos < textViewport) {
            textViewport = cursorPos;
        }

        // cursor is past the viewport end: advance right
        int end;
        while ((end = getTextViewportEnd()) <= cursorPos && end < text.length()) {
            textViewport++;
        }

        // if the end of the text is visible, try scrolling back left
        while (textViewport > 0 && getTextViewportEnd() == text.length()) {
            textViewport--;
        }
        // if that last decrement caused overflow that hides the cursor, undo it
        if (getTextViewportEnd() < text.length() && cursorPos >= getTextViewportEnd()) {
            textViewport++;
        }
    }

    public int getCursorPos() {
        return cursorPos;
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
        this.timeSinceLastInput = System.currentTimeMillis();
        events.fire(VEvents.LineInput.CURSOR_MOVE);
        updateTextViewport();
    }

    public int getCharPosAtX(int x) {
        VStyleState state = createStyleState();
        VFont font = getStyle("font", state);
        V4Int padding = getStyle("padding", state);

        String rText = getTextInViewport();
        int rTextWidth = Vera.provider.getTextWidth(rText, font);
        int textX = Vera.provider.getTextWidth(text, font) < width
                ? padding.get3()
                : padding.get3() - rTextWidth + width;

        if (x <= textX) {
            return textViewport;
        } else {
            int bestPos = rText.length();
            for (int i = 0; i < rText.length(); i++) {
                int charMidX = textX
                        + Vera.provider.getTextWidth(rText.substring(0, i), font)
                        + Vera.provider.getTextWidth(rText.substring(i, i + 1), font) / 2;
                if (x <= charMidX) {
                    bestPos = i;
                    break;
                }
            }
            return textViewport + bestPos;
        }
    }

    public VColor getCursorColorSafe() {
        VStyleState state = createStyleState();

        VColor style = getStyleOrDefault("cursor-color", null, state);
        VFont font = getStyle("font", state);

        return style == null ? font.getColor() : style;
    }

    @Override
    public int getEffectiveWidth() {
        VStyleState state = createStyleState();
        V4Int padding = getStyle("padding", state);

        return width + padding.get3() + padding.get4();
    }

    @Override
    public int getEffectiveHeight() {
        VStyleState state = createStyleState();
        V4Int padding = getStyle("padding", state);

        return height + padding.get1() + padding.get2();
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (!Character.isISOControl(chr)) {
            if (!textSelection.isClear()) {
                // Replace selected text with the typed character
                int start = Math.min(textSelection.startPos, textSelection.endPos);
                int end = Math.max(textSelection.startPos, textSelection.endPos);

                if (maxChars > -1 && text.length() - (end - start) + 1 > maxChars) {
                    events.fire(VEvents.LineInput.ADD_CHAR_LIMITED, chr);
                    return;
                }

                String front = text.substring(0, start);
                String back = text.substring(end);

                setText(front + chr + back);
                setCursorPos(start + 1);
                clearTextSelection();
            } else {
                // Normal character insertion
                if (maxChars > -1 && text.length() >= maxChars) {
                    events.fire(VEvents.LineInput.ADD_CHAR_LIMITED, chr);
                    return;
                }

                String front = text.substring(0, cursorPos);
                String back = text.substring(cursorPos);

                setText(front + chr + back);
                setCursorPos(cursorPos + 1);
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
        setCursorPos(text.length());
    }

    private void deleteText(int start, int end) {
        if (text == null || start < 0 || end > text.length() || start >= end) {
            return;
        }

        StringBuilder builder = new StringBuilder(text);
        builder.delete(start, end);
        setText(builder.toString());
        setCursorPos(Math.min(start, text.length()));
//        setTextViewport(textViewport - (end - start));
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
