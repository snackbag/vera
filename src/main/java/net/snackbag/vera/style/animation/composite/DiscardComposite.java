package net.snackbag.vera.style.animation.composite;

public class DiscardComposite extends Composite {
    @Override
    public <T> T applyStyle(Context<T> ctx, T in, boolean isNewFrame) {
        return in;
    }
}
