package pl.moderr.moderrkowo.core.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ChatListener implements Listener {

    @EventHandler
    public void chat(@NotNull AsyncPlayerChatEvent e) {
        e.setMessage(e.getMessage().replace("%", "%%"));
        if (e.getPlayer().isOp()) {
            e.setMessage(ColorUtils.color(e.getMessage()));
        }
        e.setFormat(ChatUtil.getChatName(e.getPlayer()) + ChatColor.RESET + " " + e.getMessage());
    }

    /*@EventHandler
    public void aczigment(PlayerAdvancementDoneEvent e){
        Player p = e.getPlayer();
        Advancement advancement = e.getAdvancement();
        String name = advancement.getKey().getNamespace();
        Bukkit.broadcastMessage(ColorUtils.color("&6" + p.getName() + " &ewykonał osiągnięcie &8[&a" + name + "&8]"));
    }*/


}
