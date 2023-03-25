package site.shkrr.kreamAuction.domain.payment_record;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord,Long> {
    PaymentRecord findByPaymentKey(String paymentKey);
}
