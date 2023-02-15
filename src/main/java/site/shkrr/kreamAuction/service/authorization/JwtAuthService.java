package site.shkrr.kreamAuction.service.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.common.provider.JwtAuthProvider;
import site.shkrr.kreamAuction.domain.redis.AuthTokenRedisRepository;

//Jwt 인증 에 대한 Redis 를 이용하는 서비스
@RequiredArgsConstructor
@Service
public class JwtAuthService {
    private final AuthTokenRedisRepository authTokenRedisRepository;
    private final JwtAuthProvider jwtAuthProvider;

    //RefreshToken Claims 안에 UserId 를 이용한 토큰 검증
    public boolean isValidRefreshToken(String refreshToken){
        Long userId=getUserId(refreshToken);
        return authTokenRedisRepository.isEqual(userId,refreshToken);
    }

    // RefreshToken Claims 안에 UserId 추출
    public Long getUserId(String refreshToken){
        return Long.parseLong(String.valueOf(jwtAuthProvider.getClaims(refreshToken).getSubject()));
    }

    public void removeRefreshToken(Long userId) {
        authTokenRedisRepository.removeRefreshToken(userId);
    }

    public void saveBlackListToken(String accessToken,Long duration) {
        authTokenRedisRepository.saveBlackListToken(accessToken,duration);
    }
}
