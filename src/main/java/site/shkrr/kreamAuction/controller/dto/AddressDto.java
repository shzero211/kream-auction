package site.shkrr.kreamAuction.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.shkrr.kreamAuction.domain.address.Address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AddressDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SaveRequest{

        @NotBlank(message = "받는분 이름을(를) 입력해주세요.")
        private String name;

        @NotBlank(message = "받는분 번호을(를) 입력해주세요.")
        @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
        private String phoneNum;

        @NotBlank(message = "우편번호 을(를) 입력해주세요.")
        private String zipCode;

        @NotBlank(message = "도로명 주소 을(를) 입력해주세요.")
        private String roadNameAddress;

        @NotBlank(message = "상세주소 을(를) 입력해주세요.")
        private String detailAddress;

        public Address toEntity() {
            return Address.builder()
                    .name(name)
                    .phoneNum(phoneNum)
                    .zipCode(zipCode)
                    .roadNameAddress(roadNameAddress)
                    .detailAddress(detailAddress)
                    .build();
        }
    }
}
