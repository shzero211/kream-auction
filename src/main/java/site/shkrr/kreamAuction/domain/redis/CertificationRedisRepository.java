package site.shkrr.kreamAuction.domain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import site.shkrr.kreamAuction.controller.dto.UserDto;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationKeyIsNullException;
import site.shkrr.kreamAuction.exception.smsCertification.CertificationNumNotMatchException;

import java.time.Duration;
/*
* 레디스로 인증 관리 서비스
* */
@RequiredArgsConstructor
@Repository
public class CertificationRedisRepository {
    private final RedisTemplate redisTemplate;
    private final int LIMIT_TIME=2; //만료 시간

    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key,value, Duration.ofMinutes(LIMIT_TIME));
    }
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean verify(String key, String value) {
        if(!hasKey(key)){
            throw new CertificationKeyIsNullException("휴대폰 인증을 먼저 해주세요.");
        }
        return isEqual(key,value);
    }

    public void  verify(UserDto.VerifyCertificationForPasswordRequest requestDto) {
        if(!hasKey(requestDto.getEmail())){
            throw new CertificationKeyIsNullException("인증키가 존재하지않습니다.");
        }
        if(!isEqual(requestDto.getEmail(),requestDto.getCertificationNum())){
            throw new CertificationNumNotMatchException("인증번호가 일치 하지않습니다.");
        }
        removeByKey(requestDto.getEmail());
    }
    public boolean isEqual(String key,String value){
        return redisTemplate.opsForValue().get(key).equals(value);
    }
    public void removeByKey(String key) {
        redisTemplate.delete(key);
    }
}
