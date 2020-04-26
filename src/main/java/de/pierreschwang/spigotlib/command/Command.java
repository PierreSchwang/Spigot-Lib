package de.pierreschwang.spigotlib.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player) {
            onPlayerExecute(((Player) sender), label, args);
            return true;
        }
        onConsoleExecute((ConsoleCommandSender) sender, label, args);
        return true;
    }

    public void onPlayerExecute(Player player, String command, String[] args) {
        player.sendMessage("§cNo implementation given for players");
    }

    public void onConsoleExecute(ConsoleCommandSender sender, String command, String[] args) {
        sender.sendMessage("§cNo implementation given for players");
    }

}