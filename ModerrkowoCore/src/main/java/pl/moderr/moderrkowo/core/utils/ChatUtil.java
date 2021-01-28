package pl.moderr.moderrkowo.core.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;

public class ChatUtil {

    public static void clearChat(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage(" ");
        }
    }
    public static String getChatName(Player player){
        if (player.isOp()) {
            return (ColorUtils.color("&c&lADM ") + ChatColor.of(new Color(128, 95, 217)) + player.getName());
        } else {
            return (ChatColor.of(new Color(105, 95, 217)) + player.getName());
        }
    }
    public static String getPrefix(Player player){
        if(player.isOp()){
            return ColorUtils.color("&c&lADM &e") + player.getName();
        }else{
            return player.getName();
        }
    }

}
