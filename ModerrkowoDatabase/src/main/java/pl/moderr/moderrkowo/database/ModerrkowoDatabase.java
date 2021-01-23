/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.moderr.moderrkowo.database.callback.CallbackEmpty;
import pl.moderr.moderrkowo.database.callback.CallbackExists;
import pl.moderr.moderrkowo.database.callback.CallbackUserLoad;
import pl.moderr.moderrkowo.database.data.User;
import pl.moderr.moderrkowo.database.events.DatabaseLog;
import pl.moderr.moderrkowo.database.events.LogAction;
import pl.moderr.moderrkowo.database.events.LogResult;
import pl.moderr.moderrkowo.database.events.ModerrDatabaseListener;
import pl.moderr.moderrkowo.database.exceptions.ConnectionIsNotOpenedException;
import pl.moderr.moderrkowo.database.exceptions.ConnectionIsOpenedException;
import pl.moderr.moderrkowo.database.exceptions.ConnectionReconnectException;
import pl.moderr.moderrkowo.database.exceptions.DatabaseUserNotExists;
import pl.moderr.moderrkowo.database.managers.ModerrkowoUserManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ModerrkowoDatabase extends JavaPlugin{


    private static ModerrkowoDatabase instance;
    private ModerrkowoUserManager manager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveConfig();

        host = getConfig().getString("host");
        port = getConfig().getString("port");
        database = getConfig().getString("database");
        username = getConfig().getString("username");
        password = getConfig().getString("password");
        table_user = getConfig().getString("table-user");
        sendLog(new DatabaseLog("Wczytywanie..."));
        try {
            openConnection();
        } catch (ConnectionIsOpenedException e) {
            sendLog(new DatabaseLog("Nie udało się otworzyć połączenie, ponieważ jest już ono otworzone", LogResult.EXCEPTION));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendLog(new DatabaseLog("Wystąpił problem podczas łączenia", LogResult.EXCEPTION));
        }
        manager = new ModerrkowoUserManager();
        getServer().getPluginManager().registerEvents(manager, this);
        sendLog(new DatabaseLog("Włączono plugin"));
    }

    @Override
    public void onDisable() {
        sendLog(new DatabaseLog("Wyłączanie..."));
    }

    // Instances

    /**
     * @return Zwraca instancje do Menedżera Użytkowników
     */
    public ModerrkowoUserManager getUserManager(){
        return manager;
    }
    /**
     * @return Zwraca instancje tej klasy
     */
    public static ModerrkowoDatabase getInstance(){
        return instance;
    }

    // Listener
    private List<ModerrDatabaseListener> listeners = new ArrayList<ModerrDatabaseListener>();

    /**
     * Jeżeli chcesz aby odbierać informacje od biblioteki dodaj swoją klasę typu ModerrDatabaseListener do nasłuchiwania
     * @param listenerClass Twoja klasa
     */
    public void registerDatabaseListener(ModerrDatabaseListener listenerClass){
        listeners.add(listenerClass);
    }
    public void sendLog(DatabaseLog log){
        for(ModerrDatabaseListener listener : listeners){
            listener.onLog(log);
        }
    }
    public void sendException(DatabaseLog log){
        for(ModerrDatabaseListener listener : listeners){
            listener.onException(log);
        }
    }
    public void sendFail(DatabaseLog log){
        for(ModerrDatabaseListener listener : listeners){
            listener.onFail(log);
        }
    }
    public void sendUserLoaded(User u){
        for(ModerrDatabaseListener listener : listeners){
            listener.onLoadUser(u);
        }
    }

    // Database

    private String host, port, database, username, password, table_user;
    private Connection connection = null;

    /**
     * Otwiera połączenie z baza danych
     * Wartości muszą być ustawione w <b>config.yml</b>
     * @throws ConnectionIsOpenedException Wywołane kiedy połączenie jest już otwarte
     * @throws ClassNotFoundException Wywołane kiedy nie znaleziono sterownika SQL
     * @throws SQLException Wywołane kiedy w połączeniu wystąpił błąd
     */
    public void openConnection() throws ConnectionIsOpenedException, ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true", host, port, database, username, password));
        try {
            CheckTables();
        } catch (ConnectionReconnectException e) {
            sendLog(new DatabaseLog("Połączenie było zamknięte i nie udało się go otworzyć", LogResult.EXCEPTION));
        } catch (ConnectionIsNotOpenedException e) {
            sendLog(new DatabaseLog("Połączenie było zamknięte", LogResult.EXCEPTION));
        }
    }
    /**
     * Sprawdza tabele, jeżeli nie ma tabeli to ją tworzy
     * @throws ConnectionReconnectException Wywołane kiedy nie udało się przywrócić połączenia
     * @throws SQLException Wywołane kiedy wystąpił błąd w skrypcie SQL
     * @throws ConnectionIsNotOpenedException Wywołane kiedy połaczenie jest zamknięte
     */
    private void CheckTables() throws ConnectionReconnectException, SQLException, ConnectionIsNotOpenedException {
        String sqlCreate = String.format("CREATE TABLE IF NOT EXISTS `%s`.`%s` ( `UUID` TEXT NOT NULL, `NAME` TEXT NOT NULL)", database, table_user);
        Statement stmt = getConnection(true).createStatement();
        stmt.executeUpdate(sqlCreate);
    }
    /**
     * Otwiera ponownie połączenie (rozłącza i łączy)
     * @throws ConnectionReconnectException Wywołane kiedy nie udało się ponowić połączenia
     */
    public void reconnectConnection() throws ConnectionReconnectException {
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
            openConnection();
        } catch (ConnectionIsOpenedException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new ConnectionReconnectException("Wystąpił problem z ponowieniem połączenia!");
        }
    }
    /**
     * @param reconnect Podana wartość wskazuje czy jak połączenie jest zamknięte czy je otworzyć
     * @return Zwraca otwarte połączenie z bazą danych
     * @throws ConnectionIsNotOpenedException Wywołane kiedy połączenie jest zamknięte
     * @throws SQLException Wywołane kiedy wystąpił błąd z skryptem SQL
     * @throws ConnectionReconnectException Wywołane kiedy nie udało się ponownie połączyć <b>(JEŻELI JEST RECONNECT = TRUE)</b>
     */
    public Connection getConnection(boolean reconnect) throws ConnectionIsNotOpenedException, SQLException, ConnectionReconnectException {
        if(!getConnectionState()){
            if(reconnect){
                reconnectConnection();
            }else{
                throw new ConnectionIsNotOpenedException("Połaczenie jest zamknięte!");
            }
        }
        return connection;
    }
    /**
     * @return Zwraca wartość true, false w zależności czy połączenie z bazą danych jest otwarte
     * @throws SQLException Wywołane kiedy wystąpi błąd ze skryptem SQL
     */
    public boolean getConnectionState() throws SQLException{
        return connection != null && !connection.isClosed();
    }

    /**
     * Prosta metoda do zwrotu wyniku z SQL
     * @param SQL Podany skrypt SQL
     * @return Zwraca wynik
     * @throws SQLException Wywołany podczas błędu skryptu
     */
    public ResultSet Query(String SQL) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(SQL);
    }

    //<editor-fold desc="Metody do zarządzania użytkownikiem">

    /**
     * Zwraca użytkownika po ID z bazy danych
     * @param userId UUID gracza
     * @param callback Informacje zwrotne
     */
    public void getUser(UUID userId, CallbackUserLoad callback){
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba pobrania użytkownika", LogAction.TRY));
            User user = null;
            try{
                ResultSet rs = Query(String.format("SELECT * FROM `%s` WHERE `UUID`='%s'", table_user, userId));
                if(rs.next()){
                    user = new User(UUID.fromString(rs.getString("UUID")), rs.getString("NAME"));
                    callback.IfUserIsRegistered(user);
                }else{
                    callback.IfUserIsNotRegistered();
                    callback.onFail(new DatabaseUserNotExists());
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas pobierania użytkownika", LogAction.GET, LogResult.EXCEPTION, e));
                return;
            }
            User finalUser = user;
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone(finalUser);
                sendLog(new DatabaseLog("Udało się pobrać użytkownika", LogAction.GET, LogResult.SUCCESS));
            });
        });
    }
    /**
     * Zwraca wartość czy gracz już jest w bazie danych
     * @param userId UUID gracza
     * @param callback Informacje zwrotne
     */
    public void existsUser(UUID userId, CallbackExists callback){
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba sprawdzenia czy użytkownik istnieje", LogAction.TRY));
            boolean result;
            try{
                ResultSet rs = Query(String.format("SELECT * FROM `%s` WHERE `UUID`='%s'", table_user, userId));
                result = rs.next();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas sprawdzenia czy użytkownik istnieje", LogAction.EXISTS, LogResult.EXCEPTION, e));
                return;
            }
            boolean finalResult = result;
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone(finalResult);
                sendLog(new DatabaseLog("Udało się sprawdzić czy użytkownik istnieje", LogAction.EXISTS, LogResult.SUCCESS));
            });
        });
    }
    /**
     * Wstawia gracza do bazy danych
     * @param userInstance Instancja użytkownika
     * @param callback Informacje zwrotne
     */
    public void insertUser(User userInstance, CallbackEmpty callback){
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba stworzenia użytkownika", LogAction.TRY));
            try{
                String SQL = String.format("INSERT INTO `%s` (`UUID`,`NAME`) VALUES (?,?)", table_user);
                PreparedStatement preparedStatement = getConnection(true).prepareStatement(SQL);
                preparedStatement.setString(1, userInstance.getUUID().toString());
                preparedStatement.setString(2, userInstance.getName());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (ConnectionReconnectException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas ponownego otworzenia połączenia", LogAction.INSERT, LogResult.EXCEPTION, e));
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd w skrypcie SQL", LogAction.INSERT, LogResult.EXCEPTION, e));
                return;
            } catch (ConnectionIsNotOpenedException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd, ponieważ połączenie jest zamknięte", LogAction.INSERT, LogResult.EXCEPTION, e));
                return;
            }
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone();
                sendLog(new DatabaseLog("Udało się wstawić nowego użytkownika", LogAction.INSERT, LogResult.SUCCESS));
            });
        });
    }
    /**
     * Aktualizuje informacje o użytkowniku w bazie danych
     * @param userInstance Instancja użytkownika
     * @param callback Informacje zwrotne
     */
    public void updateUser(User userInstance, CallbackEmpty callback){
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba zaaktualizowania użytkownika", LogAction.TRY));
            try {
                String SQL = String.format("UPDATE `%s` SET `UUID`=?, `NAME`=? WHERE UUID=?", table_user); // SQL FORMAT
                PreparedStatement preparedStatement = getConnection(true).prepareStatement(SQL);
                preparedStatement.setString(1, userInstance.getUUID().toString()); // SET UUID
                preparedStatement.setString(2, userInstance.getName()); // SET NAME
                preparedStatement.setString(3, userInstance.getUUID().toString()); // WHERE UUID
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (ConnectionReconnectException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas ponownego otworzenia połączenia", LogAction.UPDATE, LogResult.EXCEPTION, e));
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd w skrypcie SQL", LogAction.UPDATE, LogResult.EXCEPTION, e));
                return;
            } catch (ConnectionIsNotOpenedException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd, ponieważ połączenie jest zamknięte", LogAction.UPDATE, LogResult.EXCEPTION, e));
                return;
            }
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone();
                sendLog(new DatabaseLog("Udało się zaaktualizować użytkownika", LogAction.UPDATE, LogResult.SUCCESS));
            });
        });
    }

    //</editor-fold>

}
