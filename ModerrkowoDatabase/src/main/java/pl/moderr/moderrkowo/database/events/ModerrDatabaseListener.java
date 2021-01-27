/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.events;

import pl.moderr.moderrkowo.database.data.User;

public interface ModerrDatabaseListener {

    /**
     * Zostanie wykonany kiedy odbierze log
     * @param log Odebrana wartość
     */
    default void onLog(DatabaseLog log) {

    }
    /**
     * Zostanie wykonane kiedy zostanie załadowany użytkownik
     * @param user Użytkownik
     */
    default void onLoadUser(User user){
    }
    /**
     * Zostanie wykonane kiedy zostanie rozładowany użytkownik
     * @param user Użytkownik
     */
    default void onUnloadUser(User user){

    }
    /**
     * Zostanie wykonane kiedy użytkownik zostanie zapisany
     * @param user Użytkownik
     */
    default void onSaveUser(User user){

    }

    /**
     * Zostanie wykonane kiedy użytkownik zostanie zarejestrowany
     * @param user Użytkownik
     */
    default void onRegisterUser(User user){

    }

}
