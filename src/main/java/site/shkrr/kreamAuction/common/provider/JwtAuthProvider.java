package site.shkrr.kreamAuction.common.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import site.shkrr.kreamAuction.common.constant.TokenNameCons;
import site.shkrr.kreamAuction.common.constant.TokenValidTimeCons;
import site.shkrr.kreamAuction.domain.redis.AuthTokenRedisRepository;
import site.shkrr.kreamAuction.domain.user.Role;
import site.shkrr.kreamAuction.service.user.UserAuthDetailService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtAuthProvider {
    @Value("${jwt.secret_key}")
    private String jwtSecretKey;


    private final UserAuthDetailService userAuthDetailService;

    private final AuthTokenRedisRepository authTokenRedisRepository;

    //AccessToken 생성
    public String createAccessToken(Long userId, Role role){
        Date now=new Date();
        Claims claims= Jwts.claims()
                .setSubject(String.valueOf(userId))//JWT body 에 sub:userId 삽입
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ TokenValidTimeCons.ACCESS_VALID_TIME.getTime()));
        claims.put("role",role);// role: {role} 사입


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,jwtSecretKey)
                .compact();
    }

    //UserId 받아서 RefreshToken 생성
    public String createRefreshToken(Long userId){
        Date now=new Date();
        Claims claims=Jwts.claims()
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(now.getTime()+TokenValidTimeCons.REFRESH_VALID_TIME.getTime()))
                .setIssuedAt(now);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,jwtSecretKey)
                .compact();
    }

    //두가지 토큰 생성후 Refresh Token 은 Redis 에 저장하고 Map 에 담아서 return
    public Map<String,String> createToken(Long userId,Role userRole){
        String accessToken=createAccessToken(userId,userRole);
        String refreshToken=createRefreshToken(userId);
        //JWT AccessToken,RefreshToken 발급
        Map<String,String> tokenMap=new HashMap<>();
        tokenMap.put(TokenNameCons.ACCESS.getName(),accessToken);
        tokenMap.put(TokenNameCons.REFRESH.getName(),refreshToken);
        authTokenRedisRepository.saveRefreshToken(refreshToken,userId,TokenValidTimeCons.REFRESH_VALID_TIME.getTime());
        return tokenMap;
    }

    //Request 의 Header 에서 AccessToken 추출
    public String getAccessToken(HttpServletRequest request) {
        return  request.getHeader(TokenNameCons.ACCESS.getName());
    }

    //Request 의 Header 에서 RefreshToken 추출
    public String getRefreshToken(HttpServletRequest request) {
        return  request.getHeader(TokenNameCons.REFRESH.getName());
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    }

    //token 유효성검사
    public boolean isValid(String token) {
        //유효기간 검사
        if(!getClaims(token).getExpiration().after(new Date())){
            return false;
        }

        //블랙 리스트 검사
        if(authTokenRedisRepository.isInBlackList(token)){
            return false;
        }

        return true;
    }

    //AccessToken 정보를 통한 사용자 로그인 객체 생성
    public Authentication getAuthentication(String accessToken) {
        String userId=getClaims(accessToken).getSubject();
        UserDetails userDetails=userAuthDetailService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

}
