package com.canal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="user-service", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/user-service/client/user")
    Long getUserSeq(@RequestHeader("Authorization") String auth);
}
