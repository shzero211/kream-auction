package site.shkrr.kreamAuction.controller.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserSignUpRequestDto{

        @NotBlank(message = "이메일(을)를 입력해주세요.")
        @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호(을)를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$",message = "영문,특수문자,숫자가1개이상 있는 8~16자리 비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "휴대폰 번호(을)를 입력해주세요.")
        @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
        private String phoneNum;

        @NotBlank(message = "인증번호가 비어있습니다.")
        private String certificationNum;

    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserSmsConfirmRequestDto{
        private String phoneNum;
        private String certificationNum;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserLoginRequestDto {

        @NotBlank(message = "이메일(을)를 입력해주세요.")
        @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호(을)를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$",message = "영문,특수문자,숫자가1개이상 있는 8~16자리 비밀번호를 입력해주세요.")
        private String password;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class VerifyCertificationForPasswordRequest{

        @NotBlank(message = "이메일(을)를 입력해주세요.")
        @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
        private String email;
        @NotBlank(message="인증번호(을)를 입력해주세요.")
        String certificationNum;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChangePasswordRequest{

        @NotBlank(message = "이메일(을)를 입력해주세요.")
        @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
        private String email;

        @NotBlank(message = "이전 비밀번호(을)를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$",message = "영문,특수문자,숫자가1개이상 있는 8~16자리 비밀번호를 입력해주세요.")
        private String beforePassword;

        @NotBlank(message = "변경 비밀번호(을)를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$",message = "영문,특수문자,숫자가1개이상 있는 8~16자리 비밀번호를 입력해주세요.")
        private String newPassword;
    }

}
