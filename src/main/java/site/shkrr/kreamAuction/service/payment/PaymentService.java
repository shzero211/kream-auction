package site.shkrr.kreamAuction.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.BillingRequest;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.PayForPaymentRequest;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.PayForPaymentResponse;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.payment.Payment;
import site.shkrr.kreamAuction.domain.payment.PaymentRepository;
import site.shkrr.kreamAuction.domain.payment.enums.Status;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.exception.payment.PaymentException;
import site.shkrr.kreamAuction.exception.payment.RequestBillingKeyException;
import site.shkrr.kreamAuction.service.encrypt.EncryptService;
import site.shkrr.kreamAuction.service.payment.enums.BankNames;
import site.shkrr.kreamAuction.service.product.ProductService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static site.shkrr.kreamAuction.controller.dto.PaymentDto.BillingResponse;
import static site.shkrr.kreamAuction.controller.dto.PaymentDto.PaymentSaveInfo;

@RequiredArgsConstructor
@Service
public class PaymentService {

    @Value("${payment.authorization}")
    private String tossHttpAuthorization;
    private final PaymentRepository paymentRepository;
    private final EncryptService encryptService;
    private final ProductService productService;
    /*
    * 대표 결제 수단 등록
    * */
    public void save(BillingRequest requestDto,User loginUser){

        PaymentSaveInfo paymentInfo=requestBillingKey(requestDto);

        Payment payment =paymentInfo.toEntity(loginUser);

        paymentRepository.save(payment);
    }

    /*
    * 대표 결제 수단으로 결제
    * */
    public void pay(User loginUser, ProductInfo productInfo){

        //상품정보 정합성 확인
        if(!isValidProductInfo(productInfo)){
            throw new RuntimeException("일치하는 상품이 존재하지 않습니다.");
        }
        //로그인 유저의 대표 결제 수단 가져오기
        Payment payment=paymentRepository.findByUserAndStatus(loginUser, Status.MAIN_CARD);

        //결제
        payByBillingKey(payment,productInfo);
    }

    /*
    * 결제전 요청한 상품정보,DB정보와 일치 확인
    * */
    private boolean isValidProductInfo(ProductInfo productInfo) {
        Optional<Product> product =productService.findById(productInfo.getId());
        if(product.isEmpty()||!product.get().getNameKor().equals(productInfo.getNameKor())||!product.get().getNameEng().equals(productInfo.getNameEng())){
            return false;
        }
        return true;
    }

    /*
    * 결제 처리 로직
    * */
    private void payByBillingKey(Payment payment,ProductInfo productInfo) {
        String decryptBillingKey = encryptService.decryptAES256(payment.getBillingKey());
        PayForPaymentRequest request =payment.toPayForPaymentRequest(productInfo);

        //토스에 전송할 자동 결제 승인 요청(Path + RequestBody)
        HttpRequest httpRequest=HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/"+ decryptBillingKey))
                .header("Authorization",tossHttpAuthorization)
                .header("Content-Type","application/json")
                .method("POST",HttpRequest.BodyPublishers.ofString(Utils.json.toJson(request)))
                .build();

        HttpResponse<String> response= null;
        PayForPaymentResponse responseBody=null;

        try {//요청 전송
            response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            responseBody=Utils.json.toObj(response.body(), PayForPaymentResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //결제 에러 발생시 응답받은 Json 정보로 처리
        if(responseBody.getOrderId()==null||responseBody.getPaymentKey()==null){
            throw new PaymentException(response.body(),"결제 실패 에러 발생");
        }

    }


    /*
    * 빌링키 발급
    * */
    public PaymentSaveInfo requestBillingKey(BillingRequest requestDto){

        //Toss 에 RequestDto 정보를 통한 BillingKey 발급 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/issue"))
                .header("Authorization",tossHttpAuthorization )
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(Utils.json.toJson(requestDto)))
                .build();

        HttpResponse<String> response = null;
        BillingResponse responseBody=null;

        try {//BillingKey 발급 요청
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            responseBody=Utils.json.toObj(response.body(), BillingResponse.class);
        } catch (Exception e) {
            throw new RequestBillingKeyException("빌링키 발급 API 요청 실패");
        }

        if(responseBody.getBillingKey()==null){//API 에러 메세지가 들어와서 Json Mapping 이 안된경우
            throw new PaymentException(response.body(),"빌링키 발급 에러 발생");
        }

        String bankName= convertBankCodeToBankName(responseBody.getCard().getIssuerCode());

        //BillingKey 암호화 해서 DB에 저장
        String  encryptBillingKey=encryptService.encryptAES256(responseBody.getBillingKey());
        PaymentSaveInfo paymentSaveInfo=PaymentSaveInfo.builder()
                .customerKey(responseBody.getCustomerKey())
                .billingKey(encryptBillingKey)
                .bankName(bankName)
                .bankAccount(responseBody.getCard().getNumber())
                .build();

        return paymentSaveInfo;
    }

    /*
    * 빌링키 발급시 제공하는 은행코드->은행 이름으로 변경 처리
    * */
    private String convertBankCodeToBankName(String bankCode) {
        BankNames[] bankNames=BankNames.values();
        for(BankNames name:bankNames){
            if(name.getBankCode().equals(bankCode)){
                return name.toString();
            }
        }
        return null;
    }
}
