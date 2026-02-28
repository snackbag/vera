package net.snackbag.vera.style.standard;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.core.VCursorShape;
import net.snackbag.vera.core.VImage;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.widget.VCheckBox;

public class CheckBoxStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        sheet.setKey(VCheckBox.class, "cursor", VCursorShape.POINTING_HAND, VStyleState.HOVERED);
        sheet.setKey(VCheckBox.class, "fill", new VImage(Identifier.of(MinecraftVera.MOD_ID, "widgets/checkmark/default.png")));
        sheet.setKey(VCheckBox.class, "fill-checked", new VImage(Identifier.of(MinecraftVera.MOD_ID, "widgets/checkmark/checked.png")));
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        sheet.reserveType("fill", StyleValueType.FILL);
        sheet.reserveType("fill-checked", StyleValueType.FILL);
    }
}
