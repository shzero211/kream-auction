package site.shkrr.kreamAuction.controller.payment_method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.BrandDto.CreateRequest;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.CardInfo;
import site.shkrr.kreamAuction.controller.dto.ProductDto;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.domain.brand.Brand;
import site.shkrr.kreamAuction.domain.brand.BrandRepository;
import site.shkrr.kreamAuction.domain.payment_method.PaymentMethod;
import site.shkrr.kreamAuction.domain.payment_method.PaymentMethodRepository;
import site.shkrr.kreamAuction.domain.payment_record.PaymentRecord;
import site.shkrr.kreamAuction.domain.payment_record.PaymentRecordRepository;
import site.shkrr.kreamAuction.domain.payment_record.enums.Status;
import site.shkrr.kreamAuction.domain.product.ProductRepository;
import site.shkrr.kreamAuction.domain.product.common.Color;
import site.shkrr.kreamAuction.domain.product.common.ReleasePriceType;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.domain.user.UserRepository;
import site.shkrr.kreamAuction.service.brand.BrandService;
import site.shkrr.kreamAuction.service.payment.PaymentService;
import site.shkrr.kreamAuction.service.payment_method.PaymentMethodService;
import site.shkrr.kreamAuction.service.payment_method.enums.BankNames;
import site.shkrr.kreamAuction.service.product.ProductService;
import site.shkrr.kreamAuction.service.user.UserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentMethodControllerTest {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRecordRepository paymentRecordRepository;
    private UserDetails loginUser;
    private PaymentRecord paymentRecord;

    @Value("${test_card_num}")
    private String cardNum;

    @Value("${test_card_year}")
    private String cardYear;

    @Value("${test_card_month}")
    private String cardMonth;

    @Value("${test_card_customer_id}")
    private String customerId;

    @Value("${test_card_password}")
    private String cardPassword;

    @BeforeAll
    public void setUp() throws IOException {
        //회원가입
        UserDto.UserSignUpRequestDto signUpRequestDto= UserDto.UserSignUpRequestDto.builder()
                .email("shzero211@naver.com")
                .password("qwer!123")
                .phoneNum("01039229957")
                .build();

        loginUser=userService.signUpForAdmin(signUpRequestDto);

        //브랜드 요청 생성
        CreateRequest brandRequest= CreateRequest.builder()
                .nameKor("나이키")
                .nameEng("nike")
                .build();

        //브랜드 이미지 생성
        BufferedImage brandImage=new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baoB=new ByteArrayOutputStream();
        ImageIO.write(brandImage,"PNG",baoB);
        byte[] bytesB=baoB.toByteArray();
        MockMultipartFile mockMultipartFileBrand=new MockMultipartFile("nike.png","nike.png",MediaType.MULTIPART_FORM_DATA.toString(),bytesB);

        //브랜드 엔티티 생성
        Brand brand=brandService.createBrand(brandRequest,mockMultipartFileBrand);

        //상품 요청 생성
        ProductDto.CreateRequest productRequest= ProductDto.CreateRequest.builder()
                .nameKor("나이키 에어포스")
                .nameEng("nike air force")
                .modelNum("315122-111")
                .color(Color.WHITE)
                .releaseDate(LocalDate.now())
                .releasePrice(120000L)
                .releasePriceType(ReleasePriceType.KRW)
                .minSize(230)
                .maxSize(280)
                .sizeGap(15)
                .brand(brand.toBrandInfo())
                .build();

        //상품 이미지 생성
        BufferedImage productImg=new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baoP=new ByteArrayOutputStream();
        ImageIO.write(productImg,"PNG",baoP);
        byte[] bytesP=baoP.toByteArray();
        MockMultipartFile mockMultipartFileProduct=new MockMultipartFile("air force.png","air force.png",MediaType.MULTIPART_FORM_DATA.toString(),bytesP);

        //상품 엔티티 생성
        productService.createProduct(productRequest,mockMultipartFileProduct);
    }

    /*
    * 카드번호로 결제수단 등록 테스트
    * */
    @Test
    public void saveTest() throws Exception {

        //로그인 처리
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser,"",loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //결제 수단 등록 요청 생성
        CardInfo cardInfo=  CardInfo.builder()
                .cardNumber(cardNum)
                .cardExpirationMonth(cardMonth)
                .cardExpirationYear(cardYear)
                .cardPassword(cardPassword)
                .customerIdentityNumber(customerId)
                .build();

        //결제 수단 등록
        MvcResult result=mockMvc.perform(post("/paymentMethod/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.json.toJson(cardInfo))
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
                .andReturn();

        String bankAccount = cardNum.substring(0, 8) + "****" + cardNum.substring(12, 15) + "*";

        PaymentMethod paymentMethod=paymentMethodRepository.findByBankAccount(bankAccount);

        Assertions.assertEquals(BankNames.KBANK.toString(),paymentMethod.getBankName());
    }
    @Test
    public void cancelTest() throws Exception {
        PaymentMethodDto.PaymentCancelRequest request= PaymentMethodDto.PaymentCancelRequest.builder()
                .id(paymentRecord.getId())
                .paymentKey(paymentRecord.getPaymentKey())
                .cancelReason("사이즈 안맞음")
                .build();

        MvcResult result =mockMvc.perform(post("/payment/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.json.toJson(request))
                         .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        PaymentRecord canceledPaymentRecord=paymentRecordRepository.findById(request.getId()).get();
        Assertions.assertEquals(Status.CANCELLED,canceledPaymentRecord.getStatus());
    }
}