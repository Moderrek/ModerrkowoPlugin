/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.exceptions;

public class PlayerIsOffline extends Exception{

    public PlayerIsOffline(){
        super("Gracz jest offline!");
    }

}
