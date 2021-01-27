package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ClearChatCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.isOp()){
            for(Player p : Bukkit.getOnlinePlayers()){
                ChatUtil.clearChat(p);
                p.sendMessage(ColorUtils.color("&7Czat został wyczyszczony"));
            }
        }
        return false;
    }
}
