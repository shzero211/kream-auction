package site.shkrr.kreamAuction.service.payment_record;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto;
import site.shkrr.kreamAuction.controller.dto.PaymentMethodDto.PayForPaymentMethodResponse;
import site.shkrr.kreamAuction.domain.payment_method.PaymentMethod;
import site.shkrr.kreamAuction.domain.payment_record.PaymentRecord;
import site.shkrr.kreamAuction.domain.payment_record.PaymentRecordRepository;
import site.shkrr.kreamAuction.service.encrypt.EncryptService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentRecordService {

    private final PaymentRecordRepository paymentRecordRepository;
    private final EncryptService encryptService;

    public PaymentRecord save(PayForPaymentMethodResponse response, PaymentMethod paymentMethod){

        String encryptPaymentKey= encryptService.encryptAES256(response.getPaymentKey());
        PaymentRecord paymentRecord=response.toPaymentRecord(paymentMethod,encryptPaymentKey);

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
