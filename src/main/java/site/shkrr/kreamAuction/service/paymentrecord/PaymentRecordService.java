package site.shkrr.kreamAuction.service.paymentrecord;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.PayForPaymentResponse;
import site.shkrr.kreamAuction.domain.payment.Payment;
import site.shkrr.kreamAuction.domain.paymentrecord.PaymentRecord;
import site.shkrr.kreamAuction.domain.paymentrecord.PaymentRecordRepository;
@RequiredArgsConstructor
@Service
public class PaymentRecordService {

    private final PaymentRecordRepository paymentRecordRepository;

    public void save(PayForPaymentResponse response, Payment payment){
        PaymentRecord paymentRecord=response.toPaymentRecord(payment);
        paymentRecordRepository.save(paymentRecord);
    }
}
