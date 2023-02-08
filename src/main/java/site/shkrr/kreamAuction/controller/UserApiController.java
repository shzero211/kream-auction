package site.shkrr.kreamAuction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.service.UserService;
import site.shkrr.kreamAuction.service.certification.SmsCertificationService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {
    private final UserService userService;
    private final SmsCertificationService smsCertificationService;

    /*
    * sms 인증 메세지 전송
    * */
    @PostMapping("/sms")
    public ResponseEntity sendSms(@RequestBody String phoneNum){
        String phoneNumToStr=Utils.json.toMap(phoneNum).get("phoneNum").toString();
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

    @PostMapping("/signIn")
    public ResponseEntity signIn(@Valid @RequestBody UserDto.UserLoginRequestDto requestDto){
        String jwtToken=userService.login(requestDto);
        return Utils.response.of("로그인 성공 하였습니다.",jwtToken);
    }
}
