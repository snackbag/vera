package net.snackbag.vera;

import net.minecraft.util.Identifier;
import net.snackbag.mcvera.MinecraftVera;
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

    public static Identifier id(String path) {
        return new Identifier(MinecraftVera.MOD_ID, path);
    }
}
