package uk.firedev.chestsearch.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import uk.firedev.chestsearch.ChestSearch;
import uk.firedev.chestsearch.config.MainConfig;
import uk.firedev.chestsearch.search.SearchUtil;
import uk.firedev.chestsearch.search.Searcher;
import uk.firedev.messagelib.message.ComponentMessage;

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
                ChestSearch.getInstance().reload();
                MainConfig.getInstance().getReloadedMessage().send(ctx.getSource().getSender());
                return 1;
            });
    }

    // Search Commands

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
            MainConfig.getInstance().getNoMatchesMessage(searched).send(player);
            return;
        }
        MainConfig.getInstance().getFoundMatchesMessage(
            result.partial(),
            result.found().size(),
            searched
        ).send(player);
        SearchUtil.glow(player, result.found());
    }

}
