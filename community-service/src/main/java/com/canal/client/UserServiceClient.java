package com.canal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/user-service/client/project/{userId}")
    Long getUserSeqByUserId(@PathVariable String userId);
}
