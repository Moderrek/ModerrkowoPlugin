package pl.moderr.moderrkowo.core.commands.player.teleport;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.database.ModerrkowoDatabase;
import pl.moderr.moderrkowo.database.callback.CallbackExists;
import pl.moderr.moderrkowo.database.callback.CallbackHomeLoad;
import pl.moderr.moderrkowo.database.data.Home;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(Main.getInstance().antyLogout.inFight(p.getUniqueId())){
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            ModerrkowoDatabase.getInstance().existsHome(p.getUniqueId(), new CallbackExists() {
                @Override
                public void onDone(Boolean aBoolean) {
                    if(aBoolean){
                        ModerrkowoDatabase.getInstance().getHome(p.getUniqueId(), new CallbackHomeLoad() {
                            @Override
                            public void onDone(Home home) {
                                p.teleport(home.ToLocation());
                                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,1);
                                p.sendTitle("", ColorUtils.color("&aPomyślnie przeteleportowano"));
                            }

                            @Override
                            public void onFail(Exception e) {
                                p.sendMessage(ColorUtils.color("&cWystąpił problem z pobraniem danych..."));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            }
                        });
                    }else{
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        p.sendMessage(ColorUtils.color("&cNie masz ustawionego domu! /ustawdom"));
                    }
                }

                @Override
                public void onFail(Exception e) {
                    p.sendMessage(ColorUtils.color("&cWystąpił problem z pobraniem danych..."));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            });
        }
        return false;
    }
}
