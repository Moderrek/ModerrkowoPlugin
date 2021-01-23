package pl.moderr.moderrkowo.database.exceptions;

public class UserIsAlreadyLoaded extends Exception{

    public UserIsAlreadyLoaded(){
        super("Użytkownik jest już załadowany");
    }

}
