package uk.firedev.chestsearch.search;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.ChestSearch;

import java.util.List;

public class GlowScheduler {

    private final List<Location> locations;
    private final ParticleBuilder particles;

    private BukkitTask task = null;

    // How many times should particles be resent.
    private int iterations = 5;

    public GlowScheduler(@NotNull List<Location> locations, @NotNull ParticleBuilder particles) {
        this.locations = locations;
        this.particles = particles.clone();
    }

    public void start() {
        if (task != null || iterations <= 0) {
            return;
        }
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(
            ChestSearch.getInstance(),
            () -> {
                if (task != null && iterations <= 0) {
                    task.cancel();
                }
                locations.forEach(loc -> particles.location(loc).spawn());
                iterations--;
            },
            0,
            10
        );
    }

}
