package site.shkrr.kreamAuction.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.service.certification.SmsCertificationService;
import site.shkrr.kreamAuction.service.user.UserService;

import java.util.Map;

import static site.shkrr.kreamAuction.controller.dto.UserDto.UserSignUpRequestDto;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocalRunner implements CommandLineRunner {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;
    private final UserService userService;
    private final SmsCertificationService smsCertificationService;
    /*
    * Admin 유저 생성 및 로그인
    * */
    @Override
    public void run(String... args) throws Exception {
        UserSignUpRequestDto signUpRequestDto=UserSignUpRequestDto
                .builder()
                .email(adminEmail)
                .phoneNum("01039229957")
                .password(adminPassword)
                .build();

        userService.signUpForAdmin(signUpRequestDto);

        UserDto.UserLoginRequestDto loginRequestDto= UserDto.UserLoginRequestDto
                .builder()
                .email(adminEmail)
                .password(adminPassword)
                .build();

        Map<String,String> tokenMap =userService.login(loginRequestDto);

        for(String key : tokenMap.keySet()){
            log.info(key+","+tokenMap.get(key));
        }

    }
}
