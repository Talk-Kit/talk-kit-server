package com.canal.client;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@Slf4j
public class HeaderConfig { // feign client 요청시 Authorization token 가로챔

    @Value("${open-ai.api.secret-key}")
    private String OPENAI_API_KEY;

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            ServletRequestAttributes attri = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(attri != null){
                HttpServletRequest request = attri.getRequest();
                // project-service feign client 통신시 헤더 설정
                if (requestTemplate.feignTarget().name().equals("project-service")){
                    String authorization = request.getHeader("Authorization");
                    if(authorization != null && authorization.startsWith("Bearer ")){
                        requestTemplate.header("Authorization", authorization);
                    }else {
                        log.error("Authorization header is missing or invalid.");
                    }
                }
                // CHAT-GPT 사용위한 OpenAi API 요청시 헤더 설정
                else if (requestTemplate.feignTarget().name().equals("openAIApiClient")) {
                    String openAiApiKey = "Bearer " + OPENAI_API_KEY;
                    requestTemplate.header("Authorization", openAiApiKey);
                    
                }
            }
        };
    }


}
