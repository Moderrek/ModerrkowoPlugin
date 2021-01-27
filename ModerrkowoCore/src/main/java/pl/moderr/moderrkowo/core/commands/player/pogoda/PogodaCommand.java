package pl.moderr.moderrkowo.core.commands.player.pogoda;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PogodaCommand implements CommandExecutor, Listener {


    ArrayList<UUID> votedList = new ArrayList<UUID>();
    boolean rain = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm()) {
                if (votedList.contains(p.getUniqueId())) {
                    p.sendMessage(ColorUtils.color("&cJuż zagłosowałeś!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                } else {
                    votedList.add(p.getUniqueId());
                    int i = (int) (Bukkit.getOnlinePlayers().size() / 1.5);
                    if (i == 0) {
                        i = 1;
                    }
                    p.sendMessage(ColorUtils.color("&aZagłosowano pomyślnie! " + "&8(&a" + votedList.size() + "&8/&7" + i + "&8)"));
                    Bukkit.broadcastMessage(ColorUtils.color("&6" + p.getName() + " &ezagłosował na zmiane pogody! " + "&8(&a" + votedList.size() + "&8/&7" + i + "&8)"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    if (votedList.size() >= i) {
                        Objects.requireNonNull(Bukkit.getWorld("world")).setStorm(false);
                        Bukkit.broadcastMessage(ColorUtils.color("&8[&e*&8] &ePrzegłosowano na zmiane pogody! Pogoda została zmieniona."));
                        rain = false;
                        votedList = new ArrayList<UUID>();
                    }
                    return true;
                }
            } else {
                p.sendMessage(ColorUtils.color("&cAktualnie jest ładna pogoda nie można głosować!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }

    @EventHandler
    public void onRain(WeatherChangeEvent e) {
        if (e.getWorld() != Bukkit.getWorld("world")) {
            return;
        }
        if (e.getWorld().hasStorm()) {
            votedList = new ArrayList<UUID>();
            rain = true;
            Bukkit.broadcastMessage(ColorUtils.color("&eRozpoczęto głosowanie na zmiane pogody! &f/pogoda &eaby zagłosować."));
        } else {
            votedList = new ArrayList<UUID>();
            rain = false;
        }
    }
}
