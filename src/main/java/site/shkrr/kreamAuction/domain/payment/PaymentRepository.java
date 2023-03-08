package site.shkrr.kreamAuction.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.shkrr.kreamAuction.domain.payment.enums.Status;
import site.shkrr.kreamAuction.domain.user.User;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    //Payment 와 User 가 Entity 에서 Optional false 이기때문에 join fetch 사용하여 Payment 정보 가져옴
    @Query("select p from Payment p join fetch p.user where p.user= :user And p.status= :status")
    Optional<Payment> findByUserAndStatus(@Param("user") User loginUser, @Param("status") Status status);
}
