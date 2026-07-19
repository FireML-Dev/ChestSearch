package uk.firedev.chestsearch.search;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.config.MainConfig;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SearchUtil {

    private static final ParticleBuilder particles = new ParticleBuilder(Particle.DUST).color(Color.WHITE).count(5);

    public static List<Container> fetchAllContainersInRadius(@NotNull Block center, @NotNull Predicate<Container> predicate) {
        int range = MainConfig.getInstance().getSearchRange();
        int minY = center.getY() - range;
        int maxY = center.getY() + range;
        Predicate<Block> preFilter = block -> block.getY() >= minY && block.getY() <= maxY;

        return center.getChunk().getTileEntities(preFilter, false).stream()
            .map(state -> (state instanceof Container c) ? c : null)
            .filter(Objects::nonNull)
            .filter(predicate)
            .toList();
    }

    public static void glow(@NotNull Container container) {
        Block block = container.getBlock();
        new GlowScheduler(
            fetchLocations(block.getLocation().toCenterLocation()),
            particles
        ).start();
    }

    private static List<Location> fetchLocations(@NotNull Location location) {
        return List.of(
            location.clone().add(0, 0.5, 0),
            location.clone().add(0, -0.5, 0),
            location.clone().add(0.5, 0, 0),
            location.clone().add(-0.5, 0, 0),
            location.clone().add(0, 0, 0.5),
            location.clone().add(0, 0, 0.5)
        );
    }

}
