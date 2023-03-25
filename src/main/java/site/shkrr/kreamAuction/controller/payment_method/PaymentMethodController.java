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
import site.shkrr.kreamAuction.controller.dto.ProductDto.ProductInfo;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.service.payment_method.PaymentMethodService;

@RequiredArgsConstructor
@RequestMapping("/PaymentMethod")
@Controller
public class PaymentMethodController {
    @Value("${payment.client_key}")
    private String clientKey;

    private final PaymentMethodService paymentMethodService;
    //카드인증 클라이언트(빌링키와 함께사용될 고객키도 랜덤생성해서 전송)
    @GetMapping("/client")
    public String openClient(Model model){
        String str=Utils.random.makeRandomKey();
        model.addAttribute("customerKey", str);
        model.addAttribute("clientKey",clientKey);
        return "payment/payment_client";
    }

    @GetMapping("")
    @ResponseBody
    public ResponseEntity save(BillingRequest requestDto, @AuthenticationPrincipal User loginUser){
        paymentMethodService.save(requestDto,loginUser);
        return Utils.response.of("빌링키 발급 성공");
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity payForPayment(@AuthenticationPrincipal User loginUser, @RequestBody ProductInfo requestDto){
        paymentMethodService.pay(loginUser,requestDto);
        return Utils.response.of("결제 성공");
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity cancelPayment(@AuthenticationPrincipal User loginUser, @RequestBody PaymentMethodDto.PaymentCancelRequest requestDto){
        paymentMethodService.cancel(loginUser,requestDto);
        return Utils.response.of("결제 취소 성공");
    }
}
