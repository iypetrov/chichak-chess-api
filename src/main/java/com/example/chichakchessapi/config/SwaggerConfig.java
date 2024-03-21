package com.example.chichakchessapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "chichak-chess-api",
                version = "${app.version.major}.${app.version.minor}.${app.version.patch}",
                description = "University project for Development of web applications with Java"
        )
)
public class SwaggerConfig {
}