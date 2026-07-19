package uk.firedev.chestsearch.config;

import org.jetbrains.annotations.NotNull;

public class MainConfig {

    private static final MainConfig INSTANCE = new MainConfig();

    private MainConfig() {

    }

    public static @NotNull MainConfig getInstance() {
        return INSTANCE;
    }

    public int getSearchRange() {
        return 5; // TODO make this configurable.
    }

    public int getGlowDuration() {
        return 5; // TODO make this configurable.
    }

}
