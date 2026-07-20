package uk.firedev.chestsearch.config;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.ChestSearch;
import uk.firedev.messagelib.config.PaperConfigLoader;
import uk.firedev.messagelib.message.ComponentMessage;

public class MainConfig {

    private static final MainConfig INSTANCE = new MainConfig();

    private final ChestSearch plugin = ChestSearch.getInstance();

    private PaperConfigLoader messageLoader;
    private ComponentMessage prefix;

    private MainConfig() {}

    public static @NotNull MainConfig getInstance() {
        return INSTANCE;
    }

    public void load() {
        plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        this.messageLoader = new PaperConfigLoader(plugin.getConfig());
        this.prefix = ComponentMessage.componentMessage(messageLoader, "messages.prefix", "<aqua>[ChestSearch]");
    }

    public @NotNull FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public int getSearchRange() {
        int config = getConfig().getInt("search.range", 5);
        return Math.max(1, config);
    }

    public int getGlowCount() {
        int config = getConfig().getInt("search.glow-count", 5);
        return Math.max(1, config);
    }

    public @NotNull ComponentMessage getReloadedMessage() {
        return getMessage("messages.reloaded");
    }

    public @NotNull ComponentMessage getNoMatchesMessage(@NotNull Component value) {
        return getMessage("messages.no-matches")
            .replace("{value}", value)
            .replace("{range}", getSearchRange());
    }

    public @NotNull ComponentMessage getFoundMatchesMessage(boolean partial, int amount, @NotNull Component value) {
        String path = partial ? "messages.found-partial-matches" : "messages.found-exact-matches";
        return getMessage(path)
            .replace("{value}", value)
            .replace("{amount}", amount);
    }

    private @NotNull ComponentMessage getMessage(@NotNull String path) {
        return ComponentMessage.componentMessage(messageLoader, path).replace("{prefix}", this.prefix);
    }

}
