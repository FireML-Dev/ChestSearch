package uk.firedev.plugintemplate;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class PluginTemplate extends JavaPlugin {

    private static PluginTemplate INSTANCE;

    public PluginTemplate() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException(getClass().getName() + " has already been assigned!");
        }
        INSTANCE = this;
    }

    public static @NotNull PluginTemplate getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(PluginTemplate.class.getSimpleName() + " has not been assigned!");
        }
        return INSTANCE;
    }

    @Override
    public void onLoad() {}

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            // Register Brigadier commands here.
            // commands.registrar().register(new MyCommand().get());
        });
    }

}
