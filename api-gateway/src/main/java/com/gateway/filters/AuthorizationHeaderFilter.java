package com.gateway.filters;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    Environment env;

    public AuthorizationHeaderFilter( Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            //  header에 토큰 있는지 확인
            // 없으면
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange,"authorization header 없음", HttpStatus.UNAUTHORIZED);
            }

            String authorization = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorization.replace("Bearer ", "");

            // 토튼이 유효한지 확인함
            // 유효하지 않다면
            if (!isJwtValid(jwt)){
                return onError(exchange,"토큰 유효하지 않음", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        }));
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            subject = Jwts.parser()
                    .setSigningKey(env.getProperty("jwt.token").getBytes())
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();
        }catch (Exception e){
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            return false;
        }

        return returnValue;
    }

    // 비동기처리 : 단일값 -> Mono, 다중값 -> Flux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

    public static class Config { }
}
