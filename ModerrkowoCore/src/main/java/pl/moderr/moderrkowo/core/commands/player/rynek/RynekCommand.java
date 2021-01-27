package pl.moderr.moderrkowo.core.commands.player.rynek;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.database.data.RynekItem;

public class RynekCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(args.length > 0){
                if(args.length > 1){
                    if(args[0].equalsIgnoreCase("wystaw")){
                        Main.getInstance().rynekManager.rynekItems.add(new RynekItem(((Player) sender).getUniqueId(), ((Player) sender).getItemInHand(), 1));
                    }
                    return false;
                }
                return false;
            }
            ((Player) sender).openInventory(Main.getInstance().rynekManager.getRynekInventory(1));
            sender.sendMessage(ColorUtils.color("&aOtworzono rynek"));
        }
        return false;
    }
}
