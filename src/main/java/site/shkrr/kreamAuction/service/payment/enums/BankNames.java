package site.shkrr.kreamAuction.service.payment.enums;

import lombok.Getter;
/*
* 은행 이름 반환을 위한 enum
* */
@Getter
public enum BankNames {
    IBK_BC("3K"),GWANGJUBANK("46"),LOTTE("71"),WOORI("33");
    private String bankCode;
    BankNames(String bankCode){
        this.bankCode=bankCode;
    }
}
