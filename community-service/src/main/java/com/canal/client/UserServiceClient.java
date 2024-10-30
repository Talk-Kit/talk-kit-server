package com.canal.client;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="user-service", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/user-service/client/user")
    Long getUserSeq(@RequestHeader("Authorization") String auth);

    @GetMapping("/api/user-service/client/specific/user")
    ResponseEntity<?> getUser(Long userSeq);
}
