package net.javamio.template.command.impl;

import net.javamio.template.util.ColorUtil;
import org.bukkit.entity.Player;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.jetbrains.annotations.NotNull;

public final class HelloCommand {

    public static void register(@NotNull PaperCommandManager<Source> commandManager) {
        commandManager.command(commandManager.commandBuilder("hello")
                .senderType(PlayerSource.class)
                .permission("hello.command")
                .handler(context -> {
                    Player player = context.sender().source();
                    player.sendMessage(ColorUtil.translateColorCodes("&aHello, &f" + player.getName() + "!"));
                }));
    }
}
