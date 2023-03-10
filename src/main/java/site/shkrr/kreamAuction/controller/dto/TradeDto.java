package site.shkrr.kreamAuction.controller.dto;

import lombok.*;
import site.shkrr.kreamAuction.controller.dto.AddressDto.AddressInfo;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.trade.Trade;
import site.shkrr.kreamAuction.domain.trade.common.TradeStatus;
import site.shkrr.kreamAuction.domain.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class TradeDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BidRequest{
        @NotBlank(message = "가격을 입력해주세요")
        @Positive(message = "올바른 가격을 입력해주세요")
        private Long price;

        @NotBlank(message = "상품 사이즈를")
        @Positive(message = "올바른 가격을 입력해주세요")
        private double productSize;

        private ProductInfo productInfo;

        private AddressInfo addressInfo;

        public Trade toEntityForBuyer(User loginUser, Product product,String address) {
            return Trade.builder()
                    .price(price)
                    .productSize(productSize)
                    .status(TradeStatus.purchase_bid)
                    .product(product)
                    .shippingEndAddress(address)
                    .publisher(loginUser)
                    .buyer(loginUser)
                    .build();
        }
        public Trade toEntityForSeller(User loginUser, Product product,String address) {
            return Trade.builder()
                    .price(price)
                    .productSize(productSize)
                    .status(TradeStatus.sales_bid)
                    .product(product)
                    .shippingStartAddress(address)
                    .publisher(loginUser)
                    .seller(loginUser)
                    .build();
        }
    }

}
