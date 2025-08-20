package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.flag.VHAlignmentFlag;
import net.snackbag.vera.modifier.VHasFont;
import net.snackbag.vera.style.StyleState;

public class VLabel extends VWidget<VLabel> implements VHasFont {
    private String text;
    private VHAlignmentFlag alignment;

    public VLabel(String text, int x, int y, int width, int height, VeraApp app) {
        super(x, y, width, height, app);

        this.text = text;
        this.focusOnClick = false;
        alignment = VHAlignmentFlag.LEFT;
    }

    public VLabel(String text, VeraApp app) {
        this(text, 0, 0, 100, 16, app);

        adjustSize();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getEffectiveWidth() {
        V4Int padding = getStyle("padding", createStyleState());
        return width + padding.get3() + padding.get4();
    }

    @Override
    public int getEffectiveHeight() {
        V4Int padding = getStyle("padding", createStyleState());
        return height + padding.get1() + padding.get2();
    }

    @Override
    public int getEffectiveX() {
        V4Int padding = getStyle("padding", createStyleState());
        return getX() - padding.get4();
    }

    @Override
    public int getEffectiveY() {
        V4Int padding = getStyle("padding", createStyleState());
        return getY() - padding.get1();
    }

    public VHAlignmentFlag getAlignment() {
        return alignment;
    }

    public void setAlignment(VHAlignmentFlag alignment) {
        this.alignment = alignment;
    }

    public void adjustSize() {
        VFont font = getStyle("font", createStyleState());

        this.width = Vera.provider.getTextWidth(text, font);
        this.height = Vera.provider.getTextHeight(text, font);
    }

    @Override
    public VeraApp getApp() {
        return app;
    }

    public VColor.ColorModifier modifyColor(String key) {
        return app.styleSheet.modifyKeyAsColor(this, key);
    }

    @Override
    public void render() {
        StyleState state = createStyleState();

        VFont font = getStyle("font", state);
        VColor backgroundColor = getStyle("background-color", state);
        V4Int padding = getStyle("padding", state);

        int x = getX();
        int y = getY();

        Vera.renderer.drawRect(
                app,
                getEffectiveX(),
                getEffectiveY(),
                getEffectiveWidth(),
                getEffectiveHeight(),
                rotation,
                backgroundColor
        );

        switch (alignment) {
            case LEFT -> Vera.renderer.drawText(app, x, y, rotation, text, font);
            case CENTER -> {
                int textWidth = Vera.provider.getTextWidth(text, font);
                int centerX = getHitboxX() + (getHitboxWidth() - textWidth) / 2;
                Vera.renderer.drawText(app, centerX, y, rotation, text, font);
            }
            case RIGHT -> Vera.renderer.drawText(app, getHitboxX() + getHitboxWidth() - padding.get4() - Vera.provider.getTextWidth(text, font), y, rotation, text, font);
        }
    }
}
