package pl.moderr.moderrkowo.core.cuboids;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.cuboids.commands.CuboidCommand;
import pl.moderr.moderrkowo.core.cuboids.listeners.PlaceRemoveCuboid;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ModerrkowoLog;

import java.util.Objects;

public class ModerrCuboids {

    private static ModerrCuboids instance;
    private static Material cuboidMaterial;
    private static String cuboidDisplayName;

    public void start(){
        instance = this;
        try{
            cuboidMaterial = Material.valueOf("LODESTONE");
            cuboidDisplayName = ColorUtils.color("&aDziałka 64x64");
            Main.getInstance().getServer().getPluginManager().registerEvents(new PlaceRemoveCuboid(), Main.getInstance());
            Objects.requireNonNull(Main.getInstance().getCommand("dzialka")).setExecutor(new CuboidCommand());
            ModerrkowoLog.LogAdmin("Wczytano działki.");
        }catch (Exception e){
            e.printStackTrace();
            ModerrkowoLog.LogAdmin("Wystąpił bład przy tworzeniu działek!");
        }
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "dzialka");
        if(Bukkit.getRecipe(key) == null){
            try{
                ShapedRecipe recipe = new ShapedRecipe(key, getCuboid(1));
                recipe.shape(
                        "ABA",
                        "BDB",
                        "ABA"
                );
                recipe.setIngredient('A', Material.POLISHED_ANDESITE);
                recipe.setIngredient('B', Material.BLAZE_POWDER);
                recipe.setIngredient('D', Material.DIAMOND_BLOCK);
                Bukkit.addRecipe(recipe);
            }catch(Exception ignored){

            }
        }
    }

    public static ItemStack getCuboid(int count){
        if(count > 64 || count <= 0){
            return null;
        }
        ItemStack item = new ItemStack(cuboidMaterial, count);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(cuboidDisplayName);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static String getCuboidNamePrefix(){
        return "_cuboids_";
    }
    public static ModerrCuboids getInstance(){
        return instance;
    }

}
