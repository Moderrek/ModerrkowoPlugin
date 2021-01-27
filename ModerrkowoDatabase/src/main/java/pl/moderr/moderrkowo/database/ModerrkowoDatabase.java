/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.moderr.moderrkowo.database.callback.*;
import pl.moderr.moderrkowo.database.data.Home;
import pl.moderr.moderrkowo.database.data.RynekItem;
import pl.moderr.moderrkowo.database.data.User;
import pl.moderr.moderrkowo.database.events.DatabaseLog;
import pl.moderr.moderrkowo.database.events.LogAction;
import pl.moderr.moderrkowo.database.events.LogResult;
import pl.moderr.moderrkowo.database.events.ModerrDatabaseListener;
import pl.moderr.moderrkowo.database.exceptions.*;
import pl.moderr.moderrkowo.database.managers.ModerrkowoUserManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public final class ModerrkowoDatabase extends JavaPlugin{

    private static ModerrkowoDatabase instance;
    private ModerrkowoUserManager manager;

    @Override
    public void onEnable() {
        instance = this;
        sendLog(new DatabaseLog("Wczytywanie..."));

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

        try {
            openConnection();
            sendLog(new DatabaseLog("Otworzono połączenie!"));
        } catch (ConnectionIsOpenedException e) {
            sendLog(new DatabaseLog("Nie udało się otworzyć połączenie, ponieważ jest już ono otworzone", LogResult.EXCEPTION));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            sendLog(new DatabaseLog("Wystąpił problem podczas łączenia", LogResult.EXCEPTION));
        }

        manager = new ModerrkowoUserManager();

        // Wczytanie użytkowników
        sendLog(new DatabaseLog("Wczytywanie wszystkich użytkowników online"));
        int wczytano = 0;
        int ileDoWczytania = Bukkit.getOnlinePlayers().size();
        for(Player p : Bukkit.getOnlinePlayers()){
            sendLog(new DatabaseLog("   Wczytywanie " + p.getName(), LogAction.UserManager_SAVE));
            try {
                manager.DatabaseLoadUser(p.getUniqueId());
                sendLog(new DatabaseLog(" + Wczytano " + p.getName(), LogAction.UserManager_SAVE, LogResult.SUCCESS));
                wczytano++;
            } catch (UserIsAlreadyLoaded userIsAlreadyLoaded) {
                sendLog(new DatabaseLog(" - Nie wczytano " + p.getName(), LogAction.UserManager_SAVE, LogResult.EXCEPTION, userIsAlreadyLoaded));
            }
        }
        sendLog(new DatabaseLog("Zapisano wszystkich użytkowników online! (" + wczytano + "/" + ileDoWczytania + ")"));
        // END Wczytano użytkowników

        Bukkit.getPluginManager().registerEvents(manager, this);
        sendLog(new DatabaseLog("Włączono plugin"));
    }
    @Override
    public void onDisable() {
        sendLog(new DatabaseLog("Wyłączanie..."));
        // Zapisywanie użytkowników
        try{
            sendLog(new DatabaseLog("Zapisywanie wszystkich użytkowników online"));
            int i = 0;
            User[] allUsers = manager.getAllUsers();
            int total = allUsers.length;
            for(User users : allUsers){
                sendLog(new DatabaseLog("   Zapisywanie " + users.getName(), LogAction.UserManager_SAVE));
                try {
                    manager.UnloadUser(users, true);
                    sendLog(new DatabaseLog(" + Zapisano " + users.getName(), LogAction.UserManager_SAVE, LogResult.SUCCESS));
                    i++;
                } catch (UserNotLoaded userNotLoaded) {
                    userNotLoaded.printStackTrace();
                    sendLog(new DatabaseLog(" - Nie zapisywano " + users.getName(), LogAction.UserManager_SAVE, LogResult.EXCEPTION, userNotLoaded));
                }
            }
            sendLog(new DatabaseLog("Zapisano wszystkich użytkowników online! (" + i + "/" + total + ")"));
        }catch(Exception e){
            e.printStackTrace();
        }
        // END Zapisano użytkowników
        sendLog(new DatabaseLog("Wyłączono plugin"));
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
    private final List<ModerrDatabaseListener> listeners = new ArrayList<ModerrDatabaseListener>();

    /**
     * Jeżeli chcesz aby odbierać informacje od biblioteki dodaj swoją klasę typu ModerrDatabaseListener do nasłuchiwania
     * @param listenerClass Twoja klasa
     */
    public void registerDatabaseListener(ModerrDatabaseListener listenerClass){
        listeners.add(listenerClass);
    }
    public void sendLog(DatabaseLog log) {
        //Bukkit.broadcastMessage(log.getResult() + " >> " + log.getMessage() + " | " + log.getAction());
        /*try{
            SimpleDateFormat fileName = new SimpleDateFormat("MM_DD");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            String s = "";
            if(log.getAction() != LogAction.INFO){
                s = "[" + log.getAction() + "]";
            }
            String s1 = "";
            if(log.getException() != null){
                s1 = "\nE " + Arrays.toString(log.getException().getStackTrace());
            }
            String str = String.format("[%s %s] %s: %s %s%s", sdf.format(new java.util.Date()), log.getAction(), log.getSender(), log.getMessage(), s, s1);
            BufferedWriter writer = new BufferedWriter(new FileWriter(getDataFolder() + File.separator +  fileName.format(new java.util.Date()) + "database.txt", true));
            writer.append(str);
        }catch(Exception ignored){

        }*/

        if(log.getException() != null){
            getLogger().log(Level.SEVERE, "[" + log.getSender() + "] [" + log.getResult() +"] " + log.getAction() + ": " + log.getMessage());
            log.getException().printStackTrace();
        }else{
            System.out.println("[" + log.getSender() + "] [" + log.getResult() +"] " + log.getAction() + ": " + log.getMessage());
        }
        if(listeners.size() > 0){
            for(ModerrDatabaseListener listener : listeners){
                listener.onLog(log);
            }
        }
    }
    public void sendUserLoaded(User u){
        for(ModerrDatabaseListener listener : listeners){
            listener.onLoadUser(u);
        }
    }
    public void sendUserUnloaded(User u){
        for(ModerrDatabaseListener listener : listeners){
            listener.onUnloadUser(u);
        }
    }
    public void sendUserSave(User u){
        for(ModerrDatabaseListener listener : listeners){
            listener.onSaveUser(u);
        }
    }
    public void sendUserRegister(User u){
        for(ModerrDatabaseListener listener : listeners){
            listener.onRegisterUser(u);
        }
    }

    // Database

    private String host, port, database, username, password, table_user;
    private final String homesTable = "homes";
    private final String rynekTable = "rynek";
    private Connection connection = null;

    public void repairConnection() {
        try {
            connection.createStatement().close();
        } catch (Exception e) {
            try {
                getConnection(true);
                connection.createStatement().close();
            } catch (SQLException | ConnectionReconnectException | ConnectionIsNotOpenedException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Otwiera połączenie z baza danych
     * Wartości muszą być ustawione w <b>config.yml</b>
     * @throws ConnectionIsOpenedException Wywołane kiedy połączenie jest już otwarte
     * @throws ClassNotFoundException Wywołane kiedy nie znaleziono sterownika SQL
     * @throws SQLException Wywołane kiedy w połączeniu wystąpił błąd
     */
    public void openConnection() throws ConnectionIsOpenedException, ClassNotFoundException, SQLException {
        String s = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSLL=false&autoReconnect=true", host, port, database, username, password);
        System.out.println("Connection >> " + s);
        connection = DriverManager.getConnection(s);
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
        stmt.close();
        try {
            initializeHomeTable();
            initializeRynekTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void initializeHomeTable() throws SQLException, ClassNotFoundException, ConnectionIsNotOpenedException, ConnectionReconnectException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + database + "`.`" + homesTable + "` ( `UUID` TEXT NOT NULL, `WORLD` TEXT NOT NULL, `X` DOUBLE NOT NULL, `Y` DOUBLE NOT NULL, `Z` DOUBLE NOT NULL, `YAW` FLOAT NOT NULL, `PITCH` FLOAT NOT NULL)";
        Statement stmt = getConnection(true).createStatement();
        stmt.executeUpdate(sqlCreate);
        stmt.close();
    }
    public void initializeRynekTable() throws SQLException, ClassNotFoundException, ConnectionIsNotOpenedException, ConnectionReconnectException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS `" + database + "`.`" + rynekTable + "` ( `UUID` TEXT NOT NULL, `ITEM` TEXT NOT NULL, `COST` int(11) NOT NULL )";
        Statement stmt = getConnection(true).createStatement();
        stmt.executeUpdate(sqlCreate);
        stmt.close();
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
        repairConnection();
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
        repairConnection();
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
        repairConnection();
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
        repairConnection();
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

    //<editor-fold> RYNEK
    public void getRynek(CallbackRynek callback){
        repairConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba pobrania rynku", LogAction.TRY));
            ArrayList<RynekItem> items = new ArrayList<>();
            try{
                ResultSet rs = Query(String.format("SELECT * FROM `%s`", rynekTable));
                while(rs.next()){
                    items.add(new RynekItem(UUID.fromString(rs.getString("UUID")), JsonItemStack.fromJson(rs.getString("ITEM")), rs.getInt("COST")));
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas pobierania rynku", LogAction.GET, LogResult.EXCEPTION, e));
                return;
            }
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone(items);
                sendLog(new DatabaseLog("Udało się pobrać rynek", LogAction.GET, LogResult.SUCCESS));
            });
        });
    }
    public void setRynek(ArrayList<RynekItem> rynekItems){
        repairConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba stworzenia użytkownika", LogAction.TRY));
            try{
                getConnection(true).createStatement().execute("TRUNCATE `" + rynekTable + "`");
                if(rynekItems.size() > 0){
                    for(RynekItem item : rynekItems) {
                        String SQL = String.format("INSERT INTO `%s` (`UUID`,`ITEM`,`COST`) VALUES (?,?,?)", rynekTable);
                        PreparedStatement preparedStatement = getConnection(true).prepareStatement(SQL);
                        preparedStatement.setString(1, item.owner.toString());
                        preparedStatement.setString(2, JsonItemStack.toJson(item.item));
                        preparedStatement.setInt(3, item.cost);
                        preparedStatement.execute();
                        preparedStatement.close();
                    }
                }
            } catch (ConnectionReconnectException | ConnectionIsNotOpenedException | SQLException e) {
                e.printStackTrace();
            }
        });
    }
    //</editor-fold>


    public void existsHome(UUID uuid, CallbackExists callback) {
        repairConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba sprawdzenia czy dom użytkownika istnieje", LogAction.TRY));
            boolean result;
            try{
                ResultSet rs = Query(String.format("SELECT * FROM `%s` WHERE `UUID`='%s'", homesTable, uuid));
                result = rs.next();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas sprawdzenia czy dom użytkownika istnieje", LogAction.EXISTS, LogResult.EXCEPTION, e));
                return;
            }
            boolean finalResult = result;
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone(finalResult);
                sendLog(new DatabaseLog("Udało się sprawdzić czy dom użytkownika istnieje", LogAction.EXISTS, LogResult.SUCCESS));
            });
        });
    }
    public void getHome(UUID uuid, CallbackHomeLoad callback) {
        repairConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            sendLog(new DatabaseLog("Próba pobrania użytkownika", LogAction.TRY));
            Home home = null;
            try{
                ResultSet rs = Query(String.format("SELECT * FROM `%s` WHERE `UUID`='%s'", homesTable, uuid));
                if(rs.next()){
                    home = new Home(uuid, rs.getString("WORLD"), rs.getDouble("X"), rs.getDouble("Y"), rs.getDouble("Z"), rs.getFloat("YAW"), rs.getFloat("PITCH"));
                }else{
                    callback.onFail(new DatabaseUserNotExists());
                    return;
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onFail(e);
                sendLog(new DatabaseLog("Wystąpił błąd podczas pobierania domu", LogAction.GET, LogResult.EXCEPTION, e));
                return;
            }
            Home finalHome = home;
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone(finalHome);
                sendLog(new DatabaseLog("Udało się pobrać dom", LogAction.GET, LogResult.SUCCESS));
            });
        });
    }
    public void insertHome(UUID uuid, Home home, CallbackEmpty callback) {
        repairConnection();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            System.out.println("TRY >> insertHome");
            try {
                Location loc = home.ToLocation();
                String SQL = "INSERT INTO `" + homesTable + "` (`UUID`,`WORLD`,`X`,`Y`,`Z`,`YAW`,`PITCH`) VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = getConnection(true).prepareStatement(SQL);
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, loc.getWorld().getName());
                preparedStatement.setDouble(3, loc.getX());
                preparedStatement.setDouble(4, loc.getY());
                preparedStatement.setDouble(5, loc.getZ());
                preparedStatement.setFloat(6, loc.getYaw());
                preparedStatement.setFloat(7, loc.getPitch());
                preparedStatement.execute();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFail(e);
                System.out.println("FAIL >> insertHome");
                return;
            }
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone();
                System.out.println("SUCCESS >> insertHome");
            });
        });
    }
    public void updateHome(UUID uuid, Home home, CallbackEmpty callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            System.out.println("TRY >> updateHome");
            try {
                Location loc = home.ToLocation();
                String SQL = "UPDATE `" + homesTable + "` SET `WORLD`=?, `X`=?, `Y`=?, `Z`=?, `YAW`=?, `PITCH`=? WHERE UUID=?";
                PreparedStatement preparedStatement = getConnection(true).prepareStatement(SQL);
                preparedStatement.setString(1, loc.getWorld().getName());
                preparedStatement.setDouble(2, loc.getX());
                preparedStatement.setDouble(3, loc.getY());
                preparedStatement.setDouble(4, loc.getZ());
                preparedStatement.setFloat(5, loc.getYaw());
                preparedStatement.setFloat(6, loc.getPitch());
                preparedStatement.setString(7, uuid.toString());
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onFail(e);
                System.out.println("FAIL >> updateHome");
                return;
            }
            Bukkit.getScheduler().runTask(this, () -> {
                callback.onDone();
                System.out.println("SUCCESS >> updateHome");
            });
        });
    }

}
