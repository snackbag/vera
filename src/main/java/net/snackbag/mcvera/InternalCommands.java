package net.snackbag.mcvera;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.snackbag.mcvera.test.LayoutTestApplication;
import net.snackbag.mcvera.test.StyleTestApplication;
import net.snackbag.mcvera.test.TestApplication;

public class InternalCommands {
    public static void register(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            CommandRegistryAccess ra
    ) {
        if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;

        dispatcher.register(
                ClientCommandManager.literal("vera")
                        .then(ClientCommandManager.literal("test")
                            .then(ClientCommandManager.literal("generic").executes((ctx) -> {
                                TestApplication.INSTANCE.show();
                                return 1;
                            }))
                            .then(ClientCommandManager.literal("styles").executes((ctx) -> {
                                StyleTestApplication.INSTANCE.show();
                                return 1;
                            }))
                            .then(ClientCommandManager.literal("layout").executes((ctx) -> {
                                LayoutTestApplication.INSTANCE.show();
                                return 1;
                            }))
                        )
                        .then(ClientCommandManager.literal("clear-tests")
                                .executes((ctx) -> {
                                    TestApplication.INSTANCE = new TestApplication();
                                    StyleTestApplication.INSTANCE = new StyleTestApplication();
                                    LayoutTestApplication.INSTANCE = new LayoutTestApplication();
                                    return 1;
                                })
                        )
        );
    }
}
