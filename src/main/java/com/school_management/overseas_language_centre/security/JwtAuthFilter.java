package com.school_management.overseas_language_centre.security;

import com.school_management.overseas_language_centre.feature.intergration.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.school_management.overseas_language_centre.feature.auth.service.impl.TokenServiceImpl.TOKEN_KEY_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    // request ម្តង Filter ម្តង
    //Stateless
    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsInR5cGUiOiJhY2Nlc3MiLCJqdGkiOiI4NGUzMTRhMy00YTNiLTQ1ODktYTFhZi00ZTA2NjJiY2U2NzUiLCJpYXQiOjE3ODc2NzA3MTgsImV4cCI6MTc4Nzc1NzExOH0.IxGVEyPIfRNLfMOF1kIv5UFm1JFVUCi4-HxJ0eGaiX8Y5X70Zozp-iti3yCtbtl68YdrRqkrwPt8XAbB6TgQpw
        System.out.println("Request");
        System.out.println(request);
        String token = resolveToken(request);
        System.out.println("token");
        System.out.println(token);

        if (token == null || token.isBlank() || !jwtService.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }
        String username = jwtService.getUsernameFromToken(token);
        if (!isCurrentSession(username,token)) {
            chain.doFilter(request, response);
            return;
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);

    }
    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(authHeader);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }
    // true . false
    private boolean isCurrentSession(String username, String token) {
        try {
            Optional<String> store = redisService.get(TOKEN_KEY_PREFIX + username);
            return store.isPresent() && store.get().equals(token);
        }catch (Exception e){
            return false;
        }
    }
}
