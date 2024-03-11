package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;

public class AuthMapper {
    private AuthMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static RegisterModel convertRegisterRequestDTOToRegisterModel(RegisterRequestDTO registerRequest) {
        return new RegisterModel(
                registerRequest.nickname(),
                registerRequest.email(),
                registerRequest.password()
        );
    }

    public static LoginModel convertLoginRequestDTOToLoginModel(LoginRequestDTO loginRequest) {
        return new LoginModel(
                loginRequest.email(),
                loginRequest.password()
        );
    }
}