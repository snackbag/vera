package net.snackbag.vera.widget;

import net.snackbag.vera.VeraRenderer;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;

public class VLabel extends VWidget {
    private String text;
    private int fontSize;
    private VColor backgroundColor;

    public VLabel(String text, VeraApp app) {
        super(0, 0, 100, 12, app);

        this.text = text;
        this.fontSize = 16;
        this.backgroundColor = VColor.transparent();
    }

    public String getText() {
        return text;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public VColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(VColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        VeraRenderer renderer = getRenderer();
    }
}
