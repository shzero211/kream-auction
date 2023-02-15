package site.shkrr.kreamAuction.domain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import site.shkrr.kreamAuction.exception.user.RefreshTokenIsNotValid;

import java.time.Duration;

//Redis 를 이용한 Refresh 저장소
@RequiredArgsConstructor
@Repository
public class AuthTokenRedisRepository {

    private static final String TOKEN_KEY_HEAD="refresh_token_";
    private static final String BLACKLIST_KEY_HEAD="black_";
    private static final String BLACKLIST_VALUE="logout";
    private final RedisTemplate redisTemplate;

    //Token_Key_HEAD+userId:RefreshToken 형식으로 데이터 저장
    public void saveRefreshToken(String refreshToken,long userId,long limitTime){
        String tokenKey=makeRefreshTokenKey(userId);
        redisTemplate.opsForValue().set(tokenKey,refreshToken,Duration.ofMinutes(limitTime));
    }

    //TokenKey 를 통한 RefreshToken  동일성 검증
    public boolean isEqual(Long userId, String refreshToken) {
        String tokenKey=makeRefreshTokenKey(userId);
        Object storedRefreshToken=redisTemplate.opsForValue().get(tokenKey);
        if(storedRefreshToken==null){
            throw new RefreshTokenIsNotValid("해당 계정의 Refresh Token 은 만료 되었습니다.");
        }
        return storedRefreshToken.equals(refreshToken);
    }

    //TokenKey 생성 메서드
    public String makeRefreshTokenKey(Long userId){
        StringBuilder sb=new StringBuilder();
        sb.append(TOKEN_KEY_HEAD);
        sb.append(userId);
        return sb.toString();
    }

    public void removeRefreshToken(Long userId) {
        String tokenKey=makeRefreshTokenKey(userId);
        redisTemplate.delete(tokenKey);
    }

    public void saveBlackListToken(String accessToken,Long duration) {
        StringBuilder sb=new StringBuilder();
        sb.append(BLACKLIST_KEY_HEAD);
        sb.append(accessToken);
        redisTemplate.opsForValue().set(sb.toString(),BLACKLIST_VALUE,Duration.ofMillis(duration));
    }

    public boolean isInBlackList(String token) {
        StringBuilder sb=new StringBuilder();
        sb.append(BLACKLIST_KEY_HEAD);
        sb.append(token);
        return redisTemplate.hasKey(sb.toString());
    }
}
