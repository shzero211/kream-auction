package site.shkrr.kreamAuction.service.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.shkrr.kreamAuction.common.provider.JwtAuthProvider;
import site.shkrr.kreamAuction.domain.token.refreshToken.RefreshTokenRedisRepository;

//Jwt 인증 에 대한 Redis 를 이용하는 서비스
@RequiredArgsConstructor
@Service
public class JwtAuthRedisService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtAuthProvider jwtAuthProvider;

    //RefreshToken Claims 안에 UserId 를 이용한 토큰 검증
    public boolean isValidRefreshToken(String refreshToken){
        Long userId=getUserId(refreshToken);
        return refreshTokenRedisRepository.isEqual(userId,refreshToken);
    }

    // RefreshToken Claims 안에 UserId 추출
    public Long getUserId(String refreshToken){
        return Long.parseLong(String.valueOf(jwtAuthProvider.getClaims(refreshToken).getSubject()));
    }
}
