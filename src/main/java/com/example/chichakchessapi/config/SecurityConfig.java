package com.example.chichakchessapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    @Profile("local")
    public SecurityFilterChain localFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    @Profile("!local")
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(List.of(
                                    "http://localhost:4200"
                            ));
                            corsConfiguration.setAllowedMethods(List.of(
                                    HttpMethod.GET.name(),
                                    HttpMethod.POST.name(),
                                    HttpMethod.PUT.name(),
                                    HttpMethod.PATCH.name(),
                                    HttpMethod.DELETE.name()
                            ));
                            corsConfiguration.setAllowedHeaders(List.of(
                                    "*"
                            ));
                            corsConfiguration.setAllowCredentials(true);
                            return corsConfiguration;
                        })
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(contextPath + "/**")
                        .ignoringRequestMatchers("/health-check")
                        .ignoringRequestMatchers("/swagger-ui/**")
                        .ignoringRequestMatchers("/v3/api-docs/**")
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(contextPath + "/**").permitAll()
                        .requestMatchers("/health-check").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}