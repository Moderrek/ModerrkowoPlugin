package pl.moderr.moderrkowo.core.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;

public class ModerrkowoLog {

    public static void LogAdmin(String text) {
        String output = ColorUtils.color(HexResolver.parseHexString("<gradient:#F7C13C:#D4E443>Log") + " &7" + text);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(output);
            }
        }
    }

    public static void LogAC(Player p, String message) {
        String output = ColorUtils.color("&8[" + HexResolver.parseHexString("<gradient:#d72e2e:#ff5151>AdminChat") + "&r&8] " + ChatColor.of(new Color(69, 122, 199)) + p.getName() + " &r" + message);
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if (allPlayers.isOp()) {
                allPlayers.sendMessage(output);
            }
        }
    }

    public static void LogAC(String name, String message) {
        String output = ColorUtils.color("&8[" + HexResolver.parseHexString("<gradient:#d72e2e:#ff5151>AdminChat") + "&r&8] " + ChatColor.of(new Color(199, 69, 69)) + name + " &r" + message);
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            if (allPlayers.isOp()) {
                allPlayers.sendMessage(output);
            }
        }
    }

}
