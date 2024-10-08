package com.canal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final JwtUtil jwtUtil;
    public WebSecurity(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors-> cors.configurationSource(cosConfig()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/user-service/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/user-service/email/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/user-service/check/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/user-service/join").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user-service/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/project-service/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/script-service/v3/api-docs")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/community-service/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/practice-service/v3/api-docs/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//    @Bean
//    public CorsConfigurationSource cosConfig() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 Origin 설정
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 메서드 설정
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With")); // 허용할 헤더 설정
//        configuration.setAllowCredentials(true); // 인증 정보 허용 여부 설정 (예: 쿠키)
//        configuration.setMaxAge(3600L); // preflight 요청의 캐시 시간 설정 (초 단위)
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
//        return source;
//    }

}