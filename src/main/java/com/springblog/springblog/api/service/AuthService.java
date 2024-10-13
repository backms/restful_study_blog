package com.springblog.springblog.api.service;

import com.springblog.springblog.api.crypto.ScryptPasswordEncoder;
import com.springblog.springblog.api.domain.User;
import com.springblog.springblog.api.exception.AlreadyExistsEmailException;
import com.springblog.springblog.api.exception.InvalidSigninInformation;
import com.springblog.springblog.api.repository.UserRepository;
import com.springblog.springblog.api.request.Login;
import com.springblog.springblog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ScryptPasswordEncoder scryptPasswordEncoder;

    @Transactional
    public Long signin(Login login) throws InvalidSigninInformation {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        var matches = scryptPasswordEncoder.matches(login.getPassword(), user.getPassword());
        if (!matches) {
            throw new InvalidSigninInformation();
        }

        return user.getId();

    }

    public void signup(Signup signup) {

        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if(userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = scryptPasswordEncoder.encrypt(signup.getPassword());

        var user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        userRepository.save(user);

    }


}
