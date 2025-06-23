package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VCheckedStateChange;
import net.snackbag.vera.style.StyleState;

public class VCheckBox extends VWidget<VCheckBox> {
    private boolean checked;

    public VCheckBox(VeraApp app) {
        this(
                app,
                new Identifier(MinecraftVera.MOD_ID, "widgets/checkmark/default.png"),
                new Identifier(MinecraftVera.MOD_ID, "widgets/checkmark/checked.png")
        );
    }

    public VCheckBox(VeraApp app, Identifier defaultTexture, Identifier checkedTexture) {
        this(app, defaultTexture, checkedTexture, 15, 15);
    }

    public VCheckBox(VeraApp app, Identifier defaultTexture, Identifier checkedTexture, int width, int height) {
        super(0, 0, width, height, app);

        this.checked = false;

        setStyle("src", defaultTexture);
        setStyle("src-checked", checkedTexture);
        setStyle("overlay", VColor.transparent());

        setStyle("cursor", StyleState.HOVERED, VCursorShape.POINTING_HAND);
    }

    @Override
    public void render() {
        StyleState state = createStyleState();
        Identifier texture = checked ? getStyle("src-checked", state) : getStyle("src", state);

        Vera.renderer.drawImage(app, x, y, width, height, 0, texture);
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        if (event.equals("left-click")) setChecked(!checked);
        super.handleBuiltinEvent(event, args);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        events.fireEvent("vcheckbox-checked", checked);
    }

    public void onCheckStateChange(VCheckedStateChange runnable) {
        events.register("vcheckbox-checked", args -> runnable.run((boolean) args[0]));
    }
}
