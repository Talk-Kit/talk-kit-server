package com.canal.service;

import com.canal.client.NHNAuthClient;
import com.canal.dto.RequestNHNToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class NHNAuthService {

    private final NHNAuthClient nhnAuthClient;

    @Value("${nhn.auth.tenantId}")
    private String tenantId;

    @Value("${nhn.auth.username}")
    private String username;

    @Value("${nhn.auth.password}")
    private String password;

    public NHNAuthService(NHNAuthClient nhnAuthClient) {
        this.nhnAuthClient = nhnAuthClient;
    }

    public String getNHNToken(){
        // 토큰 요청 및 발급
        RequestNHNToken requestNHNToken = new RequestNHNToken(tenantId, username, password);
        ResponseEntity<String> response = nhnAuthClient.getNHNToken(requestNHNToken);
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            // 응답 본문을 JsonNode로 변환
            JsonNode responseBody = objectMapper.readTree(response.getBody());
            String token = responseBody.path("access").path("token").get("id").asText();
            return token;
        }catch (Exception e){
            return null;
        }
    }
}
