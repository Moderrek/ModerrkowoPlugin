/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.callback;

public interface CallbackEmpty {
    void onDone();
    void onFail(Exception cause);
}
