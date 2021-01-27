/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public class User {

    private final UUID _UUID;
    private final String _NAME;

    public User(UUID uuid, String playerName){
        this._UUID = uuid;
        this._NAME = playerName;
    }

    // Getters
    public UUID getUUID() {
        return _UUID;
    }
    public String getName() {
        return _NAME;
    }

    /**
     * @return Zwraca instacje gracza
     */
    @Nullable
    public Player getPlayer(){
        return Bukkit.getPlayer(_UUID);
    }
}
