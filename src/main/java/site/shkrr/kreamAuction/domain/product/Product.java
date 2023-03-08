package site.shkrr.kreamAuction.domain.product;

import lombok.*;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.brand.Brand;
import site.shkrr.kreamAuction.domain.product.common.ReleasePriceType;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String nameKor;

    private String nameEng;

    private String modelNum;

    private LocalDate releaseDate;

    private String color;

    private Long releasePrice;

    @Enumerated(EnumType.STRING)
    private ReleasePriceType releasePriceType;

    private Double minSize;

    private Double maxSize;

    private Double sizeGap;

    private String imagePath;

    @ManyToOne(optional = false)//조회시 Inner join 강제
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;

}
