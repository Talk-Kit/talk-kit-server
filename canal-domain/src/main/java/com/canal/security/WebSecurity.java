package com.canal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final JwtUtil jwtUtil;

    public WebSecurity(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/user-service/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/user-service/users").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/user-service/emailConfirm").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/user-service/emailCodeConfirm").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/user-service/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/project-service/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/script-service/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/community-service/v3/api-docs/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}