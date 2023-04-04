package site.shkrr.kreamAuction.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import site.shkrr.kreamAuction.domain.payment_method.PaymentMethod;
import site.shkrr.kreamAuction.domain.payment_method.enums.Status;
import site.shkrr.kreamAuction.domain.payment_record.PaymentRecord;
import site.shkrr.kreamAuction.domain.user.User;

import java.util.Date;

import static site.shkrr.kreamAuction.domain.payment_record.enums.Status.*;

public class PaymentMethodDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BillingRequest {
        private String customerKey;
        private String authKey;
    }

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static  class CardInfo{
        private String customerKey;
        private String cardNumber;
        private String cardExpirationYear;
        private String cardExpirationMonth;
        private String cardPassword;
        private String customerIdentityNumber;

        public void addCustomerKey(String customerKey) {
            this.customerKey=customerKey;
        }
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BillingResponse {
        private String mId; //상점아이디
        private String customerKey; //고객아이디
        private Date authenticatedAt; //자동결제수단 인증 시점
        private String method; //결제수단(카드고정)
        private String billingKey; //빌링키(자동결제에서 카드정보 대신 사용)
        private BillingCardInfo card;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BillingCardInfo{
        private String issuerCode; //카드 발급사 코드
        private String acquirerCode; //카드 매입사 코드
        private String number; //카드번호
        private String cardType; //카드종류(신용,체크,기프트)
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PaymentMethodSaveInfo{
        private String customerKey;
        private String billingKey;

        private String bankName;

        private String bankAccount;
        public PaymentMethod toEntity(User user){
            return PaymentMethod.builder()
                    .customerKey(customerKey)
                    .billingKey(billingKey)
                    .user(user)
                    .bankName(bankName)
                    .bankAccount(bankAccount)
                    .status(Status.MAIN_CARD)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PayForPaymentMethodRequest{
        private String customerKey; //고객아이디
        private Long amount; //결제 금액
        private String orderId; //주문아이디(6~64자,영문 대소문자,숫자,특수문자 - , _ , = , . ,@)
        private String orderName; //주문명
    }

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PayForPaymentMethodResponse{
        private String paymentKey; //결제 정보키(결제 취소시 사용됨)
        private String orderId; //주문 아이디
        private String orderName; //주문명
        private String totalAmount; //결제금액
        public PaymentRecord toPaymentRecord(PaymentMethod paymentMethod, String encryptPaymentKey){
            return PaymentRecord.builder()
                    .paymentMethod(paymentMethod)
                    .paymentKey(encryptPaymentKey)
                    .orderId(orderId)
                    .orderName(orderName)
                    .totalAmount(totalAmount)
                    .status(PAID)
                    .build();
        }
    }

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PaymentCancelRequest{

        private Long id;

        private String paymentKey;

        private String cancelReason;

        public void updatePaymentKey(String decryptPaymentKey) {
            this.paymentKey=decryptPaymentKey;
        }
    }

    @Getter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class PaymentCancelResponse{
        private String paymentKey;
    }
}
