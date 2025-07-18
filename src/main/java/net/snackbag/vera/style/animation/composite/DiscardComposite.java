package net.snackbag.vera.style.animation.composite;

public class DiscardComposite extends Composite {
    @Override
    public <T> T apply(Context<T> ctx) {
        return ctx.in();
    }
}
