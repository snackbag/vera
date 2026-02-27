package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VFill;
import net.snackbag.vera.core.VImage;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.event.VCheckedStateChange;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.core.VRenderContext;

public class VCheckBox extends VWidget<VCheckBox> {
    private boolean checked;

    public VCheckBox(VeraApp app) {
        this(app, 15, 15);
    }

    public VCheckBox(VeraApp app, int width, int height) {
        super(0, 0, width, height, app);

        this.checked = false;
    }

    @Override
    public void renderContent(VRenderContext ctx) {
        VStyleState state = createStyleState();
        VFill fill = checked ? getStyle("fill-checked", state) : getStyle("fill", state);

        Vera.renderer.drawFill(ctx, 0, 0, width, height, fill);
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
