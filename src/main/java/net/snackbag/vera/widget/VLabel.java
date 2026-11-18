package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.flag.VHAlignmentFlag;
import net.snackbag.vera.core.VRenderContext;

public class VLabel extends VWidget<VLabel> {
    private String text;
    private VHAlignmentFlag alignment;

    public VLabel(VAppAccess app, String text, int x, int y, int width, int height) {
        super(app, x, y, width, height);

        this.text = text;
        this.focusOnClick = false;
        alignment = VHAlignmentFlag.LEFT;
    }

    public VLabel(VAppAccess app, String text, int x, int y) {
        this(app, text, x, y, 100, 16);
        adjustSize();
    }

    public VLabel(VAppAccess app, String text) {
        this(app, text, 0, 0, 100, 16);
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
    public void renderContent(VRenderContext ctx) {
        var state = createStyleState();

        VFont font = getStyle("font", state);
        VFill background = getStyle("background", state);
        V4Int padding = getStyle("padding", state);

        if (background.isVisible()) {
            Vera.renderer.drawFill(
                    ctx,
                    0,
                    0,
                    getEffectiveWidth(),
                    getEffectiveHeight(),
                    background
            );
        }

        int usualX = padding.get3();
        int usualY = padding.get1();

        switch (alignment) {
            case LEFT -> Vera.renderer.drawText(ctx, usualX, usualY, text, font);
            case CENTER -> Vera.renderer.drawText(ctx, getEffectiveWidth() / 2 - Vera.provider.getTextWidth(text, font) / 2, usualY, text, font);
            case RIGHT -> Vera.renderer.drawText(ctx, getEffectiveWidth() - padding.get4() - Vera.provider.getTextWidth(text, font), usualY, text, font);
        }
    }
}
