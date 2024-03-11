package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.players.PlayerService;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@NonNullApi
@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    private final List<String> noAuthEndpoints;
    private final PlayerService playerService;
    private final AuthService authService;

    @PostConstruct
    private void initializeNoAuthEndpoints() {
        noAuthEndpoints.add("/health-check");
        noAuthEndpoints.add("/login");
        noAuthEndpoints.add("/register");
        noAuthEndpoints.add("/swagger-ui");
        noAuthEndpoints.add("/v3/api-docs");
    }

    public JWTRequestFilter(List<String> noAuthEndpoints, PlayerService playerService, AuthService authService) {
        this.noAuthEndpoints = noAuthEndpoints;
        this.playerService = playerService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean doesNeedAuth = noAuthEndpoints.stream()
                .noneMatch(endpoint -> request.getServletPath().contains(endpoint));

        if (doesNeedAuth) {
            List<Cookie> cookies = (request.getCookies() != null)
                    ? Arrays.asList(request.getCookies())
                    : List.of();

            boolean containsAuthCookie = cookies.stream()
                    .anyMatch(cookie -> cookie.getName().equals(AuthService.COOKIE_AUTH_TOKEN_NAME));

            if (!containsAuthCookie) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access Forbidden");
                return;
            }

            String jwtToken = cookies.stream()
                    .filter(x -> x.getName().equals(AuthService.COOKIE_AUTH_TOKEN_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse("");
            String userEmail = authService.extractClaims(jwtToken).getSubject();

            UserDetails userDetails = playerService.getPlayerByEmail(userEmail);

            if (!authService.verifyIfJWTTokenIsValid(jwtToken, userDetails)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Access Forbidden");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}