package pl.moderr.moderrkowo.core.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;

public class MotdListener implements Listener {

    @EventHandler
    public void ping(PaperServerListPingEvent e) {
        e.setMaxPlayers(e.getNumPlayers() + 1);
        e.setVersion("Moderrkowo 1.16.5");
        String line1 = ColorUtils.color(HexResolver.parseHexString("<gradient:#F5B618:#F5F210>Moderrkowo") + " &7- &e⚔ &6Survival &e⚔");
        String line2 = ColorUtils.color("&9> &cBETA &9<");
        e.setMotd(line1 + "\n" + line2);
    }

}
