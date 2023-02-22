package site.shkrr.kreamAuction.domain.brand;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Long> {
    boolean existsByNameKor(String nameKor);

    boolean existsByNameEng(String nameEng);
}
