package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;

public class AuthMapper {
    private AuthMapper() {
        throw new IllegalStateException("Mapper class");
    }

    public static RegisterModel convertRegisterRequestDTOToRegisterModel(RegisterRequestDTO registerRequestDTO) {
        return new RegisterModel(
                registerRequestDTO.nickname(),
                registerRequestDTO.email(),
                registerRequestDTO.password()
        );
    }

    public static LoginModel convertLoginRequestDTOToLoginModel(LoginRequestDTO loginRequestDTO) {
        return new LoginModel(
                loginRequestDTO.email(),
                loginRequestDTO.password()
        );
    }
}
