package site.shkrr.kreamAuction.domain.payment_method;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.shkrr.kreamAuction.domain.payment_method.enums.Status;
import site.shkrr.kreamAuction.domain.user.User;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long> {
    //PaymentMethod 와 User 가 Entity 에서 Optional false 이기때문에 join fetch 사용하여 PaymentMethod 정보 가져옴
    @Query("select p from PaymentMethod p join fetch p.user where p.user= :user And p.status= :status")
    Optional<PaymentMethod> findByUserAndStatus(@Param("user") User loginUser, @Param("status") Status status);

    @Query("select p from PaymentMethod p where p.bankAccount= :bankAccount")
    PaymentMethod findByBankAccount(@Param("bankAccount") String bankAccount);
}
