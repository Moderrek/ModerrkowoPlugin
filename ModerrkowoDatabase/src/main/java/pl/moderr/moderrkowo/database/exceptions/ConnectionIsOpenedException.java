/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.exceptions;

public class ConnectionIsOpenedException extends Exception{

    public ConnectionIsOpenedException(String errorMessage){
        super(errorMessage);
    }

}
