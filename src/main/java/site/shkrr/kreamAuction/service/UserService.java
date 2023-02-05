package site.shkrr.kreamAuction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.domain.users.User;
import site.shkrr.kreamAuction.domain.users.UserRepository;
import site.shkrr.kreamAuction.exception.user.DuplicateEmailException;
import site.shkrr.kreamAuction.exception.user.DuplicatePhoneNumException;
@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicated(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkPhoneNumDuplicated(String phoneNum){
        return userRepository.existsByPhoneNum(phoneNum);
    }

    @Transactional
    public User signUp(UserDto.UserSignUpRequestDto requestDto) {

        if(checkEmailDuplicated(requestDto.getEmail())){
            throw new DuplicateEmailException("이미 가입된 이메일 이 존재합니다.");
        }

        if(checkPhoneNumDuplicated(requestDto.getPhoneNum())){
            throw new DuplicatePhoneNumException("이미 가입된 휴대폰 번호 입니다.");
        }

        //이곳에 휴대폰 인증 번호 검증이 필요요

        return userRepository.save(User.builder()
                        .email(requestDto.getEmail())
                        .password(bCryptPasswordEncoder.encode(requestDto.getPassword()))
                        .phoneNum(requestDto.getPhoneNum())
                .build());
    }

}
