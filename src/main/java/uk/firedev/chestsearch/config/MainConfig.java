package uk.firedev.chestsearch.config;

import org.jetbrains.annotations.NotNull;

public class MainConfig {

    private static final MainConfig INSTANCE = new MainConfig();

    private MainConfig() {

    }

    public static @NotNull MainConfig getInstance() {
        return INSTANCE;
    }

    /**
     * The y level range to search in. 0 or below means it is disabled.
     */
    public int getSearchRange() {
        return Math.max(1, 5); // TODO make this configurable.
    }

}
