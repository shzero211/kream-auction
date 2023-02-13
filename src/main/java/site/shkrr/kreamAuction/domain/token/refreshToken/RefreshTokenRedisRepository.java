package site.shkrr.kreamAuction.domain.token.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import site.shkrr.kreamAuction.exception.user.RefreshTokenIsNotValid;

import java.time.Duration;

//Redis 를 이용한 Refresh 저장소
@RequiredArgsConstructor
@Repository
public class RefreshTokenRedisRepository {

    private static final String TOKEN_KEY_HEAD="refresh_token_";
    private final RedisTemplate redisTemplate;

    //TokenKey:RefreshToken 형식으로 데이터 저장
    public void save(String refreshToken,long userId,long limitTime){
        String tokenKey=makeTokenKey(userId);
        redisTemplate.opsForValue().set(tokenKey,refreshToken,Duration.ofMinutes(limitTime));
    }

    //TokenKey 를 통한 RefreshToken  동일성 검증
    public boolean isEqual(Long userId, String refreshToken) {
        String tokenKey=makeTokenKey(userId);
        Object storedRefreshToken=redisTemplate.opsForValue().get(tokenKey);
        if(storedRefreshToken==null){
            throw new RefreshTokenIsNotValid("해당 계정의 Refresh Token 은 만료 되었습니다.");
        }
        return storedRefreshToken.equals(refreshToken);
    }

    //TokenKey 생성 메서드
    public String makeTokenKey(Long userId){
        StringBuilder sb=new StringBuilder();
        sb.append(TOKEN_KEY_HEAD);
        sb.append(userId);
        return sb.toString();
    }
}
