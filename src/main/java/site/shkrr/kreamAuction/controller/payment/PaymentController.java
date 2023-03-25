package site.shkrr.kreamAuction.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.PaymentDto;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.BillingRequest;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.BillingRequestCardInfo;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.service.payment.PaymentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/payment")
@Controller
public class PaymentController {
    @Value("${payment.client_key}")
    private String clientKey;

    private final PaymentService paymentService;

    //카드인증 클라이언트 접속(빌링키와 함께사용될 고객키도 랜덤생성해서 전송)
    @GetMapping("/client")
    public String openClient(Model model){
        String str=Utils.random.makeRandomKey();
        model.addAttribute("customerKey", str);
        model.addAttribute("clientKey",clientKey);
        return "payment/payment_client";
    }

    //파라미터로 결제 수단 등록에 필요한 정보를 입력받아 결제수단 등록
    @GetMapping("")
    @ResponseBody
    public ResponseEntity savePaymentForClient(BillingRequest requestDto, @AuthenticationPrincipal User loginUser){
        paymentService.save(requestDto,loginUser);
        return Utils.response.of("Client 로 결제 수단 등록");
    }

    //카드정보로 결제 수단 등록
    @PostMapping("")
    @ResponseBody
    public ResponseEntity savePaymentForCard(@Valid @RequestBody BillingRequestCardInfo requestDto, @AuthenticationPrincipal User loginUser){
        paymentService.saveByCard(requestDto,loginUser);
        return Utils.response.of("카드 로 결제 수단 등록");
    }

    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity cancelPayment(@AuthenticationPrincipal User loginUser, @RequestBody PaymentDto.PaymentCancelRequest requestDto){
        paymentService.cancel(loginUser,requestDto);
        return Utils.response.of("결제 취소 성공");
    }
}
