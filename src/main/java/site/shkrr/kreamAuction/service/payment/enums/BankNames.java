package site.shkrr.kreamAuction.service.payment.enums;

import lombok.Getter;
/*
* 은행 이름 반환을 위한 enum
* */
@Getter
public enum BankNames {
    IBK_BC("3K"),GWANGJUBANK("46"),LOTTE("71"),
    KDBBANK("30"),BC("31"),SAMSUNG("51"),SAEMAUL("38"),
    SHINHAN("41"),SHINHYEOP("62"),CITI("36"),
    WOORI("33"),POST("37"),SAVINGBANK("39"),
    JEONBUKBANK("35"),JEJUBANK("42"),KAKAOBANK("15"),
    KBANK("3A"),HYUNDAI("61"),TOSSBANK("24"),
    HANA("21"),KOOKMIN("11"),NONGHYEOP("91"),
    SUHYEOP("34");
    private String bankCode;
    BankNames(String bankCode){
        this.bankCode=bankCode;
    }
}
