package site.shkrr.kreamAuction.common.filter;

import io.jsonwebtoken.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import site.shkrr.kreamAuction.common.provider.JwtAuthProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* Jwt 토큰을 통한 로그인 처리 Filter
*
* */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthProvider jwtAuthProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Request 에서 AccessToken 추출
        String accessToken=jwtAuthProvider.getAccessToken(request);

        if(accessToken!=null){
            //토큰 유효성 체크
            if(jwtAuthProvider.isValid(accessToken)){
                //로그인처리
                Authentication authentication=jwtAuthProvider.getAuthentication(accessToken);//로그인 설정시 SecurityContextHolder 안에 사용될 Authentication 생성
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);//다음 필터로 이동
    }
}
