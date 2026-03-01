package net.snackbag.vera.style.standard;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.core.VImage;
import net.snackbag.vera.core.v4.V4Int;
import net.snackbag.vera.style.StyleValueType;
import net.snackbag.vera.style.VStyleSheet;
import net.snackbag.vera.style.VStyleState;
import net.snackbag.vera.widget.VComboBox;

public class ComboBoxStandardStyle implements VStandardStyle {
    @Override
    public void apply(VStyleSheet sheet) {
        // Normal
        sheet.setKey(VComboBox.class, "padding", new V4Int(4));
        sheet.setKey(VComboBox.class, "background", VColor.white());
        sheet.setKey(VComboBox.class, "arrow", new VImage(new Identifier(MinecraftVera.MOD_ID, "widgets/combobox_arrow.png")));
        sheet.setKey(VComboBox.class, "font", VFont.create());

        // Items
        sheet.setKey(VComboBox.Item.class, "ci-height", 16);
        sheet.setKey(VComboBox.Item.class, "ci-background", VColor.white(), VStyleState.DEFAULT);
        sheet.setKey(VComboBox.Item.class, "ci-background", VColor.black(), VStyleState.HOVERED);
        sheet.setKey(VComboBox.Item.class, "ci-font", VFont.create(), VStyleState.DEFAULT);
        sheet.setKey(VComboBox.Item.class, "ci-font", VFont.create().withColor(VColor.white()), VStyleState.HOVERED);
        sheet.setKey(VComboBox.Item.class, "ci-reserve-icon-space", true);
    }

    @Override
    public void reserve(VStyleSheet sheet) {
        // Normal
        sheet.reserveType("padding", StyleValueType.V4INT);
        sheet.reserveType("background", StyleValueType.FILL);
        sheet.reserveType("arrow", StyleValueType.FILL);
        sheet.reserveType("font", StyleValueType.FONT);

        // Items
        sheet.reserveType("ci-height", StyleValueType.INT);
        sheet.reserveType("ci-background", StyleValueType.FILL);

        sheet.reserveType("ci-icon", StyleValueType.FILL);
        sheet.reserveType("ci-reserve-icon-space", StyleValueType.BOOLEAN);
        sheet.reserveType("ci-font", StyleValueType.FONT);
    }
}
