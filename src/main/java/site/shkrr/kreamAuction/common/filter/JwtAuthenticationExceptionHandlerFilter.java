package site.shkrr.kreamAuction.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import site.shkrr.kreamAuction.common.utils.Utils;
import site.shkrr.kreamAuction.exception.user.AuthLoadUserNotFoundException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
* Filter 의 Exception 은 SpringContainer 외부에서 발생하기 때문에
* Exception 처리 하기위해서는 해당 Filter 앞에 ExceptionHandlerFilter 를 생성해서 지정해주면된다.
* */
@Slf4j
@Component
public class JwtAuthenticationExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (ExpiredJwtException ex){//Jwt 토큰 만료 예외처리
            log.debug(String.valueOf(ex));
            setExceptionResponse(response,FilterExceptionMessage.EXCEPTION_EXPIRED.getMessage());
        }catch (AuthLoadUserNotFoundException ex){//User Load 과정중 예외처리
            log.debug(String.valueOf(ex));
            setExceptionResponse(response,ex.getMessage());
        }catch (SignatureException ex){//Signature 불일치 예외처리
            log.debug(String.valueOf(ex));
            setExceptionResponse(response,FilterExceptionMessage.EXCEPTION_NOTMATCHSIG.getMessage());
        }
    }

    //Exception 예외처리 응답 설정
    private void setExceptionResponse(HttpServletResponse response,String message)throws IOException{
        //상태코드 및 응답 메시지 형식 지정
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String,Object> map=new HashMap<>();
        map.put("error_msg",message);
        response.getWriter().write(Utils.json.toJson(map));
    }
}
