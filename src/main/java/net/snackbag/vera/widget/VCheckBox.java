package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VCheckedStateChange;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.core.VRenderContext;

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
    }

    @Override
    public void render(VRenderContext ctx) {
        VStyleState state = createStyleState();
        Identifier texture = checked ? getStyle("src-checked", state) : getStyle("src", state);

        Vera.renderer.drawImage(ctx, 0, 0, width, height, texture);
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        super.handleBuiltinEvent(event, args);

        if (event.equals(VEvents.Widget.LEFT_CLICK)) setChecked(!checked);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        events.fire(VEvents.CheckBox.CHECK_STATE_CHANGED, checked);
    }

    public void onCheckStateChange(VCheckedStateChange runnable) {
        events.register(VEvents.CheckBox.CHECK_STATE_CHANGED, args -> runnable.run((boolean) args[0]));
    }
}
