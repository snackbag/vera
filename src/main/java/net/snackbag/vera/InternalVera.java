package net.snackbag.vera;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public class InternalVera {
    public static List<Runnable> getScheduledTasks() {
        return Vera.nextFrameTasks;
    }

    public static void clearScheduledTasks() {
        Vera.nextFrameTasks.clear();
    }
}
