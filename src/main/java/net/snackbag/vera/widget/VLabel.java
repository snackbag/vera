package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.V4Int;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.modifier.VPaddingWidget;

public class VLabel extends VWidget<VLabel> implements VPaddingWidget {
    private String text;
    private VFont font;
    private VColor backgroundColor;
    private V4Int padding;

    public VLabel(String text, VeraApp app) {
        super(0, 0, 100, 16, app);

        this.text = text;
        this.font = VFont.create();
        this.backgroundColor = VColor.transparent();
        this.padding = new V4Int(4);
        this.focusOnClick = false;
    }

    public String getText() {
        return text;
    }

    public VFont getFont() {
        return font;
    }

    public void setFont(VFont font) {
        this.font = font;
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
    public V4Int getPadding() {
        return padding;
    }

    @Override
    public void setPadding(V4Int padding) {
        this.padding = padding;
    }

    public VFont.FontModifier modifyFont() {
        return new VFont.FontModifier(font, this::setFont);
    }

    public VColor.ColorModifier modifyFontColor() {
        return new VColor.ColorModifier(font.getColor(), (color) -> setFont(font.withColor(color)));
    }

    @Override
    public int getHitboxWidth() {
        return width + padding.get3() + padding.get4();
    }

    @Override
    public int getHitboxHeight() {
        return height + padding.get1() + padding.get2();
    }

    @Override
    public int getHitboxX() {
        return x - padding.get4();
    }

    @Override
    public int getHitboxY() {
        return y - padding.get1();
    }

    public void adjustSize() {
        this.width = Vera.provider.getTextWidth(text, font);
        this.height = Vera.provider.getTextHeight(text, font);
    }

    @Override
    public void render() {
        VeraApp app = getApp();

        Vera.renderer.drawRect(
                app,
                getHitboxX() + app.getX(),
                getHitboxY() + app.getY(),
                getHitboxWidth(),
                getHitboxHeight(),
                rotation,
                backgroundColor
        );
        Vera.renderer.drawText(app, x + app.getX(), y + app.getY(), rotation, text, font);
    }
}
