package com.example.demo;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VShortcut;
import net.snackbag.vera.flag.VLayoutAlignmentFlag;
import net.snackbag.vera.layout.VHLayout;
import net.snackbag.vera.layout.VVLayout;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.widget.VLabel;

public class DemoApplication extends VeraApp {
    private int clicks = 0;
    private VHLayout layout;
    private VVLayout centerLayout;

    @Override
    public void init() {
        new VShortcut(this, "escape", this::hide);
        setBackgroundColor(VColor.black().withOpacity(0.2f));

        layout = new VHLayout(this, 0, 0);
        layout.alignment = VLayoutAlignmentFlag.CENTER;

        centerLayout = new VVLayout(layout);
        centerLayout.alignment = VLayoutAlignmentFlag.CENTER;

        VLabel label = new VLabel("Not clicked yet", this).alsoAddTo(centerLayout);
        label.modifyFontColor().rgb(VColor.white());

        VLabel button = new VLabel("Click me", this).alsoAddTo(centerLayout);

        button.modifyFontColor().rgb(VColor.of(95, 180, 0));
        button.setStyle("background-color", VColor.white());
        button.setStyle("padding", 4);
        button.setStyle("overlay", VStyleState.HOVERED, VColor.white().withOpacity(0.5f));
        button.setStyle("border-size", 1);
        button.setStyle("border-color", VColor.of(95, 180, 0));
        button.setStyle("cursor", VCursorShape.POINTING_HAND);

        button.onLeftClick(() -> {
            clicks++;
            label.setText("Clicks: " + clicks);
            label.adjustSize();
        });

        new VShortcut(this, "leftctrl+d", () -> {
            System.out.println("Debug hit");
        });
    }

    @Override
    public void update() {
        super.update();

        setWidth(Vera.provider.getScreenWidth());
        setHeight(Vera.provider.getScreenHeight());

        layout.setSize(getWidth(), getHeight());
        centerLayout.setHeight(getHeight());
    }
}
