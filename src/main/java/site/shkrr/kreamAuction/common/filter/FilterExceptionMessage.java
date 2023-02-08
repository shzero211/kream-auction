package site.shkrr.kreamAuction.common.filter;

import lombok.Getter;

@Getter
public enum FilterExceptionMessage {
    EXCEPTION_EXPIRED("토큰 유효기간이 만료되었습니다."),EXCEPTION_NOTMATCHSIG("시크니처가 일치하지않습니다.");
    private final String message;
    FilterExceptionMessage(String message){
        this.message=message;
    }
}
