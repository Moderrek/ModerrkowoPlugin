package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ALOGI_Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.isOp()){
            if(sender instanceof Player){
                if(Main.getInstance().dbListener.Playerlisteners.contains(((Player) sender).getUniqueId())){
                    Main.getInstance().dbListener.Playerlisteners.remove(((Player) sender).getUniqueId());
                    sender.sendMessage(ColorUtils.color("&cPrzestano nasłuchiwać logi!"));
                }else{
                    Main.getInstance().dbListener.Playerlisteners.add(((Player) sender).getUniqueId());
                    sender.sendMessage(ColorUtils.color("&aZaczęto nasłuchiwać logi!"));
                }
            }else{
                sender.sendMessage(ColorUtils.color("&cNie możesz tej komendy wywołać w konsoli!"));
            }
        }else{
            sender.sendMessage(ColorUtils.color("&cNie masz permisji!"));
        }
        return false;
    }
}
