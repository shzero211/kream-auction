package site.shkrr.kreamAuction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.service.user.UserService;

@RequiredArgsConstructor
@RequestMapping("/user/private")
@RestController
public class UserPrivateApiController {
    private final UserService userService;
    /**/
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("access_token")String accessToken){
        userService.logout(accessToken);
        return Utils.response.of("로그아웃 성공");
    }

    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestHeader("access_token")String accessToken){
        userService.deleteUser(accessToken);
        return Utils.response.of("회원탈퇴 성공");
    }
}
