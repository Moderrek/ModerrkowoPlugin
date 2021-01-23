/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.data;

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
}
