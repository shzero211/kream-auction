package site.shkrr.kreamAuction.controller.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import site.shkrr.kreamAuction.controller.dto.BrandDto.BrandInfo;
import site.shkrr.kreamAuction.domain.product.Product;
import site.shkrr.kreamAuction.domain.product.common.ReleasePriceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public class ProductDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateRequest{
        @NotBlank(message = "제품 한글명을 입력해주세요.")
        private String nameKor;

        @NotBlank(message = "제품 영문명을 입력해주세요.")
        private String nameEng;

        @NotBlank(message = "모델 넘버를 입력해주세요.")
        private String modelNum;

        @NotBlank(message = "색상을 입력해주세요.")
        private String color;

        @NotNull(message = "출시일을 입력해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate releaseDate;

        @Positive(message = "올바른 출시가를 입력해주세요.")//양수
        @NotNull(message = "출시가를 입력해주세요.")
        private Long releasePrice;

        @NotNull(message = "출시가 통화를 선택해주세요.")
        private ReleasePriceType releasePriceType;

        @Positive(message = "올바른 최소 사이즈를 입력해주세요.")
        @NotNull(message = "최소 사이즈를 입력해주세요.")
        private double minSize;

        @Positive(message = "올바른 최대 사이즈를 입력해주세요.")
        @NotNull(message = "최대 사이즈를 입력해주세요.")
        private double maxSize;

        @Positive(message = "올바른 사이즈 간격을 입력해주세요.")
        @NotNull(message = "사이즈 간격을 입력해주세요.")
        private double sizeGap;

        @NotNull(message = "브랜드를 선택해주세요.")
        private BrandInfo brand;

        private String imagePath;

        public Product toEntity(){
            return Product.builder()
                    .nameKor(nameKor)
                    .nameEng(nameEng)
                    .modelNum(modelNum)
                    .color(color)
                    .releaseDate(releaseDate)
                    .releasePrice(releasePrice)
                    .releasePriceType(releasePriceType)
                    .minSize(minSize)
                    .maxSize(maxSize)
                    .sizeGap(sizeGap)
                    .imagePath(imagePath)
                    .brand(brand.toEntity())
                    .build();
        }
        public void updateImgPath(String imagePath){
            this.imagePath=imagePath;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProductInfo{
        private Long id;
        private Long price;
        private String nameKor;
        private String nameEng;
        private String color;
        public ProductInfo(Long id,Long price,String nameKor,String nameEng){
            this.id=id;
            this.price=price;
            this.nameKor=nameKor;
            this.nameEng=nameEng;
        }
    }
}
