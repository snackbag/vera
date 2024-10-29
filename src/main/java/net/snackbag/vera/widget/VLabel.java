package net.snackbag.vera.widget;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;

public class VLabel extends VWidget {
    protected String text;
    protected int fontSize;
    protected VColor backgroundColor;

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

    @Override
    public void render() {
        getProvider().renderLabel(this);
    }
}
