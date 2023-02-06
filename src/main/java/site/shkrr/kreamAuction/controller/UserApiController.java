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

    @PostMapping("/sms")
    public ResponseEntity sendSms(@RequestBody String phoneNum){
        String phoneNumToStr=Utils.json.toMap(phoneNum).get("phoneNum").toString();
        log.info(phoneNumToStr);
        smsCertificationService.sendTo(phoneNumToStr);
        return Utils.response.of("메세지 전송 성공!");
    }
    @PostMapping("/sms/confirm")
    public void smsConfirm(){

    }
    /*
    회원 가입 요청
    * request 정보 유효성검사
    * 중복 이메일 검사
    * 중복 핸드폰 번호 검사
    * */
    @PostMapping
    public ResponseEntity signUp(@Valid @RequestBody UserDto.UserSignUpRequestDto requestDto){

        userService.signUp(requestDto);

        return Utils.response.of("회원가입에 성공하였습니다.");
    }
}
