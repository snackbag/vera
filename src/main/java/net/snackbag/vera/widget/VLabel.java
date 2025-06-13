package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.modifier.VPaddingWidget;

public class VLabel extends VWidget<VLabel> implements VPaddingWidget {
    private String text;
    private VFont font;
    private V4Int padding;
    private VAlignmentFlag alignment;

    public VLabel(String text, VeraApp app) {
        super(0, 0, 100, 16, app);

        this.text = text;
        this.font = VFont.create();
        this.padding = new V4Int(4);
        this.focusOnClick = false;
        alignment = VAlignmentFlag.LEFT;

        setStyle("background-color", VColor.transparent());
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

    public VAlignmentFlag getAlignment() {
        return alignment;
    }

    public void setAlignment(VAlignmentFlag alignment) {
        this.alignment = alignment;
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
                getHitboxX(),
                getHitboxY(),
                getHitboxWidth(),
                getHitboxHeight(),
                rotation,
                getStyle("background-color")
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
