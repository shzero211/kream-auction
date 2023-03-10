package site.shkrr.kreamAuction.service.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.controller.dto.TradeDto.BidRequest;
import site.shkrr.kreamAuction.domain.address.Address;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.trade.Trade;
import site.shkrr.kreamAuction.domain.trade.TradeRepository;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.service.address.AddressService;
import site.shkrr.kreamAuction.service.product.ProductService;
@RequiredArgsConstructor
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final ProductService productService;
    private final AddressService addressService;

    /*
    * 구매 입찰
    * */
    public Trade purchaseBid(User loginUser, BidRequest requestDto){

        Product product=productService.findById(requestDto.getProductInfo().getId())
                .orElseThrow(()->new RuntimeException("상품이 존재하지 않습니다."));

        Address address=addressService.findById(requestDto.getAddressInfo().getId())
                .orElseThrow(()->new RuntimeException("주소가 존재하지 않습니다."));

        //상품 사이즈 유효 검증
        if(checkProductSize(product,requestDto)){
            throw new RuntimeException("상품 사이즈가 유효하지 않습니다.");
        }
        String shippingEndAddress=makeShippingAddress(address);
        Trade trade=requestDto.toEntityForBuyer(loginUser,product,shippingEndAddress);

        return tradeRepository.save(trade);
    }

    /*
    * 판매 입찰
    * */
    public Trade salesBid(User loginUser,BidRequest requestDto){

        Product product=productService.findById(requestDto.getProductInfo().getId())
                .orElseThrow(()->new RuntimeException("상품이 존재하지 않습니다."));

        Address address=addressService.findById(requestDto.getAddressInfo().getId())
                .orElseThrow(()->new RuntimeException("주소가 존재하지 않습니다."));

        //상품 사이즈 유효 검증
        if(checkProductSize(product,requestDto)){
            throw new RuntimeException("상품 사이즈가 유효하지 않습니다.");
        }
        String shippingStartAddress=makeShippingAddress(address);
        Trade trade=requestDto.toEntityForSeller(loginUser,product,shippingStartAddress);

        return tradeRepository.save(trade);
    }

    /*
    * 주소엔티티로 거래 배송주소 생성
    * */
    private String makeShippingAddress(Address address) {
        StringBuffer sb=new StringBuffer();
        sb.append(address.getZipCode()+" "+address.getRoadNameAddress()+" "+address.getDetailAddress());
        return sb.toString();
    }

    /*
    * sizeGap 에 일치하는 상품인지 확인
    * */
    private boolean checkProductSize(Product product,BidRequest requestDto) {
        double productMaxSize=product.getMaxSize();
        double productMinSize=product.getMinSize();
        double productSize=requestDto.getProductSize();
        double perGap=product.getSizeGap();

        if(productMinSize<=productSize&&productSize<=productMaxSize){
            double gap=productSize-productMinSize;
            if(gap%perGap==0)
                return false;
        }

        return true;
    }
}
