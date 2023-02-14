package site.shkrr.kreamAuction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.shkrr.kreamAuction.common.constant.TokenNameCons;
import site.shkrr.kreamAuction.common.provider.JwtAuthProvider;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.domain.user.Role;
import site.shkrr.kreamAuction.domain.user.User;
import site.shkrr.kreamAuction.domain.user.UserRepository;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationNumExpireException;
import site.shkrr.kreamAuction.exception.user.*;
import site.shkrr.kreamAuction.service.authorization.JwtAuthRedisService;
import site.shkrr.kreamAuction.service.certification.RedisCertificationService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService{
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    private final RedisCertificationService redisCertificationService;

    private final JwtAuthProvider jwtAuthProvider;

    private final JwtAuthRedisService jwtAuthRedisService;

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicated(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkPhoneNumDuplicated(String phoneNum){
        return userRepository.existsByPhoneNum(phoneNum);
    }

    @Transactional(readOnly = true)
    private boolean checkCertificationNumIsValid(String phoneNum,String certificationNum) {
        return redisCertificationService.verifyCertificationNum(phoneNum,certificationNum)==false;
    }

    @Transactional(readOnly = true)
    private boolean checkLoginPasswordMatch(String loginPassword,String dbPassword) {
        return bCryptPasswordEncoder.matches(loginPassword,dbPassword);
    }

    @Transactional
    public User signUp(UserDto.UserSignUpRequestDto requestDto) {

        if(checkEmailDuplicated(requestDto.getEmail())){
            throw new DuplicateEmailException("이미 가입된 이메일 이 존재합니다.");
        }

        if(checkPhoneNumDuplicated(requestDto.getPhoneNum())){
            throw new DuplicatePhoneNumException("이미 가입된 휴대폰 번호 입니다.");
        }

        if(checkCertificationNumIsValid(requestDto.getPhoneNum(), requestDto.getCertificationNum())){
            throw new CertificationNumExpireException("회원 가입에 대한 인증시간이 만료 되었습니다.");
        }

        redisCertificationService.removeCertificationNum(requestDto.getPhoneNum());
        //이곳에 휴대폰 인증 번호 검증이 필요요

        return userRepository.save(User.builder()
                        .email(requestDto.getEmail())
                        .password(bCryptPasswordEncoder.encode(requestDto.getPassword()))
                        .phoneNum(requestDto.getPhoneNum())
                        .role(Role.ROLE_USER)
                .build());
    }

    @Transactional(readOnly = true)
    public Map login(UserDto.UserLoginRequestDto requestDto) {

        User loginUser=userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->new LoginEmailHasNotEntityException("해당 이메일을 가진 회원은 존재하지 않습니다."));

        if(!checkLoginPasswordMatch(requestDto.getPassword(),loginUser.getPassword())){
            throw new LoginPasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        //JWT AccessToken,RefreshToken 발급
        Map<String,String> tokenMap=jwtAuthProvider.createToken(loginUser.getId(),loginUser.getRole());

        return tokenMap;
    }

    //Refresh 토큰 유효성을 검사하고 새로운 AccessToken  발급
    @Transactional(readOnly = true)
    public Map loginRefresh(String refreshToken) {

        //Redis 를 이용한 유효성 검사
        if(!jwtAuthRedisService.isValidRefreshToken(refreshToken)){
            throw new RefreshTokenIsNotValid("해당 계정의 Refresh Token 은 변경 되었습니다.");
        }

        Long userId= jwtAuthRedisService.getUserId(refreshToken);
        //Token 생성시 DB 와의 정합성을 위한 DB 정보 추출
        User user=userRepository.findById(userId).orElseThrow(()->new LoginRefreshNotFoundUser("login Refresh 과정중 회원 정보를 찾지 못하였습니다."));

        //새로운 AccessToken 발급
        String newAccessToken=jwtAuthProvider.createAccessToken(user.getId(),user.getRole());
        Map<String,String> map=new HashMap<>();
        map.put(TokenNameCons.ACCESS.getName(),newAccessToken);
        map.put(TokenNameCons.REFRESH.getName(),refreshToken);
        return map;
    }

    @Transactional
    public void changePassword(UserDto.ChangePasswordRequest requestDto) {
        User user=userRepository.findByEmail(requestDto.getEmail()).orElseThrow(()->new ChangePasswordUserNotFoundException("해당 이메일을 가진 User 는 존재하지 않습니다."));
        if(!isMatchBeforePassword(user,requestDto.getBeforePassword())){
            throw new BeforePasswordNotMatchException("입력된 이전 비밀번호는 일치하지 않습니다.");
        }
        user.updatePassword(bCryptPasswordEncoder.encode(requestDto.getNewPassword()));//비밀번호 변경
    }

    public boolean isMatchBeforePassword(User user,String beforePassword){
        return bCryptPasswordEncoder.matches(beforePassword, user.getPassword());
    }
}
