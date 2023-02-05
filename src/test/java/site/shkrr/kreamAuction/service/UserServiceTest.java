package site.shkrr.kreamAuction.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.domain.users.User;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private  UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void signUpTest(){
        UserDto.UserSignUpRequestDto requestDto=UserDto.UserSignUpRequestDto.builder().email("shzero211@naver.com").password("1234").phoneAuthNum("1234").phoneNum("01039229957").build();
        User user=userService.signUp(requestDto);
        assertEquals(true,passwordEncoder.matches(requestDto.getPassword(),user.getPassword()));
    }
}