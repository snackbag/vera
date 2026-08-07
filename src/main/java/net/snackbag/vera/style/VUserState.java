package net.snackbag.vera.style;

public enum VUserState {
    ACTIVE,
    SELECTED,
    DISABLED;

    public VStyleState asStyleState() {
        return new VStyleState(this);
    }
}
