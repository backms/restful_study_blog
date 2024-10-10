package com.springblog.springblog.api.config;

import com.springblog.springblog.api.config.data.UserSession;
import com.springblog.springblog.api.domain.Session;
import com.springblog.springblog.api.exception.Unauthorized;
import com.springblog.springblog.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 쿠키 사용시 start
        // 헤더로 부터 인증값 받기
//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if(servletRequest == null){
//            log.error("servletRequest is null");
//            throw new Unauthorized();
//        }
//
//        Cookie[] cookies = servletRequest.getCookies();
//
//        if(cookies.length == 0) {
//            log.error("쿠기가 없음");
//            throw new Unauthorized();
//        }
//
//        String accessToken = cookies[0].getValue();

//        Session session = sessionRepository.findByAccessToken(accessToken)
//                .orElseThrow(Unauthorized::new);
        // 쿠키 사용시 end

        String jws = webRequest.getHeader("Authorization");;
        if(jws == null || jws.equals("")) {
             throw new Unauthorized();
        }

        try {

            Jws<Claims> claims =  Jwts.parserBuilder()
                    .setSigningKey(appConfig.getJwtKey())
                    .build()
                    .parseClaimsJws(jws);

            String userId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));

        } catch (JwtException e) {
            throw new Unauthorized();
        }

    }

}
