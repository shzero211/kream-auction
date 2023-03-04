package site.shkrr.kreamAuction.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.service.payment.PaymentService;

@RequiredArgsConstructor
@RequestMapping("/payment")
@Controller
public class PaymentController {
    @Value("${payment.client_key}")
    private String clientKey;

    private final PaymentService paymentService;
    //카드인증 클라이언트(빌링키와 함께사용될 고객키도 랜덤생성해서 전송)
    @GetMapping("/client")
    public String openClient(Model model){
        String str=Utils.random.makeRandomKey();
        model.addAttribute("customerKey", str);
        model.addAttribute("clientKey",clientKey);
        return "payment/payment_client";
    }


}
