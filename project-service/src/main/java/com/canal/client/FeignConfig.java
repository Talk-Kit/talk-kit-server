package com.canal.client;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class FeignConfig { // feign client 요청시 Authorization token 가로챔
    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            ServletRequestAttributes attri = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attri != null){
                HttpServletRequest request = attri.getRequest();

                // userSeq를 찾기 위한 member-service로의 요청만 Authorization Header를 가로챔
                if (requestTemplate.feignTarget().name().equals("user-service")){
                    String authorization = request.getHeader("Authorization");
                    if(authorization != null && authorization.startsWith("Bearer ")){
                        requestTemplate.header("Authorization", authorization);
                    }else {
                        log.error("Authorization header is missing or invalid.");
                    }
                }
            }
        };
    }


}
