package uk.firedev.chestsearch.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class CommandUtil {

    public static final SimpleCommandExceptionType CANT_BE_CONSOLE = new SimpleCommandExceptionType(
        MessageComponentSerializer.message().serialize(
            Component.text("This command cannot be executed by console!")
        )
    );

    public static @NonNull Player requirePlayer(@Nullable CommandSourceStack source) throws CommandSyntaxException {
        if (source == null) {
            throw CANT_BE_CONSOLE.create();
        }
        CommandSender sender = source.getSender();
        if (!(sender instanceof Player player)) {
            throw CANT_BE_CONSOLE.create();
        }
        return player;
    }

    public static @NonNull Player requirePlayer(@Nullable CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (context == null) {
            throw CANT_BE_CONSOLE.create();
        }
        return requirePlayer(context.getSource());
    }

}
