package site.shkrr.kreamAuction.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.service.certification.EmailCertificationService;
import site.shkrr.kreamAuction.service.certification.SmsCertificationService;
import site.shkrr.kreamAuction.service.user.UserService;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/public")
@RestController
public class UserPublicApiController {
    private final UserService userService;
    private final SmsCertificationService smsCertificationService;

    private final EmailCertificationService emailCertificationService;

    /*
    * sms 인증 메세지 전송
    * */
    @PostMapping("/sms")
    public ResponseEntity sendSms(@RequestBody String phoneNum){
        String phoneNumToStr=String.valueOf(Utils.json.toObj(phoneNum,String.class));
        smsCertificationService.sendTo(phoneNumToStr);
        return Utils.response.of("메세지 전송 성공!");
    }

    /*
    * sms 인증 번호 검증
    * */
    @PostMapping("/sms/confirm")
    public ResponseEntity smsConfirm(@RequestBody UserDto.UserSmsConfirmRequestDto requestDto){
        smsCertificationService.verifyNum(requestDto.getPhoneNum(), requestDto.getCertificationNum());
        return Utils.response.of("인증 성공");
    }

    /*
    회원 가입 요청
    * request 정보 유효성 검사
    * 중복 이메일 검사
    * 중복 핸드폰 번호 검사
    * 휴대폰 인증 번호 검사
    * */
    @PostMapping("/signUp")
    public ResponseEntity signUp(@Valid @RequestBody UserDto.UserSignUpRequestDto requestDto){
        userService.signUp(requestDto);
        return Utils.response.of("회원가입에 성공하였습니다.");
    }

    /*
    * 로그인 입력에 대한 로그인 요청 처리
    * AccessToken,RefreshToken 발급
    */
    @PostMapping("/signIn")
    public ResponseEntity signIn(@Valid @RequestBody UserDto.UserLoginRequestDto requestDto){
        Map<String,String> tokenMap=userService.login(requestDto);
        return Utils.response.of("로그인 성공 하였습니다.",tokenMap);
    }

    /*
    * AccessToken 만료시 RefreshToken 을 이용한 재발급 요청 처리
    * */
    @PostMapping("/signIn/refresh")
    public ResponseEntity signInRefresh(@RequestHeader("refresh_token") String refreshToken){
        log.info(refreshToken);
        Map<String,String>tokenMap=userService.loginRefresh(refreshToken);
        return Utils.response.of("새로운 Access Token 발급 을 완료하였습니다.",tokenMap);
    }

    /*
    * 비밀번호 찾기 (페이지로 이동)
    * */
    @GetMapping("/find-password")
    public ResponseEntity findPassword(){
    return Utils.response.of("비밀번호 찾기 페이지로 이동");
    }

    /*
    * 비밀번호 찾기 (입력된 이메일로 인증번호 발급)
    *  */
    @PostMapping("/find-password")
    public ResponseEntity sendCertificationForPassword(@RequestBody String email){
        emailCertificationService.sendCertificationForPassword(email);
        return Utils.response.of("비밀번호 찾기 인증번호 발급 성공");
    }

    /*
    * 비밀번호 찾기 (인증번호 확인 후 삭제)
    * */
    @PostMapping("/find-password/verify")
    public ResponseEntity verifyCertificationForPassword(@Valid @RequestBody UserDto.VerifyCertificationForPasswordRequest requestDto){
        emailCertificationService.verifyCertificationNum(requestDto);
        return Utils.response.of("비밀번호 찾기 이메일 인증 성공");
    }

    /*
    * 비밀번호 찾기(이전 비밀번호 일치시 비밀번호 변경)
    * */
    @PostMapping("/find-password/change")
    public ResponseEntity afterCertificationChangePassword(@Valid @RequestBody UserDto.ChangePasswordRequest requestDto){
        userService.changePassword(requestDto);
        return Utils.response.of("비밀번호 변경 성공");
    }
}
