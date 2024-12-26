package net.snackbag.vera.widget;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.Vera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VeraApp;
import net.snackbag.vera.event.VCheckedStateChange;
import org.jetbrains.annotations.Nullable;

public class VCheckBox extends VWidget<VCheckBox> {
    private boolean checked;

    private Identifier checkedTexture;
    private Identifier defaultTexture;
    private VColor hoverOverlayColor;

    private @Nullable Identifier checkedHoverTexture;
    private @Nullable Identifier defaultHoverTexture;

    public VCheckBox(VeraApp app) {
        super(0, 0, 15, 15, app);

        this.checked = false;

        this.checkedTexture = new Identifier(MinecraftVera.MOD_ID, "widgets/checkmark/checked.png");
        this.defaultTexture = new Identifier(MinecraftVera.MOD_ID, "widgets/checkmark/default.png");
        this.hoverOverlayColor = VColor.transparent();
    }

    @Override
    public void render() {
        Vera.renderer.drawImage(app, x, y, width, height, 0, getCurrentTexture());
        if (isHovered()) Vera.renderer.drawRect(app, x, y, width, height, 0, hoverOverlayColor);
    }

    @Override
    public void handleBuiltinEvent(String event, Object... args) {
        if (event.equals("left-click")) setChecked(!checked);
        super.handleBuiltinEvent(event, args);
    }

    public Identifier getCurrentTexture() {
        if (isHovered() && isChecked()) return getCheckedHoverTexture();
        else if (isHovered()) return getDefaultHoverTexture();
        else if (isChecked()) return getCheckedTexture();
        else return getDefaultTexture();
    }

    public Identifier getCheckedTexture() {
        return checkedTexture;
    }

    public void setCheckedTexture(Identifier checkedTexture) {
        this.checkedTexture = checkedTexture;
    }

    public void setDefaultTexture(Identifier defaultTexture) {
        this.defaultTexture = defaultTexture;
    }

    public Identifier getDefaultTexture() {
        return defaultTexture;
    }

    public Identifier getCheckedHoverTexture() {
        return checkedHoverTexture != null ? checkedHoverTexture : checkedTexture;
    }

    public void setCheckedHoverTexture(@Nullable Identifier checkedHoverTexture) {
        this.checkedHoverTexture = checkedHoverTexture;
    }

    public Identifier getDefaultHoverTexture() {
        return defaultHoverTexture != null ? defaultHoverTexture : defaultTexture;
    }

    public void setDefaultHoverTexture(@Nullable Identifier defaultHoverTexture) {
        this.defaultHoverTexture = defaultHoverTexture;
    }

    public VColor getHoverOverlayColor() {
        return hoverOverlayColor;
    }

    public void setHoverOverlayColor(VColor hoverOverlayColor) {
        this.hoverOverlayColor = hoverOverlayColor;
    }

    public VColor.ColorModifier modifyHoverOverlayColor() {
        return new VColor.ColorModifier(hoverOverlayColor, this::setHoverOverlayColor);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        fireEvent("vcheckbox-checked", checked);
    }

    public void onCheckStateChange(VCheckedStateChange runnable) {
        registerEventExecutor("vcheckbox-checked", args -> runnable.run((boolean) args[0]));
    }
}
