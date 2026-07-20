package uk.firedev.chestsearch;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.command.MainCommand;
import uk.firedev.chestsearch.config.MainConfig;
import uk.firedev.chestsearch.search.ParticleDisplay;
import uk.firedev.messagelib.MessageLibSettings;

public final class ChestSearch extends JavaPlugin {

    private static ChestSearch INSTANCE;

    public ChestSearch() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getName() + " has already been assigned!");
        }
        INSTANCE = this;
    }

    public static @NotNull ChestSearch getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(ChestSearch.class.getSimpleName() + " has not been assigned!");
        }
        return INSTANCE;
    }

    @Override
    public void onLoad() {
        registerCommands();
        MainConfig.getInstance().load();
    }

    @Override
    public void onEnable() {
        MessageLibSettings.get().setEnableLegacy(false);
    }

    @Override
    public void onDisable() {
        ParticleDisplay.TASK.cancel();
    }

    public void reload() {
        MainConfig.getInstance().reload();
    }

    @SuppressWarnings("UnstableApiUsage")
    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MainCommand.get());
        });
    }

}
