package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class PortalListener implements Listener {

    @EventHandler
    public void onPortal(final PlayerPortalEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.setCancelled(true);
            event.getPlayer().sendTitle("", ColorUtils.color("&cKres jest wyłączony!"));
            event.setTo(event.getPlayer().getLocation());
        }
    }

}
