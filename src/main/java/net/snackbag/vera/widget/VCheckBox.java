package net.snackbag.vera.widget;

import net.snackbag.vera.Vera;
import net.snackbag.vera.core.*;
import net.snackbag.vera.event.VCheckBoxEvent;
import net.snackbag.vera.event.VEventContext;
import net.snackbag.vera.event.VEvents;
import net.snackbag.vera.style.VEffectState;

import java.util.function.Consumer;

public class VCheckBox extends VWidget<VCheckBox> {
    private boolean checked;

    public VCheckBox(VAppAccess app) {
        this(app, 15, 15);
    }

    public VCheckBox(VAppAccess app, int width, int height) {
        super(app, 0, 0, width, height);

        this.checked = false;
    }

    @Override
    public void renderContent(VRenderContext ctx) {
        var state = createStyleState();
        VFill fill = checked ? getStyle("fill-checked", state) : getStyle("fill", state);

        Vera.renderer.drawFill(ctx, 0, 0, width, height, fill);
    }

    @Override
    public void handleBuiltinEvent(String event, VEventContext ctx) {
        super.handleBuiltinEvent(event, ctx);

        if (event.equals(VEvents.Widget.LEFT_CLICK)) setChecked(!checked);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        events.fire(new VCheckBoxEvent.StateChanged(checked));
    }

    public void onCheckStateChanged(Consumer<VCheckBoxEvent.StateChanged> ctx) {
        events.register(VEvents.CheckBox.CHECK_STATE_CHANGED, ctx);
    }
}
