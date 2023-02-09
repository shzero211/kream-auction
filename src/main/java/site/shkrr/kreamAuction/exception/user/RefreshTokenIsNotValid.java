package site.shkrr.kreamAuction.exception.user;

public class RefreshTokenIsNotValid extends RuntimeException{
    public RefreshTokenIsNotValid(String message){
        super(message);
    }
}
