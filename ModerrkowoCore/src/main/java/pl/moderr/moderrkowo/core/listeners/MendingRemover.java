package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class MendingRemover implements Listener {

    @EventHandler
    public void mend(PlayerItemMendEvent e){
        e.setCancelled(true);
        e.getPlayer().sendMessage(ColorUtils.color("&cZaklęcie naprawy jest wyłączone!"));
        e.getItem().removeEnchantment(Enchantment.MENDING);
    }

}
