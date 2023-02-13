package site.shkrr.kreamAuction.common.constant;

import lombok.Getter;

@Getter
public enum TokenNameCons {
    ACCESS("access_token"),REFRESH("refresh_token");
    private String name;
    TokenNameCons(String name){
        this.name=name;
    }
}
