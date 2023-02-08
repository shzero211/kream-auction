package site.shkrr.kreamAuction.common.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.shkrr.kreamAuction.domain.users.Role;

import java.util.Date;
import java.util.zip.DataFormatException;

@Component
public class JwtAuthProvider {
    @Value("${jwt.secret_key}")
    private String jwtSecretKey;
    private static final Long tokenValidTime=30*60*1000L;
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

}
