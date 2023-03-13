package site.shkrr.kreamAuction.service.trade;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.controller.dto.AddressDto.AddressInfo;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.controller.dto.TradeDto;
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
import site.shkrr.kreamAuction.domain.trade.TradeRepository;
import site.shkrr.kreamAuction.domain.trade.common.Status;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.domain.user.UserRepository;
import site.shkrr.kreamAuction.domain.user.common.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class TradeServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TradeRepository tradeRepository;

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

    @Test
    @DisplayName("모든 사이즈 즉시 구매가 거래정보 가져오기 테스트")
    public void getImmediatePurchaseInfoTest() throws InterruptedException {
        List<Trade> tradeList=new ArrayList<>();

        //10개의 같은 사이즈 구매입찰 거래정보 생성
        for(int i=1;i<=10;i++){
            Trade trade=Trade.builder()
                    .product(product)
                    .productSize(245)
                    .shippingStartAddress("address1")
                    .shippingEndAddress("address1")
                    .status(Status.sales_bid)
                    .productPrice(Long.valueOf(100*i))
                    .build();
            tradeList.add(trade);
        }

        Thread.sleep(3000);
        tradeRepository.saveAll(tradeList);
        tradeList=new ArrayList<>();

        //3초뒤 10개의 같은 사이즈 구매입찰 거래정보 추가
        for(int i=1;i<=10;i++){
            Trade trade=Trade.builder()
                    .product(product)
                    .productSize(245)
                    .status(Status.sales_bid)
                    .shippingStartAddress("address2")
                    .shippingEndAddress("address2")
                    .productPrice(Long.valueOf(100*i))
                    .build();
            tradeList.add(trade);
        }

        tradeRepository.saveAll(tradeList);

        TradeDto.ImmediateRequest requestDto= TradeDto.ImmediateRequest.builder()
                .productId(product.getId())
                .build();

        List<Trade> trades=tradeService.getImmediatePurchaseInfo(requestDto);
        Assertions.assertEquals(100,trades.get(0).getProductPrice());
        Assertions.assertEquals("address1",trades.get(0).getShippingStartAddress());
        Assertions.assertEquals(1,trades.size());
    }
    @Test
    @DisplayName("모든 사이즈 즉시 판매가 거래정보 가져오기 테스트")
    public void getImmediateSalesInfoTest() throws InterruptedException {
        List<Trade> tradeList=new ArrayList<>();

        //10개의 같은 사이즈 구매입찰 거래정보 생성
        for(int i=1;i<=10;i++){
            Trade trade=Trade.builder()
                    .product(product)
                    .productSize(245)
                    .shippingStartAddress("address1")
                    .shippingEndAddress("address1")
                    .status(Status.purchase_bid)
                    .productPrice(Long.valueOf(100*i))
                    .build();
            tradeList.add(trade);
        }

        Thread.sleep(3000);
        tradeRepository.saveAll(tradeList);
        tradeList=new ArrayList<>();

        //3초뒤 10개의 같은 사이즈 구매입찰 거래정보 추가
        for(int i=1;i<=10;i++){
            Trade trade=Trade.builder()
                    .product(product)
                    .productSize(245)
                    .status(Status.purchase_bid)
                    .shippingStartAddress("address2")
                    .shippingEndAddress("address2")
                    .productPrice(Long.valueOf(100*i))
                    .build();
            tradeList.add(trade);
        }

        tradeRepository.saveAll(tradeList);

        TradeDto.ImmediateRequest requestDto= TradeDto.ImmediateRequest.builder()
                .productId(product.getId())
                .build();

        List<Trade> trades=tradeService.getImmediateSalesInfo(requestDto);
        Assertions.assertEquals(1000,trades.get(0).getProductPrice());
        Assertions.assertEquals("address1",trades.get(0).getShippingStartAddress());
        Assertions.assertEquals(1,trades.size());
    }
}