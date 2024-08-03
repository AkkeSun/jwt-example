package com.jwtserver.global.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // --------------- 인증 정책 ---------------
        http
            .httpBasic().disable()
            .formLogin().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 인증 방식 미사용
            .and()
            // 기본 인증 필터 호출 전에 jwt 인증필터 호출
            .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationProvider),
                UsernamePasswordAuthenticationFilter.class)
        ;

        // --------------- 인증 예외 처리 ---------------
        http
            .exceptionHandling()
            .authenticationEntryPoint(new AuthenticationEntryPoint() {
                @Override
                public void commence(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException authException) throws IOException, ServletException {
                    response.sendRedirect("/errors/invalid-token");
                }
            })
        ;

        // --------------- 인가 정책 ---------------
        // 순서 : 1. 전체허용, 2. 아래로 갈수록 권한이 넓어지도록 설정
        http
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/errors/**").permitAll()
            .antMatchers("/api/users").hasRole("USER")
            .antMatchers("/api/admin").hasRole("ADMIN")
            .anyRequest().authenticated()
        ;

        //--------------- 인가 예외 처리 ---------------
        http
            .exceptionHandling()
            .accessDeniedHandler(new AccessDeniedHandler() {
                @Override
                public void handle(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.access.AccessDeniedException accessDeniedException)
                    throws IOException, ServletException {
                    response.sendRedirect("/errors/access-denied");
                }
            })
        ;

        //--------------- csrf, cors 설정 ---------------
        http
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
        ;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://localhost:8080");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}