package pl.moderr.moderrkowo.database.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Home {

    public UUID uuid;
    public String world;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;

    public Home(UUID uuid, String world, double x, double y, double z, float yaw, float pitch){
        this.uuid = uuid;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location ToLocation(){
        return new Location(Bukkit.getWorld("world"), x,y,z,yaw,pitch);
    }

}
