package uk.firedev.chestsearch.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.config.MainConfig;
import uk.firedev.chestsearch.search.SearchUtil;
import uk.firedev.chestsearch.search.Searcher;
import uk.firedev.messagelib.message.ComponentMessage;
import uk.firedev.messagelib.message.ComponentSingleMessage;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")
public class MainCommand {

    public static LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("chestsearch")
            .then(reload())
            .then(hand())
            .then(type())
            .then(name())
            .build();
    }

    private static LiteralArgumentBuilder<CommandSourceStack> reload() {
        return Commands.literal("reload")
            .requires(stack -> stack.getSender().hasPermission("chestsearch.admin"))
            .executes(ctx -> {
                // TODO reload and message
                ctx.getSource().getSender().sendPlainMessage("You reloaded ChestSearch.");
                return 1;
            });
    }

    private static LiteralArgumentBuilder<CommandSourceStack> hand() {
        return Commands.literal("hand")
            .executes(ctx -> {
                Player player = CommandUtil.requirePlayer(ctx);
                ItemStack handItem = player.getInventory().getItemInMainHand();
                Searcher.SearchResult result = new Searcher(player).search(handItem);
                execute(player, result, handItem.displayName());
                return 1;
            });
    }

    private static LiteralArgumentBuilder<CommandSourceStack> type() {
        return Commands.literal("type")
            .then(
                Commands.argument("type", ArgumentTypes.resource(RegistryKey.ITEM))
                    .executes(ctx -> {
                        Player player = CommandUtil.requirePlayer(ctx);
                        ItemType type = ctx.getArgument("type", ItemType.class);
                        Searcher.SearchResult result = new Searcher(player).search(type);
                        execute(
                            player,
                            result,
                            Component.translatable(type)
                        );
                        return 1;
                    })
            );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> name() {
        return Commands.literal("name")
            .then(
                Commands.argument("name", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        Player player = CommandUtil.requirePlayer(ctx);
                        String name = ctx.getArgument("name", String.class);
                        Searcher.SearchResult result = new Searcher(player).search(name);
                        execute(player, result, Component.text(name));
                        return 1;
                    })
            );
    }

    private static void execute(@NotNull Player player, @NotNull Searcher.SearchResult result, @NotNull Component searched) {
        if (result.found().isEmpty()) {
            ComponentMessage.componentMessage("Found no matches for {searched}.")
                .replace("{searched}", searched)
                .send(player);
            return;
        }
        ComponentMessage.componentMessage("Found {amount} {partial}matches for {searched}. They are now glowing.")
            .replace("{amount}", result.found().size())
            .replace("{searched}", searched)
            .replace("{partial}", result.partial() ? "partial " : "")
            .send(player);
        SearchUtil.glow(player, result.found());
    }

}
