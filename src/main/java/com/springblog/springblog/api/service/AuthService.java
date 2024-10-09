package com.springblog.springblog.api.service;

import com.springblog.springblog.api.domain.Session;
import com.springblog.springblog.api.domain.User;
import com.springblog.springblog.api.exception.InvalidSigninInfomation;
import com.springblog.springblog.api.repository.UserRepository;
import com.springblog.springblog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signin(Login login) throws InvalidSigninInfomation {
        // DB 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInfomation());

        Session session = user.addSession();

        return session.getAccessToken();

    }

}
