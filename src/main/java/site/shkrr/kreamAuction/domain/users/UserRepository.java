package site.shkrr.kreamAuction.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNum(String phoneNum);

    Optional<User> findByEmail(String email);
}
