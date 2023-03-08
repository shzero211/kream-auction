package site.shkrr.kreamAuction.domain.payment;

import lombok.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.PayForPaymentRequest;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.payment.enums.Status;
import site.shkrr.kreamAuction.domain.user.User;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String customerKey;

    private String billingKey;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String bankName;

    private String bankAccount;
    public PayForPaymentRequest toPayForPaymentRequest(ProductInfo productInfo,String decryptCustomerKey) {

        StringBuilder orderNameBuilder=new StringBuilder();

        orderNameBuilder.append(productInfo.getNameKor()+" "+productInfo.getColor());

        String newOrderId=Utils.random.makeRandomKey();

        return PayForPaymentRequest.builder()
                .customerKey(decryptCustomerKey)
                .amount(productInfo.getPrice())
                .orderId(newOrderId)
                .orderName(orderNameBuilder.toString())
                .build();
    }
}
