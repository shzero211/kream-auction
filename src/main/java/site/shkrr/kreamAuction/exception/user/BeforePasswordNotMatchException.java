package site.shkrr.kreamAuction.exception.user;

public class BeforePasswordNotMatchException extends RuntimeException{
    public BeforePasswordNotMatchException(String message){
        super(message);
    }
}
