package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.modifier.VPaddingWidget;
import net.snackbag.vera.style.StyleState;

public class VLabel extends VWidget<VLabel> implements VPaddingWidget {
    private String text;
    private V4Int padding;
    private VAlignmentFlag alignment;

    public VLabel(String text, VeraApp app) {
        super(0, 0, 100, 16, app);

        this.text = text;
        this.padding = new V4Int(4);
        this.focusOnClick = false;
        alignment = VAlignmentFlag.LEFT;

        setStyle("background-color", VColor.transparent());
        setStyle("font", VFont.create());

        adjustSize();
    }

    public String getText() {
        return text;
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
        VFont font = getStyle("font", createStyleState());

        this.width = Vera.provider.getTextWidth(text, font);
        this.height = Vera.provider.getTextHeight(text, font);
    }

    public VColor.ColorModifier modifyColor(String key) {
        return app.styleSheet.modifyKeyAsColor(this, key);
    }

    public VFont.FontModifier modifyFont(String key) {
        return app.styleSheet.modifyKeyAsFont(this, key);
    }

    public VColor.ColorModifier modifyFontColor(String key) {
        // TODO: More elegant solution
        return new VColor.ColorModifier(((VFont) app.styleSheet.getKey(this, key)).getColor(), color -> app.styleSheet.modifyKeyAsFont(this, key).color(color));
    }

    @Override
    public void render() {
        StyleState state = createStyleState();
        VFont font = getStyle("font", state);
        VColor backgroundColor = getStyle("background-color", state);

        Vera.renderer.drawRect(
                app,
                getHitboxX(),
                getHitboxY(),
                getHitboxWidth(),
                getHitboxHeight(),
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
