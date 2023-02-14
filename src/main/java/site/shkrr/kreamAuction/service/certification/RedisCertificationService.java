package site.shkrr.kreamAuction.service.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationKeyIsNullException;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationNumNotMatchException;

import java.time.Duration;
/*
* 레디스로 인증 관리 서비스
* */
@RequiredArgsConstructor
@Service
public class RedisCertificationService {
    private final RedisTemplate redisTemplate;
    private final int LIMIT_TIME=2; //만료 시간

    public void saveCertificationNum(String phoneNum, String certificationNum) {
        redisTemplate.opsForValue().set(phoneNum,certificationNum, Duration.ofMinutes(LIMIT_TIME));
    }
    public void saveCertificationNumForPassword(String email,String certificationNum){
        redisTemplate.opsForValue().set(email,certificationNum,Duration.ofMinutes(LIMIT_TIME));
    }

    public boolean hasKey(String phoneNum) {
        return redisTemplate.hasKey(phoneNum);
    }

    public boolean verifyCertificationNum(String phoneNum, String certificationNum) {
        if(redisTemplate.opsForValue().get(phoneNum)==null){
            throw new CertificationKeyIsNullException("휴대폰 인증을 먼저 해주세요.");
        }
        return redisTemplate.opsForValue().get(phoneNum).equals(certificationNum);
    }

    public void  verifyCertificationNumForPassword(UserDto.VerifyCertificationForPasswordRequest requestDto) {
        if(redisTemplate.opsForValue().get(requestDto.getEmail())==null){
            throw new CertificationKeyIsNullException("인증키가 존재하지않습니다.");
        }
        if(!redisTemplate.opsForValue().get(requestDto.getEmail()).equals(requestDto.getCertificationNum())){
            throw new CertificationNumNotMatchException("인증번호가 일치 하지않습니다.");
        }
        redisTemplate.delete(requestDto.getEmail());
    }

    public void removeCertificationNum(String phoneNum) {
        redisTemplate.delete(phoneNum);
    }
}
