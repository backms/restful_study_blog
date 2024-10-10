package com.springblog.springblog.api.controller;

import com.google.common.net.HttpHeaders;
import com.springblog.springblog.api.config.AppConfig;
import com.springblog.springblog.api.request.Login;
import com.springblog.springblog.api.response.SessionResponse;
import com.springblog.springblog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {

        // 토근 생성
//        String accessToken = authService.signin(login);

        // 쿠키 사용 start
//        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
//                .domain("localhost")
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//
//        log.info(">>>>>>>> cookie={}", cookie.toString());

//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                .build();
        // 쿠키 사용 end

        long userId = authService.signin(login);

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date())
                .compact();

        return new SessionResponse(jws);

    }

}
