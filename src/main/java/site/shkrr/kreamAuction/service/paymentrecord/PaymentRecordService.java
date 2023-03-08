package site.shkrr.kreamAuction.service.paymentrecord;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.controller.dto.PaymentDto.PayForPaymentResponse;
import site.shkrr.kreamAuction.domain.payment.Payment;
import site.shkrr.kreamAuction.domain.paymentrecord.PaymentRecord;
import site.shkrr.kreamAuction.domain.paymentrecord.PaymentRecordRepository;
import site.shkrr.kreamAuction.service.encrypt.EncryptService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentRecordService {

    private final PaymentRecordRepository paymentRecordRepository;
    private final EncryptService encryptService;

    public PaymentRecord save(PayForPaymentResponse response, Payment payment){

        String encryptPaymentKey= encryptService.encryptAES256(response.getPaymentKey());
        PaymentRecord paymentRecord=response.toPaymentRecord(payment,encryptPaymentKey);

        return paymentRecordRepository.save(paymentRecord);
    }

    public Optional<PaymentRecord> findById(Long id) {
        return paymentRecordRepository.findById(id);
    }

    public void cancel(PaymentRecord paymentRecord) {
        paymentRecord.cancel();
        paymentRecordRepository.save(paymentRecord);
    }
}
