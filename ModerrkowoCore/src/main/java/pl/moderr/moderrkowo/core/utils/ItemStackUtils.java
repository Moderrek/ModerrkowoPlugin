package pl.moderr.moderrkowo.core.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {

    public static ItemStack createGuiItem(Material material, int count, String name, String... lore) {
        ItemStack item = new ItemStack(material, count);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metaLore = new ArrayList<String>();

        for (String loreComments : lore) {
            metaLore.add(loreComments);
        }

        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack GetUUIDHead(String displayname, int count, Player p, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, count);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(displayname);
        skull.setLore(lore);
        skull.setOwningPlayer(p);
        item.setItemMeta(skull);
        return item;
    }


    public static ItemStack changeName(ItemStack item, String changedName) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ColorUtils.color(changedName));
        item.setItemMeta(im);
        return item;
    }

    @Contract("_, _ -> param1")
    public static @NotNull ItemStack changeLore(@NotNull ItemStack item, String @NotNull ... lore) {
        ItemMeta im = item.getItemMeta();
        ArrayList<String> metaLore = new ArrayList();
        String[] var5 = lore;
        int var6 = lore.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            String loreComments = var5[var7];
            metaLore.add(loreComments);
        }

        im.setLore(metaLore);
        item.setItemMeta(im);
        return item;
    }

    public static int getCountOfMaterial(@NotNull Player player, Material mat) {
        int found = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == mat) {
                    found = found + item.getAmount();
                }
            }
        }
        return found;
    }

    public static boolean getBooleanOfMaterial(@NotNull Player player, Material mat, int count) {
        int found = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == mat) {
                    found = found + item.getAmount();
                }
            }
        }
        if (count > found) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean getBooleanOfMaterial(@NotNull Player player, ItemStack itemToScan, int count) {
        int found = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                boolean b = true;
                if (item.getType() == itemToScan.getType()) {
                    for (Enchantment e : item.getEnchantments().keySet()) {
                        if (!itemToScan.getEnchantments().containsKey(e)) {
                            b = false;
                        }
                    }
                    if (itemToScan.getItemMeta().hasDisplayName()) {
                        if (!item.getItemMeta().hasDisplayName()) {
                            b = false;
                        } else {
                            if (!itemToScan.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                                b = false;
                            }
                        }
                    }
                    found = found + item.getAmount();
                }
            }
        }
        if (count > found) {
            return false;
        } else {
            return true;
        }
    }

    private static int getSameItems(@NotNull Player player, ItemStack item) {
        int howMany = 0;
        for (ItemStack invStack : player.getInventory().getContents()) {
            if (invStack != null) {
                if (invStack.getType() == item.getType()) {
                    if (invStack.getItemMeta().equals(item.getItemMeta())) {
                        howMany += invStack.getAmount();
                    }
                }
            }
        }
        return howMany;
    }

    private static @NotNull List<ItemStack> getSameItemsList(@NotNull Player player, ItemStack item) {
        int howMany = 0;
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack invStack : player.getInventory().getContents()) {
            if (invStack != null) {
                if (invStack.getType() == item.getType()) {
                    if (invStack.getItemMeta().equals(item.getItemMeta())) {
                        items.add(invStack);
                        howMany += invStack.getAmount();
                    }
                }
            }
        }
        return items;
    }

    public static boolean consumeItem(@NotNull Player player, int count, Material mat) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

        }
        return true;
    }

    public static boolean consumeItem(@NotNull Player player, int count, @NotNull ArrayList<ItemStack> items) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(items.get(0));

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

        }
        return true;
    }

    public static boolean consumeItem(@NotNull Player player, int count, ItemStack item) {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(item);

        int found = 0;
        for (ItemStack stack : ammo.values())
            found += stack.getAmount();
        if (count > found)
            return false;

        for (Integer index : ammo.keySet()) {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
                player.getInventory().setItem(index, null);
            else
                stack.setAmount(stack.getAmount() - removed);

        }
        return true;
    }

    public static @NotNull
    ItemStack createItem(Material material, String name, String @NotNull ... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> metaLore = new ArrayList();
        String[] var6 = lore;
        int var7 = lore.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            String loreComments = var6[var8];
            metaLore.add(loreComments);
        }

        meta.setLore(metaLore);
        item.setItemMeta(meta);
        return item;
    }

    public static @NotNull ItemStack getPlayerHead(Player p, int amount, String displayname, String... lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        new BukkitRunnable() {
            @Override
            public void run() {
                SkullMeta skull = (SkullMeta) item.getItemMeta();
                skull.setDisplayName(displayname);
                ArrayList<String> metaLore = new ArrayList();
                String[] var7 = lore;
                int var8 = lore.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    String loreComments = var7[var9];
                    metaLore.add(loreComments);
                }

                skull.setLore(metaLore);
                skull.setOwningPlayer(p);
                item.setItemMeta(skull);
            }

        }.runTaskAsynchronously(Main.getInstance());
        return item;
    }

}
