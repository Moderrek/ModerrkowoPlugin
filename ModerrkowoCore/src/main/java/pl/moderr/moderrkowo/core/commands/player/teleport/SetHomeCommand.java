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
import pl.moderr.moderrkowo.database.callback.CallbackEmpty;
import pl.moderr.moderrkowo.database.callback.CallbackExists;
import pl.moderr.moderrkowo.database.data.Home;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(Main.getInstance().antyLogout.inFight(p.getUniqueId())){
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            Home home = new Home(p.getUniqueId(), p.getLocation().getWorld().getName(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
            ModerrkowoDatabase.getInstance().existsHome(p.getUniqueId(), new CallbackExists() {
                @Override
                public void onDone(Boolean aBoolean) {
                    if(aBoolean){
                        ModerrkowoDatabase.getInstance().updateHome(p.getUniqueId(), home, new CallbackEmpty() {
                            @Override
                            public void onDone() {
                                p.sendMessage(ColorUtils.color("&aUstawiono dom"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            }

                            @Override
                            public void onFail(Exception e) {
                                p.sendMessage(ColorUtils.color("&cWystąpił problem przy ustawieniu domu"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            }
                        });
                    }else{
                        ModerrkowoDatabase.getInstance().insertHome(p.getUniqueId(), home, new CallbackEmpty() {
                            @Override
                            public void onDone() {
                                p.sendMessage(ColorUtils.color("&aUstawiono dom"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            }

                            @Override
                            public void onFail(Exception e) {
                                p.sendMessage(ColorUtils.color("&cWystąpił problem przy ustawieniu domu"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            }
                        });
                    }
                }

                @Override
                public void onFail(Exception e) {
                    p.sendMessage(ColorUtils.color("&cWystąpił problem przy ustawieniu domu"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            });
        }
        return false;
    }
}
