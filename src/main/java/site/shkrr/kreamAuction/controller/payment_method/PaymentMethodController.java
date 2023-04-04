package site.shkrr.kreamAuction.controller.payment_method;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.BillingRequest;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.CardInfo;
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.service.payment_method.PaymentMethodService;

@RequiredArgsConstructor
@RequestMapping("/paymentMethod")
@Controller
public class PaymentMethodController {
    @Value("${payment.client_key}")
    private String clientKey;

    private final PaymentMethodService paymentMethodService;

    //카드인증 클라이언트 호출(빌링키와 함께사용될 고객키도 랜덤생성해서 전송)
    @GetMapping("/client")
    public String openClient(Model model){
        String str=Utils.random.makeRandomKey();
        model.addAttribute("customerKey", str);
        model.addAttribute("clientKey",clientKey);
        return "payment/payment_client";
    }

    //클라이언트 인증 후 결제 수단 등록
    @GetMapping("/save")
    @ResponseBody
    public ResponseEntity save(BillingRequest billingRequest, @AuthenticationPrincipal User loginUser){
        paymentMethodService.save(billingRequest,loginUser);
        return Utils.response.of("클라이언트로 결제 수단 등록 성공");
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity save(@RequestBody CardInfo cardInfo,@AuthenticationPrincipal User loginUser){
        paymentMethodService.save(cardInfo,loginUser);
        return Utils.response.of("카드번호로 결제 수단 등록 성공");
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity cancelPayment(@AuthenticationPrincipal User loginUser, @RequestBody PaymentMethodDto.PaymentCancelRequest requestDto){
        paymentMethodService.cancel(loginUser,requestDto);
        return Utils.response.of("결제 취소 성공");
    }
}
