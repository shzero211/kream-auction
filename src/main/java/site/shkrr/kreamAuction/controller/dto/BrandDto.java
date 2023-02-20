package site.shkrr.kreamAuction.controller.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

public class BrandDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateRequest{

        @NotBlank(message = "브랜드 이름(한글)을 입력해주세요.")
        private String nameKor;

        @NotBlank(message = "브랜드 이름(영어)을 입력해주세요.")
        private String nameEng;

    }
}
