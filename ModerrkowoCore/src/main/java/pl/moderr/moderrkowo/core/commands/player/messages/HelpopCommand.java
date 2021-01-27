package pl.moderr.moderrkowo.core.commands.player.messages;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class HelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                // Wysylanie wiadomosci do moderacji
                boolean success = Logger.logHelpopMessage(p, Logger.getMessage(args, 0, true));
                // Wyswietlanie wysłania
                if (success) {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    p.sendMessage(ColorUtils.color("&aPomyślnie wysłano!"));
                    p.sendTitle(ColorUtils.color("&6&lModerrkowo"), ColorUtils.color("&aWysłano wiadomość."));
                } else {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendTitle(ColorUtils.color("&6&lModerrkowo"), ColorUtils.color("&cNie wysłano wiadomości."));
                    p.sendMessage(ColorUtils.color("&cBrak aktywnych moderatorów."));
                }
                return true;
            } else {
                // Wyswietlanie bledu
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                p.sendMessage(ColorUtils.color("&cUzycie: &e/helpop <wiadomość>"));
                return false;
            }
        } else {
            sender.sendMessage("Nie jestes graczem!");
            return false;
        }
    }
}
