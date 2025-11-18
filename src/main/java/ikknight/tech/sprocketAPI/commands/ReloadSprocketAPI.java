package ikknight.tech.sprocketAPI.commands;

import ikknight.tech.sprocketAPI.SprocketAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadSprocketAPI implements CommandExecutor {
    SprocketAPI s;
    public ReloadSprocketAPI(SprocketAPI s){
        this.s = s;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()){
            sender.sendMessage(ChatColor.YELLOW+" Reloading Server...");
            s.onDisable();
            s.onEnable();
            sender.sendMessage(ChatColor.GREEN+" Reload Successful!");
        }else{
            sender.sendMessage("You do not have permissions to run this command.");
        }

        return false;
    }
}
