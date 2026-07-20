package uk.firedev.chestsearch.search;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.config.MainConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SearchUtil {

    private static final ParticleBuilder particles = new ParticleBuilder(Particle.DUST).color(Color.WHITE).count(5);

    public static List<Container> fetchAllContainersInRadius(@NotNull Block center, @NotNull Predicate<Container> predicate) {
        int range = MainConfig.getInstance().getSearchRange();
        World world = center.getWorld();
        int minX = center.getX() - range;
        int maxX = center.getX() + range;
        int minY = center.getY() - range;
        int maxY = center.getY() + range;
        int minZ = center.getZ() - range;
        int maxZ = center.getZ() + range;

        List<Container> containers = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getState(false) instanceof Container container && predicate.test(container)) {
                        containers.add(container);
                    }
                }
            }
        }
        return containers;
    }

    public static void glow(@NotNull Container container) {
        Block block = container.getBlock();
        new ParticleDisplay(
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
