package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import org.lwjgl.glfw.GLFW;

public class VLineInput extends VWidget {
    private VFont font;
    private String text;
    private int cursorPos = 0;

    public VLineInput(VeraApp app) {
        super(0, 0, 100, 20, app);
        text = "";
        font = new VFont(Vera.provider.getDefaultFontName(), 16, VColor.black());
    }

    @Override
    public void render() {
        Vera.renderer.drawText(app, x, y, 0, text, font);
        Vera.renderer.drawRect(app,
                x + Vera.provider.getTextWidth(text.substring(0, cursorPos), font), y,
                1, Vera.provider.getTextHeight(text, font), 0,
                VColor.white());
    }

    public VFont getFont() {
        return font;
    }

    public void setFont(VFont font) {
        this.font = font;
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

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !text.isEmpty()) {
            StringBuilder builder = new StringBuilder(text);
            builder.deleteCharAt(cursorPos - 1);

            cursorPos -= 1;
            text = builder.toString();
            fireEvent("vline-change");
        } else if (keyCode == GLFW.GLFW_KEY_LEFT && cursorPos > 0) {
            cursorPos -= 1;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT && cursorPos < text.length()) {
            cursorPos += 1;
        }

        super.keyPressed(keyCode, scanCode, modifiers);
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
}
