package com.springblog.springblog.api.controller;

import com.springblog.springblog.api.request.Login;
import com.springblog.springblog.api.response.SessionResponse;
import com.springblog.springblog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        // json - 아이디/비밀번호
        log.info(">>>login={}", login);

        // 토근 생성
        String accessToken = authService.signin(login);

        return new SessionResponse(accessToken);

    }

}
