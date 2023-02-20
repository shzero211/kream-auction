package site.shkrr.kreamAuction.domain.product;

import lombok.*;
import lombok.experimental.SuperBuilder;
import site.shkrr.kreamAuction.domain.BaseTimeEntity;
import site.shkrr.kreamAuction.domain.brand.Brand;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseTimeEntity {

    private String nameKor;

    private String nameEng;

    private String modelNum;

    private String releaseDate;

    private String color;

    private String releasePrice;

    private ReleasePriceType releasePriceType;

    private Double minSize;

    private Double maxSize;

    private Double sizeGap;

    private String imagePath;

    @ManyToOne(optional = false)//조회시 Inner join 강제
    @JoinColumn(name = "brand_id")
    private Brand brand;

}
