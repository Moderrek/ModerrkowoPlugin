package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.database.data.User;
import pl.moderr.moderrkowo.database.events.DatabaseLog;
import pl.moderr.moderrkowo.database.events.ModerrDatabaseListener;

import java.util.ArrayList;
import java.util.UUID;

public class DatabaseListener implements ModerrDatabaseListener {

    public ArrayList<UUID> Playerlisteners = new ArrayList<UUID>();

    public void sendMessageToAllListeners(String message){
        for(UUID uuid : Playerlisteners){
            Player p = Bukkit.getPlayer(uuid);
            if(p != null){
                p.sendMessage(ColorUtils.color(message));
            }else{
                Playerlisteners.remove(uuid);
            }
        }
    }

    @Override
    public void onLoadUser(User user) {
        sendMessageToAllListeners(ColorUtils.color("&c&lLOG &7Załadowano pomyślnie gracza " + user.getName()));
    }

    @Override
    public void onLog(DatabaseLog log) {
        sendMessageToAllListeners(ColorUtils.color("\n&c&lLOG &7" + log.getSender() + "&8: &7" + log.getMessage() + " \n&c&lLOG &8[A: " + log.getAction() + " R:" + log.getResult() + "]\n "));
    }

    @Override
    public void onSaveUser(User user) {
        Player p = user.getPlayer();
        if(p != null){
            p.sendMessage(ColorUtils.color("&aZapisano dane"));
        }
        sendMessageToAllListeners(ColorUtils.color("&c&lLOG &7Zapisano pomyślnie gracza " + user.getName()));
    }

    @Override
    public void onRegisterUser(User user) {
        if(user.getPlayer() != null){
            user.getPlayer().getInventory().addItem(new ItemStack(Material.WOODEN_AXE));
            user.getPlayer().getInventory().addItem(new ItemStack(Material.BREAD, 16));
            Bukkit.broadcastMessage(ColorUtils.color("&9> &7Gracz &6" + user.getName() + " &7dołączył po raz pierwszy!"));
        }
    }
}
