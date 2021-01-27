package pl.moderr.moderrkowo.core.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Reference {
    public static HashMap<Material, ArrayList<Material>> MINABLE = new HashMap<Material, ArrayList<Material>>();

    public static ArrayList<Material> DIGGABLE = new ArrayList<Material>();

    public static ArrayList<Material> MINABLE_SILKTOUCH = new ArrayList<Material>(Arrays.asList(
            Material.STONE,
            Material.COAL_ORE,
            Material.REDSTONE_ORE,
            Material.LAPIS_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.ICE,
            Material.NETHER_QUARTZ_ORE,
            Material.GLOWSTONE
    ));

    public static ArrayList<Material> DIGGABLE_SILKTOUCH = new ArrayList<Material>(Arrays.asList(
            Material.GRASS,
            Material.CLAY,
            Material.SNOW_BLOCK,
            Material.MYCELIUM,
            Material.GLOWSTONE
    ));

    public static HashMap<Material, Material> MINABLE_FORTUNE;

    static {
        MINABLE_FORTUNE = new HashMap<Material, Material>();

        MINABLE_FORTUNE.put(Material.COAL_ORE, Material.COAL);
        MINABLE_FORTUNE.put(Material.REDSTONE_ORE, Material.REDSTONE);
        MINABLE_FORTUNE.put(Material.LAPIS_ORE, Material.LAPIS_LAZULI);
        MINABLE_FORTUNE.put(Material.DIAMOND_ORE, Material.DIAMOND);
        MINABLE_FORTUNE.put(Material.EMERALD_ORE, Material.EMERALD);
        MINABLE_FORTUNE.put(Material.NETHER_QUARTZ_ORE, Material.QUARTZ);
        MINABLE_FORTUNE.put(Material.GLOWSTONE, Material.GLOWSTONE_DUST);
    }

    ;

    public static HashMap<Material, Material> DIGGABLE_FORTUNE;

    static {
        DIGGABLE_FORTUNE = new HashMap<Material, Material>();

        DIGGABLE_FORTUNE.put(Material.GRAVEL, Material.FLINT);
        DIGGABLE_FORTUNE.put(Material.GLOWSTONE, Material.GLOWSTONE_DUST);
    }

    ;

    public static ArrayList<Material> PICKAXES = new ArrayList<Material>(Arrays.asList(
            Material.WOODEN_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE
    ));

    public static ArrayList<Material> SPADES = new ArrayList<Material>(Arrays.asList(
            Material.WOODEN_SHOVEL,
            Material.STONE_SHOVEL,
            Material.IRON_SHOVEL,
            Material.GOLDEN_SHOVEL,
            Material.DIAMOND_SHOVEL
    ));
}
