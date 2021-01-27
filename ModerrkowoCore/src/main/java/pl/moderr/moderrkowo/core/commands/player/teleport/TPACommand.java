package pl.moderr.moderrkowo.core.commands.player.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.HashMap;
import java.util.UUID;

public class TPACommand implements CommandExecutor {


    private static TPACommand instance;

    public static TPACommand getInstance() {
        return instance;
    }

    public TPACommand() {
        instance = this;
    }

    public HashMap<UUID, UUID> tpaRequests = new HashMap<UUID, UUID>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            /*if(Moderrkowo.getAntyLogoutInstance().inFight(p.getUniqueId())){
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }*/
            if (args.length > 0) {
                Player to = Bukkit.getPlayer(args[0]);
                if (to != null) {
                    if (p.getUniqueId().equals(to.getUniqueId())) {
                        p.sendMessage(ColorUtils.color("&cNie możesz się teleportować do siebie!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                    if(to.getWorld() != p.getWorld()){
                        p.sendMessage(ColorUtils.color("&cNie można się teleportować między światami!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                    Location first = to.getLocation();
                    first.setY(0);
                    Location secound = p.getLocation();
                    secound.setY(0);
                    if(first.distance(secound) > 1500){
                        p.sendMessage(ColorUtils.color("&cMożna się tylko teleportować do 1500 kratek!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                    if (tpaRequests.containsValue(to.getUniqueId())) {
                        for (UUID key : tpaRequests.keySet()) {
                            tpaRequests.remove(key, to.getUniqueId());
                        }
                    }
                    tpaRequests.put(p.getUniqueId(), to.getUniqueId());
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    to.playSound(to.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    p.sendMessage(ColorUtils.color("&aWysłano prośbę o teleportacje."));
                    to.sendMessage(ColorUtils.color("&6" + p.getName() + " &eprosi o teleportacje do Ciebie\n&aWpisz &c/tpaccept&a aby zaakceptować\n&aWpisz &c/tpdeny&a aby anulować"));
                } else {
                    p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            } else {
                p.sendMessage(ColorUtils.color("&cPodaj nick gracza!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }
}
