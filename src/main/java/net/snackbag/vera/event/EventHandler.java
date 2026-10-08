package net.snackbag.vera.event;

import net.snackbag.vera.VElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler {
    public final VElement element;

    public @Nullable EventHandler.Processor preprocessor; // called before event stack execution
    public @Nullable EventHandler.Processor postprocessor; // called after event stack execution

    private final HashMap<String, List<Consumer<VEventContext>>> executors = new HashMap<>();

    public EventHandler(VElement element) {
        this.element = element;
    }

    public void fire(String name) {
        fire(() -> name);
    }

    public void fire(VEventContext ctx) {
        String name = ctx.eventName();

        if (element.appAccess.isDelegated()) {
            element.appAccess.getDelegator().getDelegatedEventHandler().fire(ctx);
        }

        if (preprocessor != null) preprocessor.call(name, ctx);

        if (!executors.containsKey(name)) {
            doPostProcessor(name, ctx);
            return;
        }

        executors.get(name).parallelStream().forEach(e -> e.accept(ctx));
        doPostProcessor(name, ctx);
    }

    private void doPostProcessor(String name, VEventContext ctx) {
        if (postprocessor != null) postprocessor.call(name, ctx);
    }

    public <T extends VEventContext> void register(String name, Consumer<T> executor) {
        executors.computeIfAbsent(name, k -> new ArrayList<>())
                .add(ctx -> executor.accept((T) ctx));
    }

    public void register(String name, Runnable executor) {
        register(name, e -> executor.run());
    }

    public void clear() {
        executors.clear();
    }

    public void clear(String name) {
        executors.remove(name);
    }

    @FunctionalInterface
    public interface Processor {
        void call(String name, VEventContext ctx);
    }
}
