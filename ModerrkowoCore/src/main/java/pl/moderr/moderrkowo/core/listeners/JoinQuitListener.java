package pl.moderr.moderrkowo.core.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;

import java.awt.*;

public class JoinQuitListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(ColorUtils.color(HexResolver.parseHexString("<gradient:#FDBB2D:#22C1C3>" + p.getName()) + " &r&bwskoczył na serwer!"));
        p.sendMessage(ColorUtils.color("&b❄ " + HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo") + " &r&b❄"));
        p.sendMessage(ColorUtils.color("&6> &7Witaj, &6" + p.getName() + " &7na &6MODERRKOWO!"));
        p.sendMessage(ColorUtils.color("&6> &7Discord serwera gdzie znajdują sie wszystkie informacje &c/discord"));
        p.sendMessage(ColorUtils.color("&6> &7Granie na serwerze oznacza akcepracją regulaminu &c&m/regulamin"));
        p.sendMessage(ColorUtils.color("&6> &cModerrkowo &7to gwarancja satysfakcji zabawy i bezpieczeństwa!"));
        p.sendMessage(" ");
        for(Player players : Bukkit.getOnlinePlayers()) {
            updateTab(players);
        }
        e.getPlayer().setPlayerListName(ChatUtil.getChatName(e.getPlayer()));
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTab(player);
        }
    }

    public void updateTab(Player e) {
        int administracja = 0;
        int gracze = 0;
        for(Player admin : Bukkit.getOnlinePlayers()){
            gracze++;
            if(admin.isOp()){
                administracja++;
            }
        }
        String header
                = " \n "
                + HexResolver.parseHexString("&b❄ &8[ <gradient:#FD4F1D:#FCE045>Moderrkowo") + " &r&8] &b❄&r"
                + " \n "
                + " \n&7Administracja online  &8» &6" + administracja
                + " \n&7Gracze online &8» &6" + gracze
                + " \n "
                + " \n&6----------------------------------"
                + " \n ";
        String footer
                = " \n&6----------------------------------"
                + " \n ";
        e.setPlayerListHeader(ColorUtils.color(header));
        e.setPlayerListFooter(ColorUtils.color(footer));
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            updateTab(players);
        }
        e.setQuitMessage(ColorUtils.color(HexResolver.parseHexString("<gradient:#FDBB2D:#22C1C3>" + e.getPlayer().getName()) + " &r&bopuścił serwer :c"));
    }

}
