package site.shkrr.kreamAuction.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.shkrr.kreamAuction.controller.dto.ProductDto;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByModelNum(String modelNum);

    @Query(value = "select new site.shkrr.kreamAuction.controller.dto.ProductDto$ProductInfo(p.id,p.releasePrice,p.nameKor,p.nameEng) from Product p where p.id=:id")
    ProductDto.ProductInfo getProductInfoById(@Param(value = "id") long id);
}
