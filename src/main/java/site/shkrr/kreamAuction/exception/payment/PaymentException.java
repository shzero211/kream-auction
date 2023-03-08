package site.shkrr.kreamAuction.exception.payment;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException{
    private String errorResponse;
    public PaymentException(String errorResponse, String message){
        super(message);
        this.errorResponse=errorResponse;
    }
}
