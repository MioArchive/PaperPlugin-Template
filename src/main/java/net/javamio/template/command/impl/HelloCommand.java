package net.javamio.template.command.impl;

import net.javamio.template.command.BukkitCommand;
import net.javamio.template.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelloCommand extends BukkitCommand {

    public HelloCommand() {
        super("hello", "hello.command");
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        player.sendMessage(ColorUtil.translateColorCodes("&aHello, &f" + player.getName() + "!"));
    }
}
