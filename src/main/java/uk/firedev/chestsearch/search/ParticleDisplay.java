package uk.firedev.chestsearch.search;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.firedev.chestsearch.ChestSearch;
import uk.firedev.chestsearch.config.MainConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleDisplay {

    private static final ParticleBuilder particleBuilder = new ParticleBuilder(Particle.DUST)
        .color(Color.WHITE)
        .count(5);

    private final List<Location> locations = new ArrayList<>();
    private final ParticleBuilder particles;

    private final UUID uuid;
    // How many times should particles be resent.
    private int iterations = MainConfig.getInstance().getGlowCount();

    public ParticleDisplay(@NotNull Player player) {
        this.uuid = player.getUniqueId();
        this.particles = particleBuilder.clone().receivers(player);
    }

    public ParticleDisplay addLocations(@NotNull List<Location> locations) {
        this.locations.addAll(locations);
        return this;
    }

    public void start() {
        activeDisplays.put(uuid, this);
    }

    public boolean shouldDisplay() {
        return iterations > 0;
    }

    public void display() {
        if (iterations > 0) {
            this.locations.forEach(loc -> particles.clone().location(loc).spawn());
            iterations--;
        }
    }

    // Scheduling

    private static final Map<UUID, ParticleDisplay> activeDisplays = new ConcurrentHashMap<>();
    private static @Nullable BukkitTask TASK = null;

    public static void startTask(@NotNull ChestSearch plugin) {
        TASK = Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            () -> {
                Iterator<ParticleDisplay> iterator = activeDisplays.values().iterator();
                while (iterator.hasNext()) {
                    ParticleDisplay display = iterator.next();
                    if (!display.shouldDisplay()) {
                        iterator.remove();
                        return;
                    }
                    display.display();
                }
            },
            0,
            10
        );
    }

    public static void endTask() {
        if (TASK == null || TASK.isCancelled()) {
            return;
        }
        TASK.cancel();
    }

}
