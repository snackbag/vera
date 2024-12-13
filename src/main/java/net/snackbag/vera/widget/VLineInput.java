package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import org.lwjgl.glfw.GLFW;

public class VLineInput extends VWidget {
    private VFont font;
    private String text;

    public VLineInput(VeraApp app) {
        super(0, 0, 100, 20, app);
        text = "";
    }

    @Override
    public void render() {
        Vera.renderer.drawText(app, x, y, 0, text, font);
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
            text = text.substring(0, text.length() - 1);
            fireEvent("vline-change");
        }

        super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (!Character.isISOControl(chr)) {
            text += chr;
            fireEvent("vline-change");
        }
        super.charTyped(chr, modifiers);
    }
}
