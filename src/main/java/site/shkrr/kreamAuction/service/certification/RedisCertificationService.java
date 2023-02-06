package site.shkrr.kreamAuction.service.certification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationKeyIsNullException;

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

    public boolean hasKey(String phoneNum) {
        return redisTemplate.hasKey(phoneNum);
    }

    public boolean verifyCertificationNum(String phoneNum, String certificationNum) {
        if(redisTemplate.opsForValue().get(phoneNum)==null){
            throw new CertificationKeyIsNullException("휴대폰 인증을 먼저 해주세요.");
        }
        return redisTemplate.opsForValue().get(phoneNum).equals(certificationNum);
    }

    public void removeCertificationNum(String phoneNum) {
        redisTemplate.delete(phoneNum);
    }
}
