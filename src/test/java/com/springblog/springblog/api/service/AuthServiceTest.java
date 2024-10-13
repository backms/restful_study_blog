package com.springblog.springblog.api.service;

import com.springblog.springblog.api.crypto.ScryptPasswordEncoder;
import com.springblog.springblog.api.domain.User;
import com.springblog.springblog.api.exception.AlreadyExistsEmailException;
import com.springblog.springblog.api.exception.InvalidSigninInformation;
import com.springblog.springblog.api.repository.UserRepository;
import com.springblog.springblog.api.request.Login;
import com.springblog.springblog.api.request.Signup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .email("mixoo@gmail.com")
                .password("1234")
                .name("mixoo")
                .build();

        // when
        authService.signup(signup);

        // then
        Assertions.assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().iterator().next();

        assertEquals("mixoo@gmail.com", user.getEmail());
        assertNotNull(user.getPassword());
        assertNotEquals("1234", user.getPassword());
        assertEquals("mixoo", user.getName());

    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일")
    void test2() {


        // given
        User user = User.builder()
                .email("mixoo@gmail.com")
                .password("1234")
                .name("mixoo2")
                .build();

        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("mixoo@gmail.com")
                .password("1234")
                .name("mixoo")
                .build();

        // when
        Assertions.assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));

    }

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("mixoo@gmail.com")
                .password(encryptedPassword)
                .name("mixoo")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                    .email("mixoo@gmail.com")
                    .password("1234")
                    .build();
        // when
        Long userId = authService.signin(login);

        // then
        Assertions.assertNotNull(userId);

    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀림")
    void test4() {
        // given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        Signup signup = Signup.builder()
                .email("mixoo@gmail.com")
                .password(encryptedPassword)
                .name("mixoo")
                .build();
        authService.signup(signup);

        Login login = Login.builder()
                .email("mixoo@gmail.com")
                .password("5678")
                .build();

        // expected
        Assertions.assertThrows(InvalidSigninInformation.class, () -> authService.signin(login));

    }

}