package site.shkrr.kreamAuction.exception.user;

public class LoginPasswordNotMatchException extends RuntimeException{
    public LoginPasswordNotMatchException(String message){
        super(message);
    }
}
