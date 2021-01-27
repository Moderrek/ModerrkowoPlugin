package pl.moderr.moderrkowo.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static Location getLoc(String name, double x, double z) {
        return new Location(Bukkit.getWorld(name), x + 0.5, 70.5, z + 0.5);
    }

}
