package pl.moderr.moderrkowo.core.commands.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.cuboids.ModerrCuboids;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class CraftingDzialkaCommand implements CommandExecutor {



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ColorUtils.color("&7https://moderr.pl/patch/dzialki.png"));
        return false;
    }
}
