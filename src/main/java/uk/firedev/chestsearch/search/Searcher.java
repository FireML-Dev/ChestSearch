package uk.firedev.chestsearch.search;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Searcher {

    private final Player player;
    private final int range;

    public Searcher(@NotNull Player player, int range) {
        this.player = player;
        this.range = range;
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public @NotNull List<Container> search(@NotNull ItemType type) {
        Material material = type.asMaterial();
        if (material == null) {
            return List.of();
        }
        return search(container -> container.getInventory().contains(material));
    }

    public @NotNull List<Container> search(@NotNull String name) {
        return search(container -> {
            for (ItemStack item : container.getInventory()) {
                if (item == null || item.isEmpty()) {
                    continue;
                }
                Component display = item.getItemMeta().displayName();
                if (display == null) {
                    continue;
                }
                String plainName = PlainTextComponentSerializer.plainText().serialize(display);
                if (name.equalsIgnoreCase(plainName)) {
                    return true;
                }
            }
            return false;
        });
    }

    public @NotNull List<Container> search(@NotNull ItemStack similar) {
        return search(container -> {
            for (ItemStack item : container.getInventory()) {
                if (item == null || item.isEmpty()) {
                    continue;
                }
                if (item.isSimilar(similar)) {
                    return true;
                }
            }
            return false;
        });
    }

    private @NotNull List<Container> search(@NotNull Predicate<Container> predicate) {
        return SearchUtil.fetchAllBlocksInRadius(player.getLocation().getBlock(), range).stream()
            .map(block -> (block.getState(false) instanceof Container container) ? container : null)
            .filter(Objects::nonNull)
            .filter(predicate)
            .toList();
    }

}
