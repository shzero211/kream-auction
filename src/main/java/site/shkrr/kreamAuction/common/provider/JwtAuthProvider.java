package site.shkrr.kreamAuction.common.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import site.shkrr.kreamAuction.domain.users.Role;
import site.shkrr.kreamAuction.service.UserAuthDetailService;
import site.shkrr.kreamAuction.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.zip.DataFormatException;
@RequiredArgsConstructor
@Component
public class JwtAuthProvider {
    @Value("${jwt.secret_key}")
    private String jwtSecretKey;

    private static final Long tokenValidTime=30*60*1000L;

    private final UserAuthDetailService userAuthDetailService;

    public String createToken(Long userId, Role role){
        Date now=new Date();
        Claims claims= Jwts.claims()
                .setSubject(userId.toString())//JWT body 에 sub:userId 삽입
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenValidTime));
        claims.put("role",role);// role: {role} 사입


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,jwtSecretKey)
                .compact();
    }
    //Request 의 Header 에서 AccessToken 추출
    public String getAccessToken(HttpServletRequest request) {
        return  request.getHeader("Authorization");
    }
    public Claims getClaims(String accessToken){
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(accessToken).getBody();
    }

    //AccessToken 이 만료 되었는지 확인
    public boolean isValid(String accessToken) {
        return getClaims(accessToken).getExpiration().after(new Date());
    }

    //AccessToken 정보를 통한 사용자 로그인 객체 생성
    public Authentication getAuthentication(String accessToken) {
        String userId=getClaims(accessToken).getSubject();
        UserDetails userDetails=userAuthDetailService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
}
