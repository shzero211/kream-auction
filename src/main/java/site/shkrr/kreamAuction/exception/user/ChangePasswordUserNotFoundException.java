package site.shkrr.kreamAuction.exception.user;

public class ChangePasswordUserNotFoundException extends  RuntimeException{
    public ChangePasswordUserNotFoundException(String message){
        super(message);
    }

}
