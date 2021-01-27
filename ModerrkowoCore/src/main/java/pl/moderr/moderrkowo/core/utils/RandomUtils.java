package pl.moderr.moderrkowo.core.utils;

import org.bukkit.Location;
import org.bukkit.World;
import pl.moderr.moderrkowo.core.Main;

import java.util.Random;

public class RandomUtils {

    public static Location getRandom(World world) {
        Random rand = new Random();
        int rangeMax = +(Main.getInstance().getBorder(world) / 2);
        int rangeMin = -(Main.getInstance().getBorder(world) / 2);

        int X = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Y = world.getHighestBlockYAt(X, Z);


        return new Location(world, X, Y, Z).add(0.5, 1, 0.5);
    }

    public static int getRandomInt(int rangeMin, int rangeMax) {
        Random rand = new Random();
        return rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
    }

}
