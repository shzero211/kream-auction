package site.shkrr.kreamAuction.exception.user;

public class AuthLoadUserNotFoundException extends RuntimeException{
    public AuthLoadUserNotFoundException(String message){
        super(message);
    }
}
