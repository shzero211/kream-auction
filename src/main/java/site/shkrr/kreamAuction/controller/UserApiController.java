package site.shkrr.kreamAuction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.service.UserService;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {
    private final UserService userService;
    /*
    * request 유효성검사
    * 중복 이메일 검사
    * 중복 핸드폰 번호 검사
    * */
    @PostMapping
    public ResponseEntity signUp(@Valid @RequestBody UserDto.UserSignUpRequestDto requestDto){

        userService.signUp(requestDto);

        return Utils.ResponseUtil.of("회원가입에 성공하였습니다.");
    }
}
