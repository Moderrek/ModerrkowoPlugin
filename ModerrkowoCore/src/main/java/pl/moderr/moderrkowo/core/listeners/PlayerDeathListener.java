package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;
import pl.moderr.moderrkowo.core.utils.ModerrkowoLog;

import java.util.Objects;


public class PlayerDeathListener implements Listener {

    @EventHandler
    public void death(PlayerDeathEvent e) {
        e.getEntity().spawnParticle(Particle.TOTEM, e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(), e.getEntity().getLocation().getZ(), 20, 1, 1, 1, 0.1f);
        //e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
        e.getEntity().sendMessage(Main.getServerName() + " " + ColorUtils.color(String.format("&fUmarłeś na kordynatach X: %s Y: %s Z: %s", e.getEntity().getLocation().getBlockX(), e.getEntity().getLocation().getBlockY(), e.getEntity().getLocation().getBlockZ())));
        ModerrkowoLog.LogAdmin(e.getDeathMessage());
        if(Objects.requireNonNull(e.getDeathMessage()).contains("drowned")){
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &eutonął"));
            return;
        }
        else if(e.getDeathMessage().contains("blew up")){
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &ezostał wysadzony"));
            return;
        }
        else if(e.getDeathMessage().contains("was killed by")){
            String s;
            if(e.getEntity().getKiller() == null){
                s = e.getEntity().getLastDamageCause().getEntityType().getName();
            }else{
                s = e.getEntity().getKiller().getName();
            }
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &ezostał zabity przez &6" + s));
            return;
        }
        else if(e.getDeathMessage().contains("hit the ground too hard") || e.getDeathMessage().contains("fell from a high place")){
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &eumarł z wysokości"));
            return;
        }
        else if(e.getDeathMessage().contains("burned to death") || e.getDeathMessage().contains("went up in flames")){
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &espalił sie"));
            return;
        }
        else if(e.getDeathMessage().contains("tried to swim in lava") || e.getDeathMessage().contains("tried to swim in lava to escape")){
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &eposzedł się kąpać w lawie peperonczki ^^"));
            return;
        }
        else if(e.getDeathMessage().contains("was burnt")){
            String s;
            if(e.getEntity().getKiller() == null){
                s = e.getEntity().getLastDamageCause().getEntityType().getName();
            }else{
                s = e.getEntity().getKiller().getName();
            }
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &espalił się od " + s));
        }
        else if(e.getDeathMessage().contains("was fireballed")){
            String s;
            if(e.getEntity().getKiller() == null){
                s = e.getEntity().getLastDamageCause().getEntityType().getName();
            }else{
                s = e.getEntity().getKiller().getName();
            }
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &ezostał rzucony kulą ognia od " + s));
        }
        else if(e.getDeathMessage().contains("was slain by")){
            String s;
            if(e.getEntity().getKiller() == null){
                s = e.getEntity().getLastDamageCause().getEntityType().getName();
            }else{
                s = e.getEntity().getKiller().getName();
            }
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &ezostał zabity przez &6" + s));
            return;
        }
        else if(e.getDeathMessage().contains("was shot by")){
            String s;
            if(e.getEntity().getKiller() == null){
                s = e.getEntity().getLastDamageCause().getEntityType().getName();
            }else{
                s = e.getEntity().getKiller().getName();
            }
            e.setDeathMessage(ColorUtils.color("&6" + e.getEntity().getName() + " &ezostał rozstrzelany przez &6" + s));
            return;
        }
        e.setDeathMessage(ColorUtils.color(HexResolver.parseHexString("&6{playername} &eumarł").replace("{playername}", e.getEntity().getName())));
    }

}
