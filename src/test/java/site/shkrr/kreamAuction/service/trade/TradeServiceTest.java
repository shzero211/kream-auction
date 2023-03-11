package site.shkrr.kreamAuction.service.trade;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.shkrr.kreamAuction.controller.dto.AddressDto.AddressInfo;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.controller.dto.TradeDto.BidRequest;
import site.shkrr.kreamAuction.domain.address.Address;
import site.shkrr.kreamAuction.domain.address.AddressRepository;
import site.shkrr.kreamAuction.domain.brand.Brand;
import site.shkrr.kreamAuction.domain.brand.BrandRepository;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.product.ProductRepository;
import site.shkrr.kreamAuction.domain.product.common.Color;
import site.shkrr.kreamAuction.domain.product.common.ReleasePriceType;
import site.shkrr.kreamAuction.domain.trade.Trade;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.domain.user.UserRepository;
import site.shkrr.kreamAuction.domain.user.common.Role;

import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class TradeServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TradeService tradeService;
    private  User user;
    private  Address address;
    private  Brand brand;
    private Product product;

    @BeforeAll
    public void setUp(){
        //유저 생성
        user= User.builder()
                .email("shzero211@naver.com")
                .password("qwer!123")
                .phoneNum("01039229957")
                .role(Role.ROLE_ADMIN)
                .build();
        user=userRepository.save(user);

        //주소 생성
        address= Address.builder()
                .zipCode("08160")
                .roadNameAddress("시흥대로")
                .detailAddress("1011동")
                .name("김상훈")
                .phoneNum("01039229957")
                .user(user)
                .build();
        address=addressRepository.save(address);

        //브랜드 생성
         brand=Brand.builder()
                .nameKor("컨버스")
                .nameEng("converse")
                .imagePath("")
                .build();
        brand=brandRepository.save(brand);

        //상품 생성
        product= Product.builder()
                .brand(brand)
                .sizeGap(15.0)
                .minSize(230.0)
                .maxSize(280.0)
                .releasePriceType(ReleasePriceType.KRW)
                .releasePrice(80000L)
                .releaseDate(LocalDate.now())
                .imagePath("")
                .modelNum("12345")
                .nameEng("Converse Chuck 70 Ox Black")
                .nameKor("컨버스 척 70 로우 블랙")
                .color(Color.BLACK)
                .build();
        product=productRepository.save(product);

    }


    @Test
    public void  purchaseBidTest(){

        ProductInfo productInfo= ProductInfo.builder()
                .id(product.getId())
                .build();

        AddressInfo addressInfo= AddressInfo.builder()
                .id(address.getId())
                .build();

        BidRequest bidRequest = BidRequest.builder()
                .price(81000L)
                .productSize(245)
                .productInfo(productInfo)
                .addressInfo(addressInfo)
                .build();

        Trade trade=tradeService.purchaseBid(user,bidRequest);

        Assertions.assertEquals("08160 시흥대로 1011동",trade.getShippingEndAddress());
    }

    @Test
    public void  bidExceptionTest(){

        ProductInfo productInfo= ProductInfo.builder()
                .id(product.getId())
                .build();

        AddressInfo addressInfo= AddressInfo.builder()
                .id(address.getId())
                .build();

        BidRequest bidRequest = BidRequest.builder()
                .price(81000L)
                .productSize(240)
                .productInfo(productInfo)
                .addressInfo(addressInfo)
                .build();

        Assertions.assertThrows(RuntimeException.class,()-> tradeService.purchaseBid(user,bidRequest));
    }

    @Test
    public void  salesBidTest(){

        ProductInfo productInfo= ProductInfo.builder()
                .id(product.getId())
                .build();

        AddressInfo addressInfo= AddressInfo.builder()
                .id(address.getId())
                .build();

        BidRequest bidRequest = BidRequest.builder()
                .price(81000L)
                .productSize(245)
                .productInfo(productInfo)
                .addressInfo(addressInfo)
                .build();

        Trade trade=tradeService.salesBid(user,bidRequest);

        Assertions.assertEquals("08160 시흥대로 1011동",trade.getShippingStartAddress());
    }
}