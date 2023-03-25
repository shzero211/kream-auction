package site.shkrr.kreamAuction.domain.payment_method;

import lombok.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.PayForPaymentMethodRequest;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.payment_method.enums.Status;
import site.shkrr.kreamAuction.domain.user.User;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class PaymentMethod extends BaseTimeEntity {
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
    public PayForPaymentMethodRequest toPayForPaymentMethodRequest(ProductInfo productInfo, String decryptCustomerKey) {

        StringBuilder orderNameBuilder=new StringBuilder();

        orderNameBuilder.append(productInfo.getNameKor()+" "+productInfo.getColor());

        String newOrderId=Utils.random.makeRandomKey();

        return PayForPaymentMethodRequest.builder()
                .customerKey(decryptCustomerKey)
                .amount(productInfo.getPrice())
                .orderId(newOrderId)
                .orderName(orderNameBuilder.toString())
                .build();
    }
}
