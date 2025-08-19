package net.snackbag.vera.event;

import net.snackbag.vera.VElement;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventHandler {
    public final VElement element;

    public @Nullable EventHandler.Processor preprocessor; // called before event stack execution
    public @Nullable EventHandler.Processor postprocessor; // called after event stack execution

    private final HashMap<String, List<VEvent>> executors = new HashMap<>();

    public EventHandler(VElement element) {
        this.element = element;
    }

    public void fire(String name, Object... args) {
        if (preprocessor != null) preprocessor.call(name, args);

        if (!executors.containsKey(name)) {
            doPostProcessor(name, args);
            return;
        }

        executors.get(name).parallelStream().forEach(e -> e.run(args));
        doPostProcessor(name, args);
    }

    private void doPostProcessor(String name, Object... args) {
        if (postprocessor != null) postprocessor.call(name, args);
    }

    public void register(String name, VEvent executor) {
        executors.computeIfAbsent(name, k -> new ArrayList<>()).add(executor);
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
        void call(String name, Object[] args);
    }
}
