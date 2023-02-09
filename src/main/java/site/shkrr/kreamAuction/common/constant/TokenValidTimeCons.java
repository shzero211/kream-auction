package site.shkrr.kreamAuction.common.constant;

import lombok.Getter;

@Getter
public enum TokenValidTimeCons {
    ACCESS_VALID_TIME(30*60*1000L),REFRESH_VALID_TIME(14*24*60*60*1000L);
    private Long time;
    TokenValidTimeCons(Long time){
        this.time=time;
    }
}
