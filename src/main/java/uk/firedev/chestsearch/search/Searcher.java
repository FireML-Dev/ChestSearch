package uk.firedev.chestsearch.search;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class Searcher {

    private final Player player;

    public Searcher(@NotNull Player player) {
        this.player = player;
    }

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    public @NotNull SearchResult search(@NotNull ItemType type) {
        Material material = type.asMaterial();
        if (material == null) {
            return new SearchResult(List.of(), false);
        }
        return new SearchResult(
            search(container -> container.getInventory().contains(material)),
            false
        );
    }

    public @NotNull SearchResult search(@NotNull String name) {
        AtomicBoolean partial = new AtomicBoolean(false);
        List<Container> found = search(container -> {
            List<String> itemNames = Arrays.stream(container.getInventory().getStorageContents())
                .filter(item -> item != null && !item.isEmpty())
                .map(item -> {
                    Component display = item.getItemMeta().displayName();
                    if (display == null) {
                        display = GlobalTranslator.render(Component.translatable(item), Locale.ENGLISH);
                    }
                    return PlainTextComponentSerializer.plainText().serialize(display);
                })
                .toList();
            if (itemNames.stream().anyMatch(name::equalsIgnoreCase)) {
                return true;
            } else if (itemNames.stream().anyMatch(itemName -> itemName.startsWith(name))) {
                partial.set(true);
                return true;
            } else {
                return false;
            }
        });
        return new SearchResult(found, partial.get());
    }

    public @NotNull SearchResult search(@NotNull ItemStack similar) {
        List<Container> found = search(container -> {
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
        return new SearchResult(found, false);
    }

    private @NotNull List<Container> search(@NotNull Predicate<Container> predicate) {
        return SearchUtil.fetchAllContainersInRadius(player.getLocation().getBlock(), predicate);
    }

    public record SearchResult(@NotNull List<Container> found, boolean partial) {}

}
