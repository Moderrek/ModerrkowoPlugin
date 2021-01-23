/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.callback;

import pl.moderr.moderrkowo.database.data.User;

public interface CallbackUserLoad extends Callback<User>{

    default void IfUserIsRegistered(User user){

    }
    default void IfUserIsNotRegistered(){

    }

}
