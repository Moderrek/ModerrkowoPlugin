/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.events;

import pl.moderr.moderrkowo.database.ModerrkowoDatabase;

import javax.annotation.Nullable;

public class DatabaseLog {

    private String sender = null;
    private String message = null;
    private LogAction action = null;
    private LogResult result = null;
    private Exception exception = null;

    /**
     * Constructor DatabaseLog
     * @param logSenderSourceName nazwa wywołującego, który wykonał to polecenie
     * @param logMessage zwrócona wiadomość
     * @param logAction typ wykonanej akcji
     * @param logResult wynik
     */
    public DatabaseLog(String logSenderSourceName, String logMessage, LogAction logAction, LogResult logResult){
        this.sender = logSenderSourceName;
        this.message = logMessage;
        this.action = logAction;
        this.result = logResult;
    }

    /**
     * Constructor DatabaseLog INFO
     * @param message Ustawiona wiadomosc bazy danych
     */
    public DatabaseLog(String message){
        this.sender = ModerrkowoDatabase.getInstance().getName();
        this.message = message;
        this.action = LogAction.INFO;
        this.result = LogResult.INFO;
    }

    /**
     * Constructor DatabaseLog INFO Exception
     * @param message Ustawiona wiadomosc bazy danych
     * @param result Ustawiony wynik wydarzenia
     */
    public DatabaseLog(String message, LogResult result){
        this.sender = ModerrkowoDatabase.getInstance().getName();
        this.message = message;
        this.action = LogAction.INFO;
        this.result = result;
    }

    /**
     * Constructor DatabaseLog Action Exception
     * @param message Ustawiona wiadomosc bazy danych
     * @param action W jakiej akcji został wykonany
     * @param result Ustawiony wynik wydarzenia
     */
    public DatabaseLog(String message, LogAction action, LogResult result){
        this.sender = ModerrkowoDatabase.getInstance().getName();
        this.message = message;
        this.action = action;
        this.result = result;
    }

    /**
     * @param message Wiadomość
     * @param action Przy jakiej akcji stworzono log
     */
    public DatabaseLog(String message, LogAction action){
        this.sender = ModerrkowoDatabase.getInstance().getName();
        this.message = message;
        this.action = action;
        this.result = LogResult.INFO;
    }

    /**
     * @param message Wiadomość
     * @param action Przy jakiej akcji stworzono log
     * @param result Wynik akcji
     * @param exception Błąd
     */
    public DatabaseLog(String message, LogAction action, LogResult result, Exception exception){
        this.sender = ModerrkowoDatabase.getInstance().getName();
        this.message = message;
        this.action = action;
        this.result = result;
        this.exception = exception;
    }

    /**
     * Zwraca nazwe wykonywującego
     * @return SenderName
     */
    public String getSender() {
        return sender;
    }
    /**
     * Zwraca wiadomość
     * @return Message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Podczas czego był wypuszczony log
     * @return Action
     */
    public LogAction getAction() {
        return action;
    }
    /**
     * Wynik
     * @return Result
     */
    public LogResult getResult() {
        return result;
    }
    /**
     * Zwraca exception jeżeli wystąpił jakiś błąd może być NULL
     * @return Exception
     */
    @Nullable
    public Exception getException(){
        return exception;
    }

}
