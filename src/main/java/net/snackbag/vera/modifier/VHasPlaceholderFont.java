package net.snackbag.vera.modifier;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.StyleState;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.Nullable;

public interface VHasPlaceholderFont extends VModifier {
    default VFont.FontModifier modifyPlaceholderFont() {
        return modifyPlaceholderFont(null);
    }

    default VFont.FontModifier modifyPlaceholderFont(@Nullable StyleState state) {
        return getApp().styleSheet.modifyKeyAsFont((VWidget<?>) this, "placeholder-font", state);
    }

    default VColor.ColorModifier modifyPlaceholderFontColor() {
        return modifyPlaceholderFontColor(null);
    }

    default VColor.ColorModifier modifyPlaceholderFontColor(@Nullable StyleState state) {
        return getApp().styleSheet.modifyKeyAsFontColor((VWidget<?>) this, "placeholder-font", state);
    }
}
