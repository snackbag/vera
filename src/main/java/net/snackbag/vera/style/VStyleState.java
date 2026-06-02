package net.snackbag.vera.style;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VStyleState {
    public final @Nullable VUserState userState;
    public final @NotNull VEffectState effectState;

    // for fallback
    private @NotNull VStyleState origin = this;

    public VStyleState() {
        this(VEffectState.DEFAULT);
    }

    public VStyleState(@NotNull VEffectState effectState) {
        this(effectState, null);
    }

    public VStyleState(@NotNull VEffectState effectState, @Nullable VUserState userState) {
        this.effectState = effectState;
        this.userState = userState;
    }

    private VStyleState(@NotNull VEffectState effectState, @Nullable VUserState userState, @NotNull VStyleState origin) {
        this.effectState = effectState;
        this.userState = userState;
        this.origin = origin;
    }

    public VStyleState fallback() {
        VEffectState effectFallback = effectState.fallback == null ? VEffectState.DEFAULT : effectState.fallback;

        if (userState == null) return new VStyleState(effectFallback, origin.userState, this);
        else return new VStyleState(effectState, null, this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VStyleState that)) return false;
        return userState == that.userState && effectState == that.effectState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userState, effectState);
    }
}
