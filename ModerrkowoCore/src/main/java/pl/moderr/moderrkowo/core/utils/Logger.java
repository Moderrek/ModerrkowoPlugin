package pl.moderr.moderrkowo.core.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Logger {

    public static boolean logHelpopMessage(Player sender, String message) {
        boolean success = false;
        int numberOfAdmins = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                //User u = UserManager.getUser(sender.getUniqueId());
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                TextComponent tc = new TextComponent();
                //tc.setText(ColorUtils.color(String.format("&8[&dHELP&8] &7%s&8: &e%s", u.getNicknameWithRanks("&7"), message)));
                tc.setText(ColorUtils.color(String.format("&8[&dHELP&8] &7%s&8: &e%s", sender.getName(), message)));
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ahelpop " + sender.getName() + " "));
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color(String.format("&e&lODPOWIEDZ &8(&7%s&8)", sender.getName())))));
                p.spigot().sendMessage(tc);
                numberOfAdmins++;
            }
        }
        if (numberOfAdmins != 0) {
            success = true;
        }
        return success;
    }


    public static void logAdminChat(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&8[&cAC&8] &7" + message));
            }
        }
    }

    public static void logAdminLog(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&8[&cAL&8] &c" + message));
            }
        }
    }

    public static void logPluginMessage(String message) {
        String prefix = "&e&lI ";
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color(prefix + "&e[&6PL&e] &e" + message));
            }
        }
    }

    public static void logDiscordMessage(String message) {
        String prefix = "";
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color(prefix + "&8[&9DC&8] &7" + message));
            }
        }
    }

    public static void logPluginMessage(String message, boolean error) {
        String prefix = "&e&lI ";
        if (error) {
            prefix = "&c&lE ";
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                if (error) {
                    System.out.println(ColorUtils.color(prefix + "&e[&6DB&e] &c" + message));
                    p.sendMessage(ColorUtils.color(prefix + "&e[&6PL&e] &c" + message));
                } else {
                    p.sendMessage(ColorUtils.color(prefix + "&e[&6PL&e] &a" + message));
                }
            }
        }
    }

    public static void logDataBaseMessage(String message) {
        String prefix = "";
        System.out.println("[MySQL] >> INFO >> " + message);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color(prefix + "&8[&cMySQL&8] &a" + message));
            }
        }
    }

    public static void logDataBaseMessage(String message, boolean error) {
        String prefix = "";
        if (error) {
            prefix = "&c&lE ";
            System.out.println("[MySQL] >> ERROR >> " + message);
        } else {
            System.out.println("[MySQL] >> INFO >> " + message);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                if (error) {
                    p.sendMessage(ColorUtils.color(prefix + "&8[&cMySQL&8] &c" + message));
                } else {
                    p.sendMessage(ColorUtils.color(prefix + "&8[&cMySQL&8] &a" + message));
                }
            }
        }
    }

    public static String getMessage(String [] args, int startFromArg, boolean removeFirstSpace) {
        StringBuilder out = new StringBuilder();
        for (int i = startFromArg; i != args.length; i++) {
            out.append(" ").append(args[i]);
        }
        if (removeFirstSpace) {
            return out.substring(1);
        }
        return out.toString();
    }

}
