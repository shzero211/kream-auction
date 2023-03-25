package site.shkrr.kreamAuction.domain.payment_record;

import lombok.*;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.payment_method.PaymentMethod;
import site.shkrr.kreamAuction.domain.payment_record.enums.Status;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class PaymentRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String paymentKey;

    private String orderId;

    private String orderName;

    private String totalAmount;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private PaymentMethod paymentMethod;

    private Status status;

    public void cancel() {
        this.status=Status.CANCELLED;
    }
}
