package net.javamio.template.command.commandapi;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class CommandAPIWrapper {

    private final @NotNull Plugin plugin;
    private final @NotNull String name;
    private final @NotNull List<Argument<?>> arguments = new ArrayList<>();
    private final @NotNull List<CommandAPIWrapper> children = new ArrayList<>();

    private @Nullable String permission;
    private @NotNull String[] aliases = new String[0];

    private @Nullable PlayerCommandExecutor playerExec;
    private @Nullable ConsoleCommandExecutor consoleExec;
    private @Nullable CommandExecutor anyExec;

    private CommandAPIWrapper(@NotNull final Plugin plugin, @NotNull final String name) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.name = Objects.requireNonNull(name, "name");
    }

    public static @NotNull CommandAPIWrapper command(@NotNull final Plugin plugin, @NotNull final String name) {
        return new CommandAPIWrapper(plugin, name);
    }

    public @NotNull CommandAPIWrapper permission(@Nullable final String permission) {
        this.permission = permission;
        return this;
    }

    public @NotNull CommandAPIWrapper aliases(@NotNull final String... aliases) {
        this.aliases = aliases != null ? aliases : new String[0];
        return this;
    }

    public @NotNull CommandAPIWrapper args(@NotNull final Argument<?>... args) {
        if (args != null) arguments.addAll(List.of(args));
        return this;
    }

    public @NotNull CommandAPIWrapper execAny(@NotNull final CommandExecutor exec) {
        this.anyExec = exec;
        return this;
    }

    public @NotNull CommandAPIWrapper execPlayer(@NotNull final PlayerCommandExecutor exec) {
        this.playerExec = exec;
        return this;
    }

    public @NotNull CommandAPIWrapper execConsole(@NotNull final ConsoleCommandExecutor exec) {
        this.consoleExec = exec;
        return this;
    }

    public @NotNull CommandAPIWrapper sub(@NotNull final String name, @NotNull final Consumer<CommandAPIWrapper> builder) {
        final CommandAPIWrapper child = new CommandAPIWrapper(plugin, name);
        builder.accept(child);
        this.children.add(child);
        return this;
    }

    public void register() {
        buildNode().register();
    }

    private @NotNull CommandAPICommand buildNode() {
        final CommandAPICommand cmd = new CommandAPICommand(this.name);

        if (permission != null) cmd.withPermission(permission);
        if (aliases.length > 0) cmd.withAliases(aliases);
        if (!arguments.isEmpty()) cmd.withArguments(arguments.toArray(Argument[]::new));

        if (anyExec != null) {
            cmd.executes(anyExec);
        } else {
            if (playerExec != null) cmd.executesPlayer(playerExec);
            if (consoleExec != null) cmd.executesConsole(consoleExec);

            if (playerExec == null && consoleExec == null) {
                cmd.executes((CommandExecutor) (sender, args) -> sender.sendRichMessage("<red>Error: No executor found!"));
            }
        }

        for (final CommandAPIWrapper child : children) {
            cmd.withSubcommand(child.buildNode());
        }
        return cmd;
    }

}
