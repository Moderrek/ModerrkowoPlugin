package pl.moderr.moderrkowo.core;

import org.bukkit.boss.BossBar;

import java.util.UUID;

public class AntyLogoutItem {

    public UUID uuid;
    public int secounds;
    public BossBar bossBar;

    public AntyLogoutItem(UUID uuid, int secounds, BossBar bossBar){
        this.uuid = uuid;
        this.secounds = secounds;
        this.bossBar = bossBar;
    }

}
