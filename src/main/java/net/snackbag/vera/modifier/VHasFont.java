package net.snackbag.vera.modifier;

import net.snackbag.vera.core.VColor;
import net.snackbag.vera.core.VFont;
import net.snackbag.vera.style.VEffectState;
import net.snackbag.vera.widget.VWidget;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.ScheduledForRemoval(inVersion = "2.1")
@Deprecated(since = "2.0")
public interface VHasFont extends VModifier {
    default VFont.FontModifier modifyFont() {
        return modifyFont(null);
    }

    default VFont.FontModifier modifyFont(@Nullable VEffectState state) {
        return getApp().styleSheet.modifyKeyAsFont((VWidget<?>) this, "font", state);
    }

    default VColor.ColorModifier modifyFontColor() {
        return modifyFontColor(null);
    }

    default VColor.ColorModifier modifyFontColor(@Nullable VEffectState state) {
        return getApp().styleSheet.modifyKeyAsFontColor((VWidget<?>) this, "font", state);
    }
}
