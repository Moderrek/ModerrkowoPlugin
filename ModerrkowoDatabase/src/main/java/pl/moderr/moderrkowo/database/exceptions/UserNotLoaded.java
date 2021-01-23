package pl.moderr.moderrkowo.database.exceptions;

public class UserNotLoaded extends Exception{

    public UserNotLoaded(){
        super("Użytkownik nie jest załadowany");
    }

}
