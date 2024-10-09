package com.springblog.springblog.api.config;

import com.springblog.springblog.api.config.data.UserSession;
import com.springblog.springblog.api.domain.Session;
import com.springblog.springblog.api.exception.Unauthorized;
import com.springblog.springblog.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 헤더로 부터 인증값 받기
        String accessToken = webRequest.getHeader("Authorization");
        if(accessToken == null || accessToken.equals("")) {
            throw new Unauthorized();
        }

        // 데이터베이스 사용자 확인작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return new UserSession(session.getUser().getId());
    }

}
