/*
 * This file is a part of ModerrkowoDatabase
 *
 * Copyright (c) 2020-2021. All rights reserved.
 *
 */
package pl.moderr.moderrkowo.database.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.moderr.moderrkowo.database.ModerrkowoDatabase;
import pl.moderr.moderrkowo.database.callback.CallbackEmpty;
import pl.moderr.moderrkowo.database.callback.CallbackExists;
import pl.moderr.moderrkowo.database.callback.CallbackUserLoad;
import pl.moderr.moderrkowo.database.data.User;
import pl.moderr.moderrkowo.database.events.DatabaseLog;
import pl.moderr.moderrkowo.database.events.LogAction;
import pl.moderr.moderrkowo.database.events.LogResult;
import pl.moderr.moderrkowo.database.exceptions.PlayerIsOffline;
import pl.moderr.moderrkowo.database.exceptions.UserIsAlreadyLoaded;
import pl.moderr.moderrkowo.database.exceptions.UserNotLoaded;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ModerrkowoUserManager implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Próba załadowania gracza...", LogAction.TRY));
        try {
            DatabaseLoadUser(e.getPlayer().getUniqueId());
            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Załadowano gracza pomyślnie", LogAction.UserManager_LOAD, LogResult.SUCCESS));
        } catch (UserIsAlreadyLoaded userIsAlreadyLoaded) {
            userIsAlreadyLoaded.printStackTrace();
            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Nie udało się załadować gracza, ponieważ już był załadowany", LogAction.UserManager_LOAD, LogResult.EXCEPTION, userIsAlreadyLoaded));
        }
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e){
        ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Próba rozładowania gracza...", LogAction.TRY));
        try {
            UnloadUser(e.getPlayer().getUniqueId(), true);
            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Rozładowano gracza pomyślnie", LogAction.UserManager_UNLOAD, LogResult.SUCCESS));
        } catch (UserNotLoaded userNotLoaded) {
            userNotLoaded.printStackTrace();
            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Nie udało się rozładować gracza, ponieważ nie był załadowany", LogAction.UserManager_UNLOAD, LogResult.EXCEPTION, userNotLoaded));
        }
    }

    private HashMap<UUID, User> LoadedUsers = new HashMap<>();

    /**
     * Zwraca użytkownika
     * @param uuid Identyfikator gracza
     * @return Zwraca instancje/dane użytkownika
     * @throws UserNotLoaded Wywołane kiedy gracz nie był załadowany
     */
    public User getUser(UUID uuid) throws UserNotLoaded {
        if(LoadedUsers.containsKey(uuid)){
            return LoadedUsers.get(uuid);
        }else{
            throw new UserNotLoaded();
        }
    }
    /**
     * Nie zwraca NULL albo EXCEPTION ,ponieważ gdy użytkownik nie jest załadowany ładuje go
     * @param uuid Identyfikator gracza
     * @return Zwraca użytkownika
     */
    public User getUserForce(UUID uuid){
        if (!LoadedUsers.containsKey(uuid)) {
            try {
                DatabaseLoadUser(uuid);
            } catch (UserIsAlreadyLoaded userIsAlreadyLoaded) {
                userIsAlreadyLoaded.printStackTrace();
            }
        }
        return LoadedUsers.get(uuid);
    }
    /**
     * Sprawdza czy gracz jest załadowany
     * @param uuid Identyfikator gracza
     * @return Zwraca wartość true, false w stosunku załadowania gracza
     */
    public boolean isUserLoaded(UUID uuid){
        return LoadedUsers.containsKey(uuid);
    }
    /**
     * @return Zwraca całą tablice załadowanych użytkowników
     */
    public Collection<User> getAllUsers(){
        return LoadedUsers.values();
    }
    /**
     * Wyrzuca gracza z serwera
     * @param uuid Identyfikator gracza
     */
    public void Kick(UUID uuid){
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) {
            p.kickPlayer("");
        }
    }
    /**
     * Funkcja dzięki, której można załadować użytkownika
     * @param uuid Identyfikator gracza
     * @throws UserIsAlreadyLoaded Wywołane kiedy gracz już jest załadowany
     */
    public void DatabaseLoadUser(UUID uuid) throws UserIsAlreadyLoaded {
        if(LoadedUsers.containsKey(uuid)){
            throw new UserIsAlreadyLoaded();
        }else{
            ModerrkowoDatabase.getInstance().existsUser(uuid, new CallbackExists() {
                @Override
                public void onDone(Boolean result) {
                    if(result){
                        ModerrkowoDatabase.getInstance().getUser(uuid, new CallbackUserLoad() {
                            @Override
                            public void onDone(User result) {
                                LoadedUsers.put(uuid, result);
                                ModerrkowoDatabase.getInstance().sendUserLoaded(result);
                                ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Udało się załadować gracza", LogAction.UserManager_LOAD, LogResult.SUCCESS));
                            }

                            @Override
                            public void onFail(Exception cause) {
                                ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Nie udało się załadować gracza", LogAction.UserManager_LOAD, LogResult.EXCEPTION, cause));
                            }
                        });
                    }else{
                        Player p = Bukkit.getPlayer(uuid);
                        if(p != null){
                            User u = new User(uuid, p.getName());
                            LoadedUsers.put(uuid, u);
                            ModerrkowoDatabase.getInstance().sendUserRegister(u);
                            ModerrkowoDatabase.getInstance().sendUserLoaded(u);
                        }else{
                            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Nie udało się załadować gracza", LogAction.UserManager_LOAD, LogResult.EXCEPTION, new PlayerIsOffline()));
                        }
                    }
                }

                @Override
                public void onFail(Exception cause) {
                    ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Nie udało się załadować gracza", LogAction.UserManager_LOAD, LogResult.EXCEPTION, cause));
                    ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Wyrzucanie gracza...", LogAction.UserManager_LOAD, LogResult.EXCEPTION));
                    try{
                        ModerrkowoDatabase.getInstance().getUserManager().Kick(uuid);
                        ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Udało się wyrzucić gracza", LogAction.UserManager_LOAD, LogResult.SUCCESS));
                    }catch(Exception e){
                        ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Nie udało się wyrzucić gracza", LogAction.UserManager_LOAD, LogResult.EXCEPTION, e));
                    }
                }
            });
        }
    }
    /**
     * Rozładowywuje gracza
     * @param uuid Identyfikator gracza
     * @throws UserNotLoaded Wywołane kiedy gracza nie ma załadowanego
     */
    public void UnloadUser(UUID uuid, boolean save) throws UserNotLoaded {
        if(LoadedUsers.containsKey(uuid)){
            if(save){
                SaveUser(LoadedUsers.get(uuid));
            }
            ModerrkowoDatabase.getInstance().sendUserUnloaded(LoadedUsers.get(uuid));
            LoadedUsers.remove(uuid);
            Kick(uuid);
        }else{
            throw new UserNotLoaded();
        }
    }
    /**
     * Rozładowywuje gracza
     * @param user Instancja użytkownika
     * @throws UserNotLoaded Wywołane kiedy gracza nie ma załadowanego
     */
    public void UnloadUser(User user, boolean save) throws UserNotLoaded {
        UnloadUser(user.getUUID(), save);
    }
    /**
     * Zapisuje gracza do bazy danych
     * @param user Instancja użytkownika
     */
    public void SaveUser(User user){
        ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Zapisywanie gracza...", LogAction.UserManager_SAVE, LogResult.INFO));
        ModerrkowoDatabase.getInstance().existsUser(user.getUUID(), new CallbackExists() {
            @Override
            public void onDone(Boolean result) {
                if(result){
                    ModerrkowoDatabase.getInstance().updateUser(user, new CallbackEmpty() {
                        @Override
                        public void onDone() {
                            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Pomyślnie zapisano gracza", LogAction.UserManager_SAVE, LogResult.SUCCESS));
                            ModerrkowoDatabase.getInstance().sendUserSave(user);
                        }

                        @Override
                        public void onFail(Exception cause) {
                            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Wystąpił błąd podczas zapisywania gracza", LogAction.UserManager_SAVE, LogResult.EXCEPTION, cause));
                        }
                    });
                }else{
                    ModerrkowoDatabase.getInstance().insertUser(user, new CallbackEmpty() {
                        @Override
                        public void onDone() {
                            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Pomyślnie zapisano gracza", LogAction.UserManager_SAVE, LogResult.SUCCESS));
                            ModerrkowoDatabase.getInstance().sendUserSave(user);
                        }

                        @Override
                        public void onFail(Exception cause) {
                            ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Wystąpił błąd podczas zapisywania gracza", LogAction.UserManager_SAVE, LogResult.EXCEPTION, cause));
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception cause) {
                ModerrkowoDatabase.getInstance().sendLog(new DatabaseLog("Wystąpił błąd podczas zapisywania gracza", LogAction.UserManager_SAVE, LogResult.EXCEPTION, cause));
            }
        });
    }
}
