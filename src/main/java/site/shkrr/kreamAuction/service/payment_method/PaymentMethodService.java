package site.shkrr.kreamAuction.service.payment_method;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.*;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.payment_method.PaymentMethod;

import site.shkrr.kreamAuction.domain.payment_method.PaymentMethodRepository;
import site.shkrr.kreamAuction.domain.payment_method.enums.Status;
import site.shkrr.kreamAuction.domain.payment_record.PaymentRecord;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.exception.payment.PaymentException;
import site.shkrr.kreamAuction.exception.payment.RequestBillingKeyException;
import site.shkrr.kreamAuction.service.encrypt.EncryptService;
import site.shkrr.kreamAuction.service.payment_method.enums.BankNames;
import site.shkrr.kreamAuction.service.payment_record.PaymentRecordService;
import site.shkrr.kreamAuction.service.product.ProductService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PaymentMethodService {

    @Value("${payment.authorization}")
    private String tossHttpAuthorization;
    private final EncryptService encryptService;
    private final ProductService productService;
    private final PaymentRecordService paymentRecordService;
    private final PaymentMethodRepository paymentMethodRepository;

    /*
    *client 정보로  대표 결제 수단 등록
    * */
    @Transactional
    public void save(BillingRequest requestDto, User loginUser){

        PaymentMethodSaveInfo paymentInfo=requestBillingKey(requestDto);

        PaymentMethod paymentMethod =paymentInfo.toEntity(loginUser);

        paymentMethodRepository.save(paymentMethod);
    }

    /*
     * 카드로 대표 결제 수단 등록
     * */
    @Transactional
    public void saveByCard(BillingRequestCardInfo requestDto,User loginUser){

        PaymentMethodSaveInfo paymentInfo=requestBillingKeyByCardInfo(requestDto);

        PaymentMethod paymentMethod =paymentInfo.toEntity(loginUser);

        paymentMethodRepository.save(paymentMethod);
    }

    /*
    * 대표 결제 수단으로 결제
    * */
    @Transactional
    public PaymentRecord pay(User loginUser, ProductInfo productInfo){

        //상품정보 정합성 확인
        if(!isValidProductInfo(productInfo)){
            throw new RuntimeException("일치하는 상품이 존재하지 않습니다.");
        }
        //로그인 유저의 대표 결제 수단 가져오기
        PaymentMethod paymentMethod=paymentMethodRepository.findByUserAndStatus(loginUser, Status.MAIN_CARD).orElseThrow(()->new RuntimeException("대표 결제수단이 등록되어 있지않습니다."));

        //결제
        return payByBillingKey(paymentMethod,productInfo);
    }

    /*
    * 결제취소
    * */
    @Transactional
    public void cancel(User loginUser, PaymentCancelRequest requestDto) {
        Optional<PaymentRecord> paymentRecord=paymentRecordService.findById(requestDto.getId());
        if(paymentRecord.isEmpty()||!paymentRecord.get().getPaymentKey().equals(requestDto.getPaymentKey())){
            throw new RuntimeException("일치하는 결제내역이 없습니다.");
        }
        cancelPayment(requestDto,paymentRecord.get());
    }

    @Transactional
    private void cancelPayment(PaymentCancelRequest requestDto,PaymentRecord paymentRecord) {
        String decryptPaymentKey= encryptService.decryptAES256(requestDto.getPaymentKey());

        requestDto.updatePaymentKey(decryptPaymentKey);

        StringBuilder urlBuilder=new StringBuilder();
        urlBuilder.append("https://api.tosspayments.com/v1/payments/");
        urlBuilder.append(requestDto.getPaymentKey());
        urlBuilder.append("/cancel");

        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(urlBuilder.toString()))
                .header("Authorization",tossHttpAuthorization)
                .header("Content-Type","application/json")
                .method("POST",HttpRequest.BodyPublishers.ofString(Utils.json.toJson(requestDto)))
                .build();

        HttpResponse<String> httpResponse=null;
        PaymentCancelResponse responseBody=null;

        try{
            httpResponse=HttpClient.newHttpClient().send(request,HttpResponse.BodyHandlers.ofString());
            responseBody=Utils.json.toObj(httpResponse.body(),PaymentCancelResponse.class);
        }catch (Exception e){
            throw new RuntimeException("결제 취소 요청 실패");
        }

        if(responseBody.getPaymentKey()==null){
            throw new PaymentException(httpResponse.body(),"결제 취소 에러 발생");
        }
        paymentRecordService.cancel(paymentRecord);
    }


    /*
     * SDK AuthKey 로 빌링키 발급
     * */
    @Transactional
    public PaymentMethodSaveInfo requestBillingKey(BillingRequest requestDto){

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
        //은행 코드 번호를 은행이름으로 변경
        String bankName= convertBankCodeToBankName(responseBody.getCard().getIssuerCode());

        // 암호화 해서 DB에 저장
        String  encryptBillingKey=encryptService.encryptAES256(responseBody.getBillingKey());
        String encryptCustomerKey=encryptService.encryptAES256(responseBody.getCustomerKey());

        PaymentMethodSaveInfo paymentSaveInfo=PaymentMethodSaveInfo.builder()
                .customerKey(encryptCustomerKey)
                .billingKey(encryptBillingKey)
                .bankName(bankName)
                .bankAccount(responseBody.getCard().getNumber())
                .build();

        return paymentSaveInfo;
    }

    /*
     * 카드정보로 빌링키 발급
     * */
    @Transactional
    public PaymentMethodSaveInfo requestBillingKeyByCardInfo(BillingRequestCardInfo requestDto){
        //customerKey 랜덤 생성후 요청에 삽입
        String customerKey=Utils.random.makeRandomKey();
        requestDto.addCustomerKey(customerKey);

        //Toss 에 RequestDto 정보를 통한 BillingKey 발급 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/authorizations/card"))
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

        // 암호화 해서 DB에 저장
        String  encryptBillingKey=encryptService.encryptAES256(responseBody.getBillingKey());
        String encryptCustomerKey=encryptService.encryptAES256(responseBody.getCustomerKey());

        PaymentMethodSaveInfo paymentSaveInfo=PaymentMethodSaveInfo.builder()
                .customerKey(encryptCustomerKey)
                .billingKey(encryptBillingKey)
                .bankName(bankName)
                .bankAccount(responseBody.getCard().getNumber())
                .build();

        return paymentSaveInfo;
    }


    /*
    * 결제전 요청한 상품정보,DB 정보와 일치 확인
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
    private PaymentRecord payByBillingKey(PaymentMethod paymentMethod,ProductInfo productInfo) {

        String decryptBillingKey = encryptService.decryptAES256(paymentMethod.getBillingKey());
        String decryptCustomerKey = encryptService.decryptAES256(paymentMethod.getCustomerKey());

        PayForPaymentMethodRequest request =paymentMethod.toPayForPaymentMethodRequest(productInfo,decryptCustomerKey);

        //토스에 전송할 자동 결제 승인 요청(Path + RequestBody)
        HttpRequest httpRequest=HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/"+ decryptBillingKey))
                .header("Authorization",tossHttpAuthorization)
                .header("Content-Type","application/json")
                .method("POST",HttpRequest.BodyPublishers.ofString(Utils.json.toJson(request)))
                .build();

        HttpResponse<String> response= null;
        PayForPaymentMethodResponse responseBody=null;

        try {//요청 전송
            response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            responseBody=Utils.json.toObj(response.body(), PayForPaymentMethodResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //결제 에러 발생시 응답받은 Json 정보로 처리
        if(responseBody.getOrderId()==null||responseBody.getPaymentKey()==null){
            throw new PaymentException(response.body(),"결제 실패 에러 발생");
        }

        //결제 내역 저장
        return paymentRecordService.save(responseBody,paymentMethod);
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
