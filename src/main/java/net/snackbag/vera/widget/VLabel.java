package net.snackbag.vera.widget;

import net.snackbag.vera.core.VeraApp;

public class VLabel extends VWidget {
    protected String text;
    protected int fontSize;

    public VLabel(String text, VeraApp app) {
        super(0, 0, 100, 12, app);

        this.text = text;
        this.fontSize = 16;
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

    @Override
    public void render() {
        getProvider().renderLabel(this);
    }
}
