/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.callback;

public interface Callback<T> {
    void onDone(T result);
    void onFail(Exception cause);
}
