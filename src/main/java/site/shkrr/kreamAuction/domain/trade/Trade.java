package site.shkrr.kreamAuction.domain.trade;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.trade.common.Status;
import site.shkrr.kreamAuction.domain.user.User;

import javax.persistence.*;

@BatchSize(size = 20)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Trade extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_trade", sequenceName = "seq_trade", allocationSize = 20)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PUBLISHER_ID")
    private User publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER_ID")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long productPrice;

    private double productSize;

    private String shippingStartAddress;

    private String shippingEndAddress;

}
