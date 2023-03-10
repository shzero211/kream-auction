package site.shkrr.kreamAuction.domain.trade;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.trade.common.TradeStatus;
import site.shkrr.kreamAuction.domain.user.User;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Trade extends BaseTimeEntity {
    @Id
    @GeneratedValue
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
    private TradeStatus status;

    private Long price;

    private double productSize;

    private String shippingStartAddress;

    private String shippingEndAddress;


}
