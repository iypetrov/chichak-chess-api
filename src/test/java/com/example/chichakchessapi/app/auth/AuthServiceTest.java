package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class AuthServiceTest {
    @Autowired
    AuthService authService;

    @Test
    void testLoginWithCorrectAndWrongPassword() {
        String originalEmail = "user@gmail.com";
        String originalPassword = "user123";
        RegisterModel registration = new RegisterModel("userDge", originalEmail, originalPassword);
        LoginModel login = new LoginModel(originalEmail, "wrong");

        authService.register(registration);

        assertThrows(UnauthorizedException.class, () -> authService.login(login));

        login.setPassword(originalPassword);
        assertEquals(
                originalEmail,
                authService.login(login).getEmail()
        );
    }
}